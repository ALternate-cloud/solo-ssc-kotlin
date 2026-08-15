package com.sololeveling.sscprep.domain.engine

import com.sololeveling.sscprep.domain.model.DailyQuestState
import com.sololeveling.sscprep.domain.model.PlayerState
import java.util.Calendar
import java.util.Date
import kotlin.math.min

object DailyQuestEngine {

    fun checkDailyReset(state: DailyQuestState, todayDateString: String): DailyQuestState {
        if (state.lastDate == todayDateString) return state

        // Check if yesterday's quest was completed
        val wasAllCompleted = isAllCompleted(state)
        var penaltyActive = state.penaltyActive
        var streak = state.streak

        if (!wasAllCompleted && !state.claimed && state.lastDate.isNotEmpty()) {
            // Trigger Penalty Quest!
            penaltyActive = true
            streak = 1
        } else if (wasAllCompleted) {
            streak += 1
        }

        val resetTasks = state.tasks.map { it.copy(current = 0) }

        return state.copy(
            lastDate = todayDateString,
            claimed = false,
            penaltyActive = penaltyActive,
            penaltyMistakes = 0,
            streak = streak,
            tasks = resetTasks
        )
    }

    fun isAllCompleted(state: DailyQuestState): Boolean {
        return state.tasks.all { it.current >= it.target }
    }

    fun getOverallProgressPercentage(state: DailyQuestState): Int {
        var totalTarget = 0
        var totalDone = 0
        state.tasks.forEach {
            totalTarget += it.target
            totalDone += min(it.target, it.current)
        }
        return if (totalTarget > 0) ((totalDone.toDouble() / totalTarget) * 100).toInt() else 0
    }

    fun incrementTask(state: DailyQuestState, taskId: String, amount: Int = 1): DailyQuestState {
        val updatedTasks = state.tasks.map {
            if (it.id == taskId) {
                it.copy(current = min(it.target, it.current + amount))
            } else {
                it
            }
        }
        return state.copy(tasks = updatedTasks)
    }

    fun toggleTaskDirect(state: DailyQuestState, taskId: String): DailyQuestState {
        val updatedTasks = state.tasks.map {
            if (it.id == taskId) {
                val newCurrent = if (it.current >= it.target) 0 else it.target
                it.copy(current = newCurrent)
            } else {
                it
            }
        }
        return state.copy(tasks = updatedTasks)
    }

    data class ClaimResult(
        val updatedQuestState: DailyQuestState,
        val updatedPlayerState: PlayerState,
        val expGained: Int,
        val goldGained: Int,
        val statPointsGained: Int,
        val success: Boolean
    )

    fun claimDailyReward(questState: DailyQuestState, playerState: PlayerState): ClaimResult? {
        if (!isAllCompleted(questState) || questState.claimed) return null

        val newQuestState = questState.copy(claimed = true)

        val levelUpResult = PlayerEngine.addExp(playerState, 250)
        var updatedPlayer = PlayerEngine.addGold(levelUpResult.newState, 120)
        updatedPlayer = updatedPlayer.copy(
            unallocatedPoints = updatedPlayer.unallocatedPoints + 3
        )

        return ClaimResult(
            updatedQuestState = newQuestState,
            updatedPlayerState = updatedPlayer,
            expGained = 250,
            goldGained = 120,
            statPointsGained = 3,
            success = true
        )
    }

    fun getTimeUntilMidnight(): String {
        val now = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val diffMs = midnight.timeInMillis - now.timeInMillis
        val hours = (diffMs / (1000 * 60 * 60)) % 24
        val minutes = (diffMs / (1000 * 60)) % 60
        val seconds = (diffMs / 1000) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
