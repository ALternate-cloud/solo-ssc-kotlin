package com.sololeveling.sscprep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ShadowCommander(
    val id: String,
    val name: String,
    val subject: String,
    val rank: String,
    val avatar: String,
    val level: Int = 1,
    val exp: Int = 0,
    val maxExp: Int = 100,
    val extractedCount: Int = 0,
    val buff: String
)

val DEFAULT_SHADOW_COMMANDERS = listOf(
    ShadowCommander(
        id = "lieutenant_igris",
        name = "Shadow Igris (Knight Commander)",
        subject = "Quantitative Aptitude",
        rank = "Elite Knight",
        avatar = "🗡️",
        buff = "+5% Speed Calculation Boost on Mathematics Dungeons"
    ),
    ShadowCommander(
        id = "lieutenant_beru",
        name = "Shadow Beru (King of Speed)",
        subject = "General Intelligence & Reasoning",
        rank = "General Rank",
        avatar = "🐜",
        buff = "Negative Marking Shield (Mitigates 1 wrong penalty in Boss Mocks)"
    ),
    ShadowCommander(
        id = "lieutenant_iron",
        name = "Shadow Iron (Heavy Shield)",
        subject = "English Language",
        rank = "Knight Rank",
        avatar = "🛡️",
        buff = "+10% EXP Bonus on English Vocabulary & Grammar Raids"
    ),
    ShadowCommander(
        id = "lieutenant_tusk",
        name = "Shadow Tusk (High Shaman)",
        subject = "General Awareness",
        rank = "Elite Shaman",
        avatar = "🔥",
        buff = "Insight Vision: Unlocks quick formula shortcuts & hints"
    )
)

@Serializable
data class FallenMonster(
    val id: String,
    val question: Question,
    val wrongAnswerIndex: Int?,
    val timestamp: String,
    val resolved: Boolean = false
)

@Serializable
data class ShadowArmyState(
    val totalShadows: Int = 0,
    val monarchAuraLevel: Int = 1,
    val commanders: List<ShadowCommander> = DEFAULT_SHADOW_COMMANDERS,
    val fallenMonsters: List<FallenMonster> = emptyList()
)
