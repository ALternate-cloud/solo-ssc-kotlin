package com.sololeveling.sscprep.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.sololeveling.sscprep.domain.engine.InfiniteQuestionGenerator
import com.sololeveling.sscprep.domain.model.Question
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

data class TowerBoss(
    val floor: Int,
    val name: String,
    val title: String,
    val avatar: String,
    val bonusStatPoints: Int,
    val bonusGold: Int
)

val TOWER_BOSSES = listOf(
    TowerBoss(25, "Cerberus", "The Gatekeeper of Hellfire", "🐺", 3, 500),
    TowerBoss(50, "Volkan", "The Demon Monarch Architect", "🌋", 5, 1000),
    TowerBoss(75, "Metus", "The Undead Necromancer Sovereign", "💀", 8, 2000),
    TowerBoss(100, "Baran (Demon King)", "The Sovereign of White Flames", "👑", 15, 5000)
)

@Composable
fun DemonCastleScreen(
    viewModel: MainViewModel,
    onExit: () -> Unit
) {
    val playerState by viewModel.playerState.collectAsState()
    val tower = playerState.towerState
    val highestCleared = tower.highestFloorCleared

    var viewState by remember { mutableStateOf("tower_map") } // tower_map, floor_battle, floor_result
    var activeFloor by remember { mutableStateOf(highestCleared + 1) }

    var questionsList by remember { mutableStateOf<List<Question>>(emptyList()) }
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    var correctCount by remember { mutableStateOf(0) }
    var timerSeconds by remember { mutableStateOf(30) }
    var isTimerActive by remember { mutableStateOf(false) }
    var floorClearedSuccess by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    fun advanceFloorQuestion() {
        if (currentQuestionIdx >= questionsList.size - 1) {
            // Finished 3 questions
            isTimerActive = false
            floorClearedSuccess = correctCount >= 2 // 2 out of 3 needed to clear floor
            viewState = "floor_result"
            if (floorClearedSuccess) {
                viewModel.soundAndHaptics.playLevelUp()
                val boss = TOWER_BOSSES.find { it.floor == activeFloor }
                viewModel.recordDemonTowerFloor(
                    floor = activeFloor,
                    bonusPoints = boss?.bonusStatPoints ?: 0,
                    bonusGold = boss?.bonusGold ?: (activeFloor * 20)
                )
            } else {
                viewModel.soundAndHaptics.playBossRoar()
            }
        } else {
            currentQuestionIdx++
            selectedOption = null
            isSubmitted = false
            isTimerActive = true
        }
    }

    fun startFloor(floor: Int) {
        activeFloor = floor
        val diff = when {
            floor <= 25 -> "Easy"
            floor <= 60 -> "Medium"
            else -> "Hard"
        }
        questionsList = listOf(
            InfiniteQuestionGenerator.generateQuantQuestion(diff),
            InfiniteQuestionGenerator.generateQuantQuestion(diff),
            InfiniteQuestionGenerator.generateQuantQuestion(diff)
        )
        currentQuestionIdx = 0
        correctCount = 0
        selectedOption = null
        isSubmitted = false
        viewState = "floor_battle"
        isTimerActive = true
        viewModel.soundAndHaptics.playClick()
    }

    fun submitFloorAnswer(optIdx: Int) {
        if (isSubmitted || currentQuestionIdx >= questionsList.size) return
        selectedOption = optIdx
        isSubmitted = true
        isTimerActive = false

        val q = questionsList[currentQuestionIdx]
        val isCorrect = optIdx == q.correct
        if (isCorrect) {
            correctCount++
            viewModel.soundAndHaptics.playClick()
        } else {
            viewModel.soundAndHaptics.playBossRoar()
        }

        coroutineScope.launch {
            delay(1500)
            advanceFloorQuestion()
        }
    }

    // Scroll to current unlocked floor
    LaunchedEffect(Unit) {
        val targetIdx = maxOf(0, 100 - (highestCleared + 3))
        listState.scrollToItem(targetIdx)
    }

    // Floor challenge countdown timer
    LaunchedEffect(viewState, currentQuestionIdx, isTimerActive) {
        if (viewState == "floor_battle" && isTimerActive) {
            val totalTime = when {
                activeFloor <= 25 -> 45
                activeFloor <= 50 -> 35
                activeFloor <= 75 -> 25
                else -> 18
            }
            timerSeconds = totalTime
            while (timerSeconds > 0 && !isSubmitted) {
                delay(1000)
                timerSeconds--
            }
            if (timerSeconds == 0 && !isSubmitted) {
                isSubmitted = true
                viewModel.soundAndHaptics.playBossRoar()
                delay(1500)
                advanceFloorQuestion()
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
                    text = "DEMON CASTLE TOWER 🗼",
                    style = MaterialTheme.typography.titleMedium,
                    color = SystemPurple,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SystemSurfaceElevated,
                    border = BorderStroke(1.dp, SystemPurple)
                ) {
                    Text(
                        text = "FLOOR $highestCleared / 100",
                        color = SystemPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (viewState) {
                "tower_map" -> {
                    // Tower Map (Floor 100 down to Floor 1)
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            SystemWindowCard(borderColor = SystemPurple, glowEffect = true) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                    Text("🗼", fontSize = 42.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("100-FLOOR GAUNTLET", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Black)
                                    Text("Climb to Floor 100 and slay Baran, the Demon King. Floor Bosses grant permanent stat points!", color = TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
                                }
                            }
                        }

                        // 100 Floors in descending order (Floor 100 at top, Floor 1 at bottom)
                        val floors = (100 downTo 1).toList()
                        items(floors) { floorNum ->
                            val isCleared = floorNum <= highestCleared
                            val isCurrent = floorNum == highestCleared + 1
                            val isLocked = floorNum > highestCleared + 1
                            val boss = TOWER_BOSSES.find { it.floor == floorNum }

                            val borderColor = when {
                                boss != null && (isCleared || isCurrent) -> SystemGold
                                isCurrent -> SystemPurple
                                isCleared -> SystemGreen
                                else -> SystemBorder.copy(alpha = 0.5f)
                            }

                            val bg = when {
                                isCurrent -> SystemPurple.copy(alpha = 0.15f)
                                isCleared -> SystemSurfaceElevated
                                else -> SystemSurface.copy(alpha = 0.5f)
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = bg,
                                border = BorderStroke(if (isCurrent || boss != null) 1.5.dp else 1.dp, borderColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = isCurrent) { startFloor(floorNum) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (isCurrent) SystemPurple else SystemSurface,
                                            border = BorderStroke(1.dp, borderColor),
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = if (boss != null) boss.avatar else "$floorNum",
                                                    fontSize = if (boss != null) 16.sp else 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isCurrent) TextPrimary else TextSecondary
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = if (boss != null) "FLOOR $floorNum: ${boss.name.uppercase()}" else "FLOOR $floorNum",
                                                color = if (boss != null) SystemGold else if (isCurrent) SystemPurple else if (isCleared) TextPrimary else TextMuted,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = if (boss != null) "👑 BOSS REWARD: +${boss.bonusStatPoints} STAT POINTS" else "3 Speed Questions • +${floorNum * 20} Gold",
                                                color = if (boss != null) SystemGold.copy(alpha = 0.8f) else TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    when {
                                        isCleared -> Text("CLEARED 🟢", color = SystemGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        isCurrent -> Text("CHALLENGE ⚡", color = SystemPurple, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                        else -> Icon(Icons.Default.Lock, contentDescription = "Locked", tint = TextMuted, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                "floor_battle" -> {
                    // Battle Floor
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Floor Header & Timer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("FLOOR $activeFloor TRIAL", color = SystemPurple, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                Text("Question ${currentQuestionIdx + 1} of 3", color = TextSecondary, fontSize = 12.sp)
                            }
                            Surface(
                                shape = CircleShape,
                                color = if (timerSeconds <= 5) SystemCrimson else SystemSurfaceElevated,
                                border = BorderStroke(1.dp, if (timerSeconds <= 5) SystemCrimson else SystemPurple),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("$timerSeconds", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (currentQuestionIdx < questionsList.size) {
                            val q = questionsList[currentQuestionIdx]
                            SystemWindowCard(borderColor = SystemPurple) {
                                Text("[${q.subject.uppercase()}]", color = SystemPurple, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(q.question, color = TextPrimary, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                q.options.forEachIndexed { idx, opt ->
                                    val isSelected = selectedOption == idx
                                    val isCorrect = isSubmitted && idx == q.correct
                                    val isWrong = isSubmitted && isSelected && idx != q.correct

                                    val bg = when {
                                        isCorrect -> SystemGreen.copy(alpha = 0.2f)
                                        isWrong -> SystemCrimson.copy(alpha = 0.2f)
                                        isSelected -> SystemPurple.copy(alpha = 0.15f)
                                        else -> SystemSurfaceElevated
                                    }
                                    val border = when {
                                        isCorrect -> SystemGreen
                                        isWrong -> SystemCrimson
                                        isSelected -> SystemPurple
                                        else -> SystemBorder
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = bg,
                                        border = BorderStroke(1.dp, border),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(enabled = !isSubmitted) { submitFloorAnswer(idx) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(('A' + idx).toString(), color = if (isCorrect) SystemGreen else SystemPurple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(opt, color = TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                "floor_result" -> {
                    // Floor Results Modal
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        SystemWindowCard(
                            borderColor = if (floorClearedSuccess) SystemGold else SystemCrimson,
                            glowEffect = true
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth().padding(8.dp)
                            ) {
                                Text(if (floorClearedSuccess) "🎉" else "💀", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (floorClearedSuccess) "FLOOR $activeFloor CONQUERED!" else "FLOOR TRIAL FAILED",
                                    color = if (floorClearedSuccess) SystemGold else SystemCrimson,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = if (floorClearedSuccess) "Score: $correctCount / 3 correct. Floor $activeFloor has been purified!" else "Score: $correctCount / 3 correct. You need at least 2 correct to advance.",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                if (floorClearedSuccess) {
                                    val boss = TOWER_BOSSES.find { it.floor == activeFloor }
                                    if (boss != null) {
                                        Text("👑 BOSS SLAIN: +${boss.bonusStatPoints} STAT POINTS!", color = SystemGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                    Text("+${activeFloor * 20} Gold • Next Floor Unlocked", color = SystemGreen, fontSize = 13.sp)
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                if (floorClearedSuccess && activeFloor < 100) {
                                    SoloGlowingButton(
                                        text = "NEXT FLOOR (${activeFloor + 1}) ⚡",
                                        onClick = { startFloor(activeFloor + 1) },
                                        containerColor = SystemPurple,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                TextButton(
                                    onClick = { viewState = "tower_map" },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("RETURN TO TOWER MAP", color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
