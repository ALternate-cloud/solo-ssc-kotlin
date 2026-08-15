package com.sololeveling.sscprep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HunterRank(
    val rank: String,
    val title: String,
    val minLevel: Int,
    val colorHex: String
)

val HUNTER_RANKS = listOf(
    HunterRank(rank = "E", title = "Aspirant (Weakest of All Mankind)", minLevel = 1, colorHex = "#94A3B8"),
    HunterRank(rank = "D", title = "Apprentice Hunter", minLevel = 10, colorHex = "#4ADE80"),
    HunterRank(rank = "C", title = "Skilled Aspirant", minLevel = 25, colorHex = "#38BDF8"),
    HunterRank(rank = "B", title = "Elite Tactician", minLevel = 45, colorHex = "#C084FC"),
    HunterRank(rank = "A", title = "Master Strategist", minLevel = 70, colorHex = "#FB923C"),
    HunterRank(rank = "S", title = "National Level Aspirant", minLevel = 100, colorHex = "#FDE047"),
    HunterRank(rank = "Monarch", title = "Shadow Monarch (Supreme Rank)", minLevel = 150, colorHex = "#C084FC")
)

@Serializable
data class TargetPost(
    val id: String,
    val name: String,
    val ministry: String,
    val rankBadge: String,
    val icon: String,
    val cutoffTarget: String
)

val SSC_TARGET_POSTS = listOf(
    TargetPost(
        id = "iti",
        name = "Income Tax Inspector (ITI)",
        ministry = "CBDT, Dept of Revenue",
        rankBadge = "S-Rank Financial Monarch",
        icon = "💰",
        cutoffTarget = "335 / 390"
    ),
    TargetPost(
        id = "aso_mea",
        name = "ASO in Ministry of External Affairs (MEA)",
        ministry = "Ministry of External Affairs",
        rankBadge = "Shadow Diplomat",
        icon = "🌐",
        cutoffTarget = "340 / 390"
    ),
    TargetPost(
        id = "gst_inspector",
        name = "GST & Central Excise Inspector",
        ministry = "CBIC, Dept of Revenue",
        rankBadge = "Shadow Enforcer",
        icon = "⚡",
        cutoffTarget = "325 / 390"
    ),
    TargetPost(
        id = "cbi_si",
        name = "Sub-Inspector in CBI",
        ministry = "Central Bureau of Investigation",
        rankBadge = "Shadow Investigator",
        icon = "🔍",
        cutoffTarget = "330 / 390"
    ),
    TargetPost(
        id = "ed_aeo",
        name = "Assistant Enforcement Officer (ED)",
        ministry = "Directorate of Enforcement",
        rankBadge = "Financial Inquisitor",
        icon = "⚖️",
        cutoffTarget = "338 / 390"
    ),
    TargetPost(
        id = "cag_aao",
        name = "Assistant Audit Officer (AAO)",
        ministry = "CAG (Gazetted Group B)",
        rankBadge = "Treasury Monarch",
        icon = "👑",
        cutoffTarget = "345 / 390"
    ),
    TargetPost(
        id = "preventive_officer",
        name = "Customs Preventive Officer (PO)",
        ministry = "CBIC (Airports & Seaports)",
        rankBadge = "Coastal Vanguard",
        icon = "⚓",
        cutoffTarget = "328 / 390"
    ),
    TargetPost(
        id = "divisional_accountant",
        name = "Divisional Accountant (DA)",
        ministry = "CAG State Divisions",
        rankBadge = "Division Commander",
        icon = "📊",
        cutoffTarget = "320 / 390"
    )
)

@Serializable
data class HunterStats(
    val intelligence: Int = 10, // Concept Mastery & Reasoning (INT)
    val vitality: Int = 10,     // Study Endurance & Focus minutes (VIT)
    val agility: Int = 10,      // Solving Speed (AGI)
    val sense: Int = 10,        // Precision & Negative Mark Avoidance (SEN)
    val strength: Int = 10      // Discipline & Habit Consistency (STR)
)

@Serializable
data class HunterMilestones(
    val totalQuestionsSolved: Int = 0,
    val mockTestsCleared: Int = 0,
    val shadowsExtracted: Int = 0,
    val streakDays: Int = 1,
    val focusMinutes: Int = 0
)

@Serializable
data class PlayerState(
    val name: String = "Sung Jin-Aspirant",
    val title: String = "The One Who Overcomes Formulas",
    val jobClass: String = "Exam Slayer / Player",
    val targetPostId: String = "iti",
    val level: Int = 1,
    val exp: Int = 0,
    val maxExp: Int = 100,
    val rank: String = "E",
    val hp: Int = 205,
    val maxHp: Int = 205,
    val mp: Int = 134,
    val maxMp: Int = 134,
    val gold: Int = 150,
    val unallocatedPoints: Int = 5,
    val stats: HunterStats = HunterStats(),
    val milestones: HunterMilestones = HunterMilestones()
)
