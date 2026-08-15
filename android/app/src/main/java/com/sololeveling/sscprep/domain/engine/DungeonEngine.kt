package com.sololeveling.sscprep.domain.engine

import com.sololeveling.sscprep.domain.model.DungeonGate
import com.sololeveling.sscprep.domain.model.FallenMonster
import com.sololeveling.sscprep.domain.model.PlayerState
import com.sololeveling.sscprep.domain.model.Question
import com.sololeveling.sscprep.domain.model.RaidResult
import com.sololeveling.sscprep.domain.model.RaidSession
import kotlin.math.ceil
import kotlin.math.max

object DungeonEngine {

    fun createSession(
        gate: DungeonGate,
        questions: List<Question>,
        playerState: PlayerState
    ): RaidSession {
        return RaidSession(
            gate = gate,
            questions = questions,
            currentIndex = 0,
            answers = MutableList(questions.size) { null },
            flags = MutableList(questions.size) { false },
            timeRemainingSeconds = gate.timeMinutes * 60,
            totalTimeSeconds = gate.timeMinutes * 60,
            bossMaxHp = questions.size * 100,
            bossHp = questions.size * 100,
            playerMaxHp = playerState.maxHp,
            playerHp = playerState.hp,
            isCompleted = false
        )
    }

    data class SessionSubmission(
        val raidResult: RaidResult,
        val fallenMonsters: List<FallenMonster>,
        val updatedPlayerState: PlayerState,
        val completedSession: RaidSession
    )

    fun submitSession(
        session: RaidSession,
        playerState: PlayerState,
        currentTimeFormatted: String
    ): SessionSubmission {
        var correct = 0
        var wrong = 0
        var unattempted = 0

        val damagePerCorrect = 100
        val damageToPlayer = 20

        var currentBossHp = session.bossMaxHp
        var currentPlayerHp = session.playerHp

        val newFallenMonsters = mutableListOf<FallenMonster>()

        session.questions.forEachIndexed { index, question ->
            val userAns = session.answers.getOrNull(index)
            if (userAns == null) {
                unattempted++
            } else if (userAns == question.correct) {
                correct++
                currentBossHp = max(0, currentBossHp - damagePerCorrect)
            } else {
                wrong++
                currentPlayerHp = max(0, currentPlayerHp - damageToPlayer)
                newFallenMonsters.add(
                    FallenMonster(
                        id = "fallen_${System.currentTimeMillis()}_${index}",
                        question = question,
                        wrongAnswerIndex = userAns,
                        timestamp = currentTimeFormatted,
                        resolved = false
                    )
                )
            }
        }

        // SSC CGL Marking Scheme: +2 for correct, -0.50 for incorrect
        val rawScore = (correct * 2.0) - (wrong * 0.50)
        val maxScore = session.questions.size * 2.0
        val accuracy = if (correct + wrong > 0) ((correct.toDouble() / (correct + wrong)) * 100).toInt() else 0
        val isBossDefeated = currentBossHp <= 0 || (correct >= ceil(session.questions.size * 0.6).toInt())

        val expEarned = if (isBossDefeated) session.gate.expReward else (session.gate.expReward * 0.4).toInt()
        val goldEarned = if (isBossDefeated) session.gate.goldReward else 0

        val result = RaidResult(
            gateId = session.gate.id,
            gateName = session.gate.name,
            bossName = session.gate.bossName,
            isBossDefeated = isBossDefeated,
            totalQuestions = session.questions.size,
            correctCount = correct,
            wrongCount = wrong,
            unattemptedCount = unattempted,
            rawScore = max(0.0, rawScore),
            maxScore = maxScore,
            accuracy = accuracy,
            expEarned = expEarned,
            goldEarned = goldEarned,
            timeTakenSeconds = session.totalTimeSeconds - session.timeRemainingSeconds
        )

        // Award EXP and update player milestones
        val levelUpResult = PlayerEngine.addExp(playerState, expEarned)
        var updatedPlayer = levelUpResult.newState
        if (goldEarned > 0) {
            updatedPlayer = PlayerEngine.addGold(updatedPlayer, goldEarned)
        }
        val updatedMilestones = updatedPlayer.milestones.copy(
            totalQuestionsSolved = updatedPlayer.milestones.totalQuestionsSolved + (correct + wrong),
            mockTestsCleared = updatedPlayer.milestones.mockTestsCleared + (if (isBossDefeated) 1 else 0)
        )
        updatedPlayer = updatedPlayer.copy(
            milestones = updatedMilestones,
            hp = currentPlayerHp
        )

        val finalizedSession = session.copy(
            bossHp = currentBossHp,
            playerHp = currentPlayerHp,
            isCompleted = true
        )

        return SessionSubmission(
            raidResult = result,
            fallenMonsters = newFallenMonsters,
            updatedPlayerState = updatedPlayer,
            completedSession = finalizedSession
        )
    }
}
