package com.sololeveling.sscprep.auth

import android.content.Context
import android.content.SharedPreferences
import com.sololeveling.sscprep.network.ApiClient
import com.sololeveling.sscprep.network.AuthResponse
import com.sololeveling.sscprep.network.LoginRequest
import com.sololeveling.sscprep.network.RegisterRequest

class AuthRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("solo_auth_prefs", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return prefs.getString("auth_token", null) != null
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun getUsername(): String? {
        return prefs.getString("auth_username", null)
    }

    fun getHunterName(): String? {
        return prefs.getString("auth_hunter_name", null)
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        val response = ApiClient.apiService.login(request)
        if (response.success && response.token != null && response.user != null) {
            saveAuthInfo(response.token, response.user.id, response.user.username, response.user.hunterName)
        }
        return response
    }

    suspend fun register(request: RegisterRequest): AuthResponse {
        val response = ApiClient.apiService.register(request)
        if (response.success && response.token != null && response.user != null) {
            saveAuthInfo(response.token, response.user.id, response.user.username, response.user.hunterName)
        }
        return response
    }

    private fun saveAuthInfo(token: String, userId: String, username: String, hunterName: String?) {
        prefs.edit().apply {
            putString("auth_token", token)
            putString("auth_user_id", userId)
            putString("auth_username", username)
            putString("auth_hunter_name", hunterName)
            apply()
        }
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    suspend fun deleteAccount(): AuthResponse {
        val response = ApiClient.apiService.deleteAccount()
        if (response.success) {
            logout()
        }
        return response
    }
}
