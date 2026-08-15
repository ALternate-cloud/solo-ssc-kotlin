package com.sololeveling.sscprep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DailyTask(
    val id: String,
    val name: String,
    val current: Int = 0,
    val target: Int,
    val unit: String,
    val expReward: Int
)

@Serializable
data class DailyQuestState(
    val lastDate: String = "",
    val claimed: Boolean = false,
    val penaltyActive: Boolean = false,
    val penaltyMistakes: Int = 0,
    val streak: Int = 1,
    val tasks: List<DailyTask> = listOf(
        DailyTask(id = "t_quant", name = "Solve 30 Quantitative Aptitude Questions", target = 30, unit = "Questions", expReward = 100),
        DailyTask(id = "t_reas", name = "Practice 25 Reasoning & Logic Questions", target = 25, unit = "Questions", expReward = 80),
        DailyTask(id = "t_eng", name = "Master 20 English Vocab / Grammar Rules", target = 20, unit = "Questions", expReward = 70),
        DailyTask(id = "t_focus", name = "Complete 60 Mins Deep Focus Pomodoro", target = 60, unit = "Minutes", expReward = 120)
    )
)
