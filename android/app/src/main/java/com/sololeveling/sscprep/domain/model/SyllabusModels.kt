package com.sololeveling.sscprep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SyllabusTopic(
    val id: String,
    val subject: String,
    val name: String,
    val weightage: String,
    val isCompleted: Boolean = false,
    val highYieldFormulas: List<String> = emptyList()
)

@Serializable
data class HunterLeaderboardEntry(
    val rankPosition: Int,
    val name: String,
    val title: String,
    val hunterRank: String,
    val level: Int,
    val postTarget: String,
    val questionsSolved: Int,
    val accuracy: Int,
    val avatar: String
)

val SAMPLE_HUNTER_LEADERBOARD = listOf(
    HunterLeaderboardEntry(
        rankPosition = 1,
        name = "Sung Jin-Woo (Shadow Monarch)",
        title = "National Level Monarch",
        hunterRank = "Monarch",
        level = 142,
        postTarget = "Income Tax Inspector (ITI)",
        questionsSolved = 4850,
        accuracy = 98,
        avatar = "👑"
    ),
    HunterLeaderboardEntry(
        rankPosition = 2,
        name = "Cha Hae-In (Sword Dancer)",
        title = "S-Rank Master",
        hunterRank = "S",
        level = 98,
        postTarget = "ASO in MEA",
        questionsSolved = 3420,
        accuracy = 95,
        avatar = "🗡️"
    ),
    HunterLeaderboardEntry(
        rankPosition = 3,
        name = "Go Gun-Hee (Chairman)",
        title = "National Sovereign",
        hunterRank = "S",
        level = 95,
        postTarget = "CAG Assistant Audit Officer (AAO)",
        questionsSolved = 3190,
        accuracy = 96,
        avatar = "⚡"
    ),
    HunterLeaderboardEntry(
        rankPosition = 4,
        name = "Baek Yoonho (White Tiger)",
        title = "A-Rank Vanguard",
        hunterRank = "A",
        level = 78,
        postTarget = "GST & Excise Inspector",
        questionsSolved = 2450,
        accuracy = 91,
        avatar = "🐯"
    ),
    HunterLeaderboardEntry(
        rankPosition = 5,
        name = "Choi Jong-In (Ultimate Soldier)",
        title = "A-Rank Flame Monarch",
        hunterRank = "A",
        level = 74,
        postTarget = "Assistant Enforcement Officer (ED)",
        questionsSolved = 2100,
        accuracy = 89,
        avatar = "🔥"
    ),
    HunterLeaderboardEntry(
        rankPosition = 6,
        name = "Woo Jin-Chul (Chief Inspector)",
        title = "B-Rank Tactician",
        hunterRank = "B",
        level = 54,
        postTarget = "Sub-Inspector in CBI",
        questionsSolved = 1680,
        accuracy = 87,
        avatar = "🛡️"
    )
)
