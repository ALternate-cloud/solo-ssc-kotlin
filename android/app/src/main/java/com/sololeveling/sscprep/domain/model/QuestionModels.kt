package com.sololeveling.sscprep.domain.model

import kotlinx.serialization.Serializable

enum class QuestionSubject(val displayName: String, val icon: String) {
    QUANT("Quantitative Aptitude", "📐"),
    REASONING("General Intelligence & Reasoning", "🧩"),
    ENGLISH("English Language", "📖"),
    GENERAL_AWARENESS("General Awareness", "🌍"),
    FULL_MOCK("Full Mock Exam", "⚔️")
}

enum class QuestionDifficulty(val displayName: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    MONARCH("Monarch Level")
}

@Serializable
data class Question(
    val id: String,
    val subject: String,
    val topic: String,
    val difficulty: String = "Medium",
    val examTag: String? = null,
    val question: String,
    val options: List<String>,
    val correct: Int,
    val explanation: String,
    val trick: String? = null,
    val isBookmarked: Boolean = false
)

@Serializable
data class PyqPaper(
    val id: String,
    val exam: String,
    val year: String,
    val shift: String,
    val difficulty: String,
    val totalQuestions: Int,
    val durationMinutes: Int,
    val maxMarks: Int,
    val bossName: String,
    val bossAvatar: String,
    val desc: String,
    val questions: List<Question>
)
