package com.sololeveling.sscprep.domain.engine

import com.sololeveling.sscprep.domain.model.HUNTER_RANKS
import com.sololeveling.sscprep.domain.model.PlayerState
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object PlayerEngine {

    fun recalculateDerived(state: PlayerState): PlayerState {
        val maxHp = 100 + (state.stats.vitality * 10) + (state.level * 5)
        val maxMp = 50 + (state.stats.intelligence * 8) + (state.level * 4)
        val maxExp = floor(100.0 * 1.22.pow(state.level - 1.0)).toInt()

        val clampedHp = min(state.hp, maxHp)
        val clampedMp = min(state.mp, maxMp)

        val newRank = evaluateRank(state.level)

        return state.copy(
            maxHp = maxHp,
            maxMp = maxMp,
            maxExp = maxExp,
            hp = clampedHp,
            mp = clampedMp,
            rank = newRank
        )
    }

    fun evaluateRank(level: Int): String {
        var currentRank = "E"
        for (r in HUNTER_RANKS) {
            if (level >= r.minLevel) {
                currentRank = r.rank
            }
        }
        return currentRank
    }

    data class LevelUpResult(
        val newState: PlayerState,
        val didLevelUp: Boolean,
        val levelsGained: Int,
        val oldRank: String,
        val newRank: String,
        val didRankUp: Boolean
    )

    fun addExp(state: PlayerState, expGain: Int): LevelUpResult {
        var currentExp = state.exp + expGain
        var level = state.level
        var unallocatedPoints = state.unallocatedPoints
        var didLevelUp = false
        var levelsGained = 0
        val oldRank = state.rank

        var maxExp = floor(100.0 * 1.22.pow(level - 1.0)).toInt()

        while (currentExp >= maxExp) {
            currentExp -= maxExp
            level += 1
            unallocatedPoints += 3
            didLevelUp = true
            levelsGained += 1
            maxExp = floor(100.0 * 1.22.pow(level - 1.0)).toInt()
        }

        val updatedState = recalculateDerived(
            state.copy(
                level = level,
                exp = currentExp,
                maxExp = maxExp,
                unallocatedPoints = unallocatedPoints,
                hp = if (didLevelUp) 100 + (state.stats.vitality * 10) + (level * 5) else state.hp,
                mp = if (didLevelUp) 50 + (state.stats.intelligence * 8) + (level * 4) else state.mp
            )
        )

        val newRank = updatedState.rank
        val didRankUp = oldRank != newRank

        return LevelUpResult(
            newState = updatedState,
            didLevelUp = didLevelUp,
            levelsGained = levelsGained,
            oldRank = oldRank,
            newRank = newRank,
            didRankUp = didRankUp
        )
    }

    fun allocateStat(state: PlayerState, statType: String): PlayerState? {
        if (state.unallocatedPoints <= 0) return null

        val newStats = when (statType.uppercase()) {
            "INT" -> state.stats.copy(intelligence = state.stats.intelligence + 1)
            "VIT" -> state.stats.copy(vitality = state.stats.vitality + 1)
            "AGI" -> state.stats.copy(agility = state.stats.agility + 1)
            "SEN" -> state.stats.copy(sense = state.stats.sense + 1)
            "STR" -> state.stats.copy(strength = state.stats.strength + 1)
            else -> return null
        }

        return recalculateDerived(
            state.copy(
                stats = newStats,
                unallocatedPoints = state.unallocatedPoints - 1
            )
        )
    }

    fun modifyHp(state: PlayerState, delta: Int): PlayerState {
        val newHp = max(0, min(state.maxHp, state.hp + delta))
        return state.copy(hp = newHp)
    }

    fun modifyMp(state: PlayerState, delta: Int): PlayerState {
        val newMp = max(0, min(state.maxMp, state.mp + delta))
        return state.copy(mp = newMp)
    }

    fun addGold(state: PlayerState, amount: Int): PlayerState {
        return state.copy(gold = state.gold + amount)
    }

    fun spendGold(state: PlayerState, amount: Int): PlayerState? {
        if (state.gold < amount) return null
        return state.copy(gold = state.gold - amount)
    }
}
