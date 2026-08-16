package com.sololeveling.sscprep.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.sololeveling.sscprep.domain.model.DailyQuestState
import com.sololeveling.sscprep.domain.model.PlayerState
import com.sololeveling.sscprep.domain.model.ShadowArmyState
import com.sololeveling.sscprep.network.ApiClient
import com.sololeveling.sscprep.network.SyncRequest
import com.sololeveling.sscprep.network.toDto
import com.sololeveling.sscprep.network.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class SyncStatus {
    IDLE,
    SYNCING,
    SYNCED,
    OFFLINE,
    ERROR
}

class SyncManager(private val context: Context) {

    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val TAG = "SyncManager"

    /**
     * Check if device has internet connectivity
     */
    fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Push current local state to the server cloud.
     * Called automatically after important state changes.
     */
    suspend fun pushToCloud(
        playerState: PlayerState,
        questState: DailyQuestState,
        shadowState: ShadowArmyState
    ): Boolean {
        if (!isOnline()) {
            _syncStatus.value = SyncStatus.OFFLINE
            return false
        }

        return try {
            _syncStatus.value = SyncStatus.SYNCING

            val request = SyncRequest(
                player = playerState.toDto(),
                quests = questState,
                shadows = shadowState
            )

            val response = ApiClient.apiService.pushSyncData(request)

            if (response.success) {
                _syncStatus.value = SyncStatus.SYNCED
                Log.d(TAG, "Cloud sync push successful")
                true
            } else {
                _syncStatus.value = SyncStatus.ERROR
                Log.e(TAG, "Cloud sync push failed: ${response.message}")
                false
            }
        } catch (e: Exception) {
            _syncStatus.value = SyncStatus.ERROR
            Log.e(TAG, "Cloud sync push error: ${e.message}")
            false
        }
    }

    /**
     * Pull latest state from the server cloud.
     * Called on app start after login.
     * Returns the server state, or null if pull fails.
     */
    data class CloudState(
        val playerState: PlayerState,
        val questState: DailyQuestState,
        val shadowState: ShadowArmyState
    )

    suspend fun pullFromCloud(): CloudState? {
        if (!isOnline()) {
            _syncStatus.value = SyncStatus.OFFLINE
            return null
        }

        return try {
            _syncStatus.value = SyncStatus.SYNCING

            val response = ApiClient.apiService.getSyncData()

            if (response.success && response.data != null) {
                val cloudState = CloudState(
                    playerState = response.data.player.toDomain(),
                    questState = response.data.quests,
                    shadowState = response.data.shadows
                )
                _syncStatus.value = SyncStatus.SYNCED
                Log.d(TAG, "Cloud sync pull successful - Level ${cloudState.playerState.level}")
                cloudState
            } else {
                _syncStatus.value = SyncStatus.ERROR
                Log.e(TAG, "Cloud sync pull failed")
                null
            }
        } catch (e: Exception) {
            _syncStatus.value = SyncStatus.ERROR
            Log.e(TAG, "Cloud sync pull error: ${e.message}")
            null
        }
    }
}
