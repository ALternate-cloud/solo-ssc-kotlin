package com.sololeveling.sscprep.network

import com.sololeveling.sscprep.domain.model.DailyQuestState
import com.sololeveling.sscprep.domain.model.HunterMilestones
import com.sololeveling.sscprep.domain.model.HunterStats
import com.sololeveling.sscprep.domain.model.PlayerState
import com.sololeveling.sscprep.domain.model.ShadowArmyState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: UserDto? = null
)

@Serializable
data class AppVersionResponse(
    val success: Boolean = true,
    val latestVersionCode: Int = 1,
    val latestVersionName: String = "1.0.0",
    val downloadUrl: String = "",
    val changelog: String = ""
)

@Serializable
data class UserResponse(
    val success: Boolean,
    val user: UserDto? = null
)

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val hunterName: String? = null
)

@Serializable
data class SyncDataResponse(
    val success: Boolean,
    val data: SyncDataDto? = null
)

@Serializable
data class SyncDataDto(
    val player: PlayerDto,
    val quests: DailyQuestState,
    val shadows: ShadowArmyState
)

@Serializable
data class SyncRequest(
    val player: PlayerDto,
    val quests: DailyQuestState,
    val shadows: ShadowArmyState
)

@Serializable
data class SyncPushResponse(
    val success: Boolean,
    val message: String
)

@Serializable
data class LeaderboardResponse(
    val success: Boolean,
    val leaderboard: List<LeaderboardEntryDto> = emptyList()
)

@Serializable
data class LeaderboardEntryDto(
    val rankPosition: Int,
    val userId: String,
    val hunterName: String,
    val level: Int,
    val rank: String,
    val title: String,
    val gold: Int,
    val totalQuestionsSolved: Int,
    val mockTestsCleared: Int
)

@Serializable
data class PlayerDto(
    val name: String,
    val level: Int,
    val exp: Int,
    val maxExp: Int,
    val rank: String,
    val hp: Int,
    val maxHp: Int,
    val mp: Int,
    val maxMp: Int,
    val gold: Int,
    val unallocatedPoints: Int,
    val title: String,
    val stats: StatsDto,
    @SerialName("statsUnlocked") val milestones: MilestonesDto
)

@Serializable
data class StatsDto(
    @SerialName("int") val int: Int,
    @SerialName("vit") val vit: Int,
    @SerialName("agi") val agi: Int,
    @SerialName("sen") val sen: Int,
    @SerialName("str") val str: Int
)

@Serializable
data class MilestonesDto(
    val totalQuestionsSolved: Int,
    val mockTestsCleared: Int,
    val shadowsExtracted: Int,
    val streakDays: Int,
    val focusMinutes: Int
)

fun PlayerDto.toDomain(): PlayerState {
    return PlayerState(
        name = name,
        level = level,
        exp = exp,
        maxExp = maxExp,
        rank = rank,
        hp = hp,
        maxHp = maxHp,
        mp = mp,
        maxMp = maxMp,
        gold = gold,
        unallocatedPoints = unallocatedPoints,
        title = title,
        stats = HunterStats(
            intelligence = stats.int,
            vitality = stats.vit,
            agility = stats.agi,
            sense = stats.sen,
            strength = stats.str
        ),
        milestones = HunterMilestones(
            totalQuestionsSolved = milestones.totalQuestionsSolved,
            mockTestsCleared = milestones.mockTestsCleared,
            shadowsExtracted = milestones.shadowsExtracted,
            streakDays = milestones.streakDays,
            focusMinutes = milestones.focusMinutes
        )
    )
}

fun PlayerState.toDto(): PlayerDto {
    return PlayerDto(
        name = name,
        level = level,
        exp = exp,
        maxExp = maxExp,
        rank = rank,
        hp = hp,
        maxHp = maxHp,
        mp = mp,
        maxMp = maxMp,
        gold = gold,
        unallocatedPoints = unallocatedPoints,
        title = title,
        stats = StatsDto(
            int = stats.intelligence,
            vit = stats.vitality,
            agi = stats.agility,
            sen = stats.sense,
            str = stats.strength
        ),
        milestones = MilestonesDto(
            totalQuestionsSolved = milestones.totalQuestionsSolved,
            mockTestsCleared = milestones.mockTestsCleared,
            shadowsExtracted = milestones.shadowsExtracted,
            streakDays = milestones.streakDays,
            focusMinutes = milestones.focusMinutes
        )
    )
}

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val hunterName: String
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)
