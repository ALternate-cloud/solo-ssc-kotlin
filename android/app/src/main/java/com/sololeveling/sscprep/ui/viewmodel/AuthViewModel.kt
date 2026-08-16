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
                _authState.value = AuthState.Error(e.message ?: "Network error occurred")
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
                    _authState.value = AuthState.Error(response.message ?: "Registration failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Network error occurred")
            }
        }
    }

    fun logout() {
        authRepo.logout()
        _authState.value = AuthState.LoggedOut
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.LoggedOut
        }
    }
}
