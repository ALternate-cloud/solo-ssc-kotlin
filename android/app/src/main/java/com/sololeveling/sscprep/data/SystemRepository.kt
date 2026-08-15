package com.sololeveling.sscprep.data

import android.content.Context
import android.content.SharedPreferences
import com.sololeveling.sscprep.domain.engine.DailyQuestEngine
import com.sololeveling.sscprep.domain.engine.PlayerEngine
import com.sololeveling.sscprep.domain.model.DailyQuestState
import com.sololeveling.sscprep.domain.model.InventoryState
import com.sololeveling.sscprep.domain.model.PlayerState
import com.sololeveling.sscprep.domain.model.Question
import com.sololeveling.sscprep.domain.model.ShadowArmyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SystemRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("solo_leveling_system_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true; isLenient = true; encodeDefaults = true }

    private val _playerState = MutableStateFlow(loadPlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _questState = MutableStateFlow(loadQuestState())
    val questState: StateFlow<DailyQuestState> = _questState.asStateFlow()

    private val _shadowState = MutableStateFlow(loadShadowState())
    val shadowState: StateFlow<ShadowArmyState> = _shadowState.asStateFlow()

    private val _inventoryState = MutableStateFlow(loadInventoryState())
    val inventoryState: StateFlow<InventoryState> = _inventoryState.asStateFlow()

    private val _bookmarkedQuestions = MutableStateFlow(loadBookmarkedQuestions())
    val bookmarkedQuestions: StateFlow<Set<String>> = _bookmarkedQuestions.asStateFlow()

    init {
        // Run daily reset check
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val updatedQuest = DailyQuestEngine.checkDailyReset(_questState.value, todayStr)
        if (updatedQuest != _questState.value) {
            updateQuestState(updatedQuest)
        }
    }

    // --- Player State ---
    private fun loadPlayerState(): PlayerState {
        val raw = prefs.getString("player_state", null)
        return if (raw != null) {
            try {
                json.decodeFromString<PlayerState>(raw)
            } catch (e: Exception) {
                PlayerEngine.recalculateDerived(PlayerState())
            }
        } else {
            PlayerEngine.recalculateDerived(PlayerState())
        }
    }

    fun updatePlayerState(newState: PlayerState) {
        val recalculated = PlayerEngine.recalculateDerived(newState)
        _playerState.value = recalculated
        prefs.edit().putString("player_state", json.encodeToString(recalculated)).apply()
    }

    // --- Quests State ---
    private fun loadQuestState(): DailyQuestState {
        val raw = prefs.getString("quest_state", null)
        return if (raw != null) {
            try {
                json.decodeFromString<DailyQuestState>(raw)
            } catch (e: Exception) {
                DailyQuestState()
            }
        } else {
            DailyQuestState()
        }
    }

    fun updateQuestState(newState: DailyQuestState) {
        _questState.value = newState
        prefs.edit().putString("quest_state", json.encodeToString(newState)).apply()
    }

    // --- Shadow Army State ---
    private fun loadShadowState(): ShadowArmyState {
        val raw = prefs.getString("shadow_state", null)
        return if (raw != null) {
            try {
                json.decodeFromString<ShadowArmyState>(raw)
            } catch (e: Exception) {
                ShadowArmyState()
            }
        } else {
            ShadowArmyState()
        }
    }

    fun updateShadowState(newState: ShadowArmyState) {
        _shadowState.value = newState
        prefs.edit().putString("shadow_state", json.encodeToString(newState)).apply()
    }

    // --- Inventory State ---
    private fun loadInventoryState(): InventoryState {
        val raw = prefs.getString("inventory_state", null)
        return if (raw != null) {
            try {
                json.decodeFromString<InventoryState>(raw)
            } catch (e: Exception) {
                InventoryState()
            }
        } else {
            InventoryState()
        }
    }

    fun updateInventoryState(newState: InventoryState) {
        _inventoryState.value = newState
        prefs.edit().putString("inventory_state", json.encodeToString(newState)).apply()
    }

    // --- Bookmarks ---
    private fun loadBookmarkedQuestions(): Set<String> {
        return prefs.getStringSet("bookmarked_ids", emptySet()) ?: emptySet()
    }

    fun toggleBookmark(questionId: String) {
        val current = _bookmarkedQuestions.value.toMutableSet()
        if (current.contains(questionId)) {
            current.remove(questionId)
        } else {
            current.add(questionId)
        }
        _bookmarkedQuestions.value = current
        prefs.edit().putStringSet("bookmarked_ids", current).apply()
    }
}
