package com.sololeveling.sscprep.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sololeveling.sscprep.auth.AuthRepository
import com.sololeveling.sscprep.network.LoginRequest
import com.sololeveling.sscprep.network.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Loading : AuthState()
    object LoggedOut : AuthState()
    data class LoggedIn(val username: String, val hunterName: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepo = AuthRepository(application)
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        if (authRepo.isLoggedIn()) {
            val username = authRepo.getUsername() ?: ""
            val hunterName = authRepo.getHunterName() ?: ""
            _authState.value = AuthState.LoggedIn(username, hunterName)
        } else {
            _authState.value = AuthState.LoggedOut
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepo.login(LoginRequest(username, password))
                if (response.success && response.user != null) {
                    _authState.value = AuthState.LoggedIn(response.user.username, response.user.hunterName ?: "Hunter")
                } else {
                    _authState.value = AuthState.Error(response.message ?: "Login failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(extractErrorMessage(e))
            }
        }
    }

    fun register(username: String, password: String, email: String, hunterName: String = username) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepo.register(RegisterRequest(username, password, email, hunterName))
                if (response.success && response.user != null) {
                    _authState.value = AuthState.LoggedIn(response.user.username, response.user.hunterName ?: hunterName)
                } else {
                    _authState.value = AuthState.Error(response.message.ifBlank { "Registration failed" })
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(extractErrorMessage(e))
            }
        }
    }

    private fun extractErrorMessage(e: Exception): String {
        if (e is retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                try {
                    val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true; isLenient = true }
                    val errorObj = json.decodeFromString<com.sololeveling.sscprep.network.AuthResponse>(errorBody)
                    if (!errorObj.message.isNullOrBlank()) {
                        return errorObj.message
                    }
                } catch (_: Exception) {}
            }
            return when (e.code()) {
                400 -> "Invalid credentials. If you haven't created an account yet, please tap 'Awaken Here' below!"
                401 -> "Invalid username or password. Access denied."
                404 -> "Account not found. Please register first."
                500, 502, 503 -> "Server is starting up on Render. Please retry in 10-15 seconds!"
                else -> "Server response error (HTTP ${e.code()})"
            }
        }
        val msg = e.localizedMessage ?: e.message ?: ""
        if (msg.contains("Unable to resolve host") || msg.contains("Failed to connect") || msg.contains("timeout")) {
            return "Connecting to server... (Render free instances take ~15s to wake up on first request). Please retry in a moment!"
        }
        return if (msg.isNotBlank() && !msg.startsWith("HTTP")) msg else "Authentication failed. Please check your credentials or register first."
    }

    fun logout() {
        authRepo.logout()
        _authState.value = AuthState.LoggedOut
    }

    fun deleteAccount(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = authRepo.deleteAccount()
                if (response.success) {
                    _authState.value = AuthState.LoggedOut
                    onSuccess()
                } else {
                    onError(response.message.ifBlank { "Could not delete account." })
                }
            } catch (e: Exception) {
                onError(extractErrorMessage(e))
            }
        }
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.LoggedOut
        }
    }
}
