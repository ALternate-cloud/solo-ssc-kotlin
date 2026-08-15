package com.sololeveling.sscprep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DungeonGate(
    val id: String,
    val rank: String,
    val name: String,
    val subject: String,
    val bossName: String,
    val bossAvatar: String,
    val questionCount: Int,
    val timeMinutes: Int,
    val expReward: Int,
    val goldReward: Int,
    val desc: String,
    val isInfiniteTower: Boolean = false
)

val SYSTEM_DUNGEON_GATES = listOf(
    DungeonGate(
        id = "gate_quant",
        rank = "E",
        name = "Goblin Mine: Quantitative Aptitude",
        subject = "Quantitative Aptitude",
        bossName = "Hobgoblin Leader (Algebra & Arithmetic)",
        bossAvatar = "👹",
        questionCount = 10,
        timeMinutes = 12,
        expReward = 150,
        goldReward = 60,
        desc = "Clear 10 high-frequency Quant questions to slay the Hobgoblin and conquer foundational math formulas."
    ),
    DungeonGate(
        id = "gate_reas",
        rank = "D",
        name = "Gargoyle Belfry: Logical Reasoning",
        subject = "General Intelligence & Reasoning",
        bossName = "Obsidian Gargoyle (Patterns & Syllogism)",
        bossAvatar = "🦇",
        questionCount = 10,
        timeMinutes = 10,
        expReward = 140,
        goldReward = 50,
        desc = "Decipher optical riddles, coding-decoding, and syllogism matrices to shatter the Gargoyle."
    ),
    DungeonGate(
        id = "gate_eng",
        rank = "C",
        name = "Siren Cavern: English Language",
        subject = "English Language",
        bossName = "Abyssal Siren (Grammar & Vocabulary)",
        bossAvatar = "🧜‍♀️",
        questionCount = 10,
        timeMinutes = 8,
        expReward = 130,
        goldReward = 50,
        desc = "Survive tricky cloze tests, error spotting, and archaic idioms without falling for misleading grammatical traps."
    ),
    DungeonGate(
        id = "gate_ga",
        rank = "B",
        name = "Citadel of Knowledge: General Awareness",
        subject = "General Awareness",
        bossName = "Ancient Archmage (History & Polity)",
        bossAvatar = "🧙‍♂️",
        questionCount = 10,
        timeMinutes = 7,
        expReward = 160,
        goldReward = 70,
        desc = "Test your knowledge on Constitution, Indian History, Economy, and Current Affairs."
    ),
    DungeonGate(
        id = "gate_cgl_boss",
        rank = "S",
        name = "RED GATE: SSC CGL Tier-1 Mega Raid",
        subject = "Full Mock Exam",
        bossName = "Kargalgan the Demon King (Grand Examiner)",
        bossAvatar = "👿",
        questionCount = 25,
        timeMinutes = 30,
        expReward = 600,
        goldReward = 300,
        desc = "The ultimate boss battle! Full-length comprehensive simulation across all 4 subjects with live Boss HP vs Player HP."
    ),
    DungeonGate(
        id = "gate_demon_castle",
        rank = "Monarch",
        name = "DEMON CASTLE: Infinite Tower of Trials",
        subject = "Procedural Infinite Gauntlet",
        bossName = "Baran the Demon Monarch (Infinite Ruler)",
        bossAvatar = "👑",
        questionCount = 20,
        timeMinutes = 25,
        expReward = 850,
        goldReward = 450,
        desc = "Procedurally generated infinite questions scaled to your level! Conquer endless dynamic mathematics and reasoning trials.",
        isInfiniteTower = true
    )
)

data class RaidSession(
    val gate: DungeonGate,
    val questions: List<Question>,
    val currentIndex: Int = 0,
    val answers: MutableList<Int?> = MutableList(questions.size) { null },
    val flags: MutableList<Boolean> = MutableList(questions.size) { false },
    val timeRemainingSeconds: Int = gate.timeMinutes * 60,
    val totalTimeSeconds: Int = gate.timeMinutes * 60,
    val bossMaxHp: Int = questions.size * 100,
    val bossHp: Int = questions.size * 100,
    val playerMaxHp: Int = 205,
    val playerHp: Int = 205,
    val isCompleted: Boolean = false
)

@Serializable
data class RaidResult(
    val gateId: String,
    val gateName: String,
    val bossName: String,
    val isBossDefeated: Boolean,
    val totalQuestions: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val unattemptedCount: Int,
    val rawScore: Double,
    val maxScore: Double,
    val accuracy: Int,
    val expEarned: Int,
    val goldEarned: Int,
    val timeTakenSeconds: Int
)
