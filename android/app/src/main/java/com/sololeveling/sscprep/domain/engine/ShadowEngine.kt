package com.sololeveling.sscprep.domain.engine

import com.sololeveling.sscprep.domain.model.PlayerState
import com.sololeveling.sscprep.domain.model.ShadowArmyState
import kotlin.math.floor

object ShadowEngine {

    data class ExtractionResult(
        val updatedShadowState: ShadowArmyState,
        val updatedPlayerState: PlayerState,
        val commanderName: String,
        val success: Boolean
    )

    fun extractShadow(
        shadowState: ShadowArmyState,
        playerState: PlayerState,
        fallenMonsterId: String
    ): ExtractionResult? {
        val fallenList = shadowState.fallenMonsters.toMutableList()
        val index = fallenList.indexOfFirst { it.id == fallenMonsterId }
        if (index == -1) return null

        val targetMonster = fallenList[index]
        if (targetMonster.resolved) return null

        fallenList[index] = targetMonster.copy(resolved = true)

        val subject = targetMonster.question.subject
        val commanders = shadowState.commanders.toMutableList()
        var commanderIdx = commanders.indexOfFirst { it.subject == subject }
        if (commanderIdx == -1) commanderIdx = 0

        val currentCommander = commanders[commanderIdx]
        var commExp = currentCommander.exp + 40
        var commLvl = currentCommander.level
        var commMaxExp = currentCommander.maxExp

        if (commExp >= commMaxExp) {
            commExp -= commMaxExp
            commLvl += 1
            commMaxExp = floor(commMaxExp * 1.3).toInt()
        }

        commanders[commanderIdx] = currentCommander.copy(
            level = commLvl,
            exp = commExp,
            maxExp = commMaxExp,
            extractedCount = currentCommander.extractedCount + 1
        )

        val newShadowState = shadowState.copy(
            totalShadows = shadowState.totalShadows + 1,
            commanders = commanders,
            fallenMonsters = fallenList
        )

        // Award Player EXP and Gold
        val playerLvlUp = PlayerEngine.addExp(playerState, 80)
        var updatedPlayer = PlayerEngine.addGold(playerLvlUp.newState, 30)
        val updatedMilestones = updatedPlayer.milestones.copy(
            shadowsExtracted = updatedPlayer.milestones.shadowsExtracted + 1
        )
        updatedPlayer = updatedPlayer.copy(milestones = updatedMilestones)

        return ExtractionResult(
            updatedShadowState = newShadowState,
            updatedPlayerState = updatedPlayer,
            commanderName = currentCommander.name,
            success = true
        )
    }
}
