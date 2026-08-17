package com.sololeveling.sscprep.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.sololeveling.sscprep.domain.engine.InfiniteQuestionGenerator
import com.sololeveling.sscprep.domain.model.Question
import com.sololeveling.sscprep.ui.components.RankBadgeChip
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel
import kotlin.random.Random

data class DuelOpponent(
    val name: String,
    val rank: String,
    val elo: Int,
    val avatar: String,
    val title: String
)

val MONARCH_RIVAL_DUELISTS = listOf(
    DuelOpponent("Sung Jin-Woo (Shadow Monarch)", "Monarch", 2450, "👑", "The Sovereign of Shadows"),
    DuelOpponent("Thomas Andre (Goliath)", "S", 2180, "🦁", "National Level Hunter"),
    DuelOpponent("Liu Zhigang (Blade Master)", "S", 1950, "⚔️", "Dragon Slayer of Quant"),
    DuelOpponent("Cha Hae-In (Sword Dancer)", "S", 1820, "🗡️", "The Radiant Hunter"),
    DuelOpponent("Choi Jong-In (Ultimate Mage)", "A", 1640, "🔥", "Master of Reasoning"),
    DuelOpponent("Baek Yoon-Ho (White Tiger)", "A", 1520, "🐯", "Beast Calculation Master"),
    DuelOpponent("Woo Jin-Chul (Chief Inspector)", "A", 1380, "🛡️", "Discipline Enforcer"),
    DuelOpponent("Yoo Jin-Ho (Vice Master)", "C", 1120, "💰", "Rich Boy Aspirant"),
    DuelOpponent("Kang Tae-Shik (Shadow Assassin)", "B", 1250, "⚡", "Speed Drill Assassin")
)

@Composable
fun HunterDuelScreen(
    viewModel: MainViewModel,
    onExit: () -> Unit
) {
    val playerState by viewModel.playerState.collectAsState()
    val arena = playerState.arenaProfile

    var duelState by remember { mutableStateOf("lobby") } // lobby, matchmaking, battling, result
    var opponent by remember { mutableStateOf(MONARCH_RIVAL_DUELISTS[0]) }

    var currentRound by remember { mutableStateOf(1) }
    val totalRounds = 5
    var currentQuestion by remember { mutableStateOf<Question?>(null) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    var playerHp by remember { mutableStateOf(100) }
    var opponentHp by remember { mutableStateOf(100) }
    var roundTimer by remember { mutableStateOf(15) }
    var isTimerRunning by remember { mutableStateOf(false) }

    var playerRoundScore by remember { mutableStateOf(0) }
    var opponentRoundScore by remember { mutableStateOf(0) }
    var isVictory by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    fun advanceRound() {
        if (currentRound >= totalRounds || playerHp <= 0 || opponentHp <= 0) {
            // Duel Finished!
            isTimerRunning = false
            isVictory = playerRoundScore >= opponentRoundScore && playerHp > 0
            duelState = "result"
            if (isVictory) {
                viewModel.soundAndHaptics.playLevelUp()
                viewModel.recordArenaMatch(won = true)
            } else {
                viewModel.soundAndHaptics.playBossRoar()
                viewModel.recordArenaMatch(won = false)
            }
        } else {
            currentRound++
            currentQuestion = when (currentRound % 3) {
                0 -> InfiniteQuestionGenerator.generateQuantQuestion("Hard")
                1 -> InfiniteQuestionGenerator.generateQuantQuestion("Medium")
                else -> InfiniteQuestionGenerator.generateQuantQuestion("Easy")
            }
            selectedOption = null
            isAnswerSubmitted = false
            isTimerRunning = true
        }
    }

    fun startMatchmaking() {
        duelState = "matchmaking"
        viewModel.soundAndHaptics.playClick()
        coroutineScope.launch {
            delay(2000) // Radar scan delay
            val validOpponents = MONARCH_RIVAL_DUELISTS.filter { kotlin.math.abs(it.elo - arena.eloRating) <= 600 }
            opponent = if (validOpponents.isNotEmpty()) validOpponents.random() else MONARCH_RIVAL_DUELISTS.random()
            playerHp = 100
            opponentHp = 100
            currentRound = 1
            playerRoundScore = 0
            opponentRoundScore = 0
            currentQuestion = InfiniteQuestionGenerator.generateQuantQuestion()
            selectedOption = null
            isAnswerSubmitted = false
            duelState = "battling"
            isTimerRunning = true
            viewModel.soundAndHaptics.playAriseSound()
        }
    }

    fun submitAnswer(optionIndex: Int) {
        if (isAnswerSubmitted || currentQuestion == null) return
        selectedOption = optionIndex
        isAnswerSubmitted = true
        isTimerRunning = false

        val isCorrect = optionIndex == currentQuestion!!.correct
        if (isCorrect) {
            viewModel.soundAndHaptics.playClick()
            playerRoundScore++
            opponentHp = maxOf(0, opponentHp - 25)
        } else {
            viewModel.soundAndHaptics.playBossRoar()
            playerHp = maxOf(0, playerHp - 25)
        }

        // Simulate opponent answer
        val oppCorrect = Random.nextFloat() < 0.65f
        if (oppCorrect && !isCorrect) {
            opponentRoundScore++
        }

        coroutineScope.launch {
            delay(1500)
            advanceRound()
        }
    }

    // 15-second round countdown
    LaunchedEffect(duelState, currentRound, isTimerRunning) {
        if (duelState == "battling" && isTimerRunning) {
            roundTimer = 15
            while (roundTimer > 0 && !isAnswerSubmitted) {
                delay(1000)
                roundTimer--
            }
            if (roundTimer == 0 && !isAnswerSubmitted) {
                // Time up! Player took damage
                isAnswerSubmitted = true
                viewModel.soundAndHaptics.playBossRoar()
                playerHp = maxOf(0, playerHp - 25)
                delay(1500)
                advanceRound()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onExit) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
                Text(
                    text = "HUNTER DUEL ARENA ⚔️",
                    style = MaterialTheme.typography.titleMedium,
                    color = SystemCrimson,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SystemSurfaceElevated,
                    border = BorderStroke(1.dp, SystemGold)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚡", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${arena.eloRating} ELO", color = SystemGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (duelState) {
                "lobby" -> {
                    // Arena Lobby
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            SystemWindowCard(borderColor = SystemCrimson, glowEffect = true) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                    Text("⚔️", fontSize = 48.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("1v1 SPEED DUEL GAUNTLET", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Black)
                                    Text("Clash in a 5-round rapid-fire speed quiz against ranked hunters.", color = TextSecondary, fontSize = 13.sp, textAlign = TextAlign.Center)
                                    Spacer(modifier = Modifier.height(16.dp))

                                    SoloGlowingButton(
                                        text = "FIND RIVAL HUNTER ⚡",
                                        onClick = { startMatchmaking() },
                                        containerColor = SystemCrimson,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        // Arena Profile Card
                        item {
                            SystemWindowCard(borderColor = SystemBorder) {
                                Text("YOUR DUEL STATS", color = SystemPrimary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column {
                                        Text("Division Tier", color = TextSecondary, fontSize = 12.sp)
                                        Text(arena.arenaTier, color = SystemGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                    Column {
                                        Text("Win / Loss", color = TextSecondary, fontSize = 12.sp)
                                        Text("${arena.wins}W / ${arena.losses}L", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                    Column {
                                        Text("Win Streak", color = TextSecondary, fontSize = 12.sp)
                                        Text("🔥 ${arena.winStreak}", color = SystemCrimson, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                }
                            }
                        }

                        // Ranked Rivals Roster
                        item {
                            SystemWindowCard(borderColor = SystemBorder) {
                                Text("ARENA RIVALS QUEUE", color = TextSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                MONARCH_RIVAL_DUELISTS.take(4).forEach { opp ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(opp.avatar, fontSize = 20.sp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(opp.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                Text(opp.title, color = TextSecondary, fontSize = 11.sp)
                                            }
                                        }
                                        Text("${opp.elo} ELO", color = SystemGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    HorizontalDivider(color = SystemBorder.copy(alpha = 0.3f))
                                }
                            }
                        }
                    }
                }

                "matchmaking" -> {
                    // Radar Animation
                    val infiniteTransition = rememberInfiniteTransition(label = "radar")
                    val angle by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(tween(1500, easing = LinearEasing)),
                        label = "angle"
                    )

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .border(2.dp, SystemCrimson, CircleShape)
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📡", fontSize = 48.sp)
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "SEARCHING FOR RIVAL HUNTER...",
                                color = SystemCrimson,
                                fontWeight = FontWeight.Black,
                                style = MaterialTheme.typography.titleMedium,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Matching ELO Rating ~${arena.eloRating}", color = TextSecondary, fontSize = 13.sp)
                        }
                    }
                }

                "battling" -> {
                    // Live Battle Screen
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Battle Versus Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Player
                            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)) {
                                Text(playerState.name, color = SystemPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                LinearProgressIndicator(
                                    progress = { playerHp / 100f },
                                    color = if (playerHp > 30) HpRed else SystemCrimson,
                                    trackColor = SystemSurfaceElevated,
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                                )
                                Text("HP: $playerHp/100", color = TextSecondary, fontSize = 10.sp)
                            }

                            // Round & Timer Center
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Text("ROUND $currentRound/$totalRounds", color = SystemGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Surface(
                                    shape = CircleShape,
                                    color = if (roundTimer <= 5) SystemCrimson else SystemSurfaceElevated,
                                    border = BorderStroke(1.dp, if (roundTimer <= 5) SystemCrimson else SystemPrimary),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("$roundTimer", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    }
                                }
                            }

                            // Opponent
                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                                Text(opponent.name.take(15) + "...", color = SystemCrimson, fontWeight = FontWeight.Bold, fontSize = 13.sp, textAlign = TextAlign.End)
                                LinearProgressIndicator(
                                    progress = { opponentHp / 100f },
                                    color = if (opponentHp > 30) HpRed else SystemCrimson,
                                    trackColor = SystemSurfaceElevated,
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                                )
                                Text("HP: $opponentHp/100", color = TextSecondary, fontSize = 10.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Question Card
                        currentQuestion?.let { q ->
                            SystemWindowCard(borderColor = SystemPrimary) {
                                Text(
                                    text = "[${q.subject.uppercase()}]",
                                    color = SystemPrimary,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = q.question,
                                    color = TextPrimary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Options List
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                q.options.forEachIndexed { idx, opt ->
                                    val isSelected = selectedOption == idx
                                    val isCorrect = isAnswerSubmitted && idx == q.correct
                                    val isWrong = isAnswerSubmitted && isSelected && idx != q.correct

                                    val bg = when {
                                        isCorrect -> SystemGreen.copy(alpha = 0.2f)
                                        isWrong -> SystemCrimson.copy(alpha = 0.2f)
                                        isSelected -> SystemPrimary.copy(alpha = 0.15f)
                                        else -> SystemSurfaceElevated
                                    }

                                    val border = when {
                                        isCorrect -> SystemGreen
                                        isWrong -> SystemCrimson
                                        isSelected -> SystemPrimary
                                        else -> SystemBorder
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = bg,
                                        border = BorderStroke(1.dp, border),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(enabled = !isAnswerSubmitted) { submitAnswer(idx) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = ('A' + idx).toString(),
                                                color = if (isCorrect) SystemGreen else SystemPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = opt,
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                "result" -> {
                    // Match Results Modal
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        SystemWindowCard(
                            borderColor = if (isVictory) SystemGold else SystemCrimson,
                            glowEffect = true
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth().padding(8.dp)
                            ) {
                                Text(if (isVictory) "👑" else "💀", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isVictory) "VICTORY!" else "DEFEAT",
                                    color = if (isVictory) SystemGold else SystemCrimson,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = if (isVictory) "You slashed down ${opponent.name}!" else "${opponent.name} overpowered your defenses.",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("ELO Rating", color = TextSecondary, fontSize = 11.sp)
                                        Text(
                                            text = if (isVictory) "+25 ELO ⚡" else "-10 ELO",
                                            color = if (isVictory) SystemGreen else SystemCrimson,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Gold Reward", color = TextSecondary, fontSize = 11.sp)
                                        Text(if (isVictory) "+150 💰" else "+30 💰", color = SystemGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                SoloGlowingButton(
                                    text = "PLAY AGAIN ⚔️",
                                    onClick = { startMatchmaking() },
                                    containerColor = if (isVictory) SystemGold else SystemCrimson,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                TextButton(onClick = onExit, modifier = Modifier.fillMaxWidth()) {
                                    Text("RETURN TO SYSTEM", color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
