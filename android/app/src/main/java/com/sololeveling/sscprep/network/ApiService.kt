package com.sololeveling.sscprep.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/auth/me")
    suspend fun getMe(): UserResponse

    @GET("api/player/sync")
    suspend fun getSyncData(): SyncDataResponse

    @POST("api/player/sync")
    suspend fun pushSyncData(@Body request: SyncRequest): SyncPushResponse

    @GET("api/leaderboard")
    suspend fun getLeaderboard(): LeaderboardResponse

    @GET("api/version")
    suspend fun checkAppVersion(): AppVersionResponse
}
