package com.sololeveling.sscprep.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.domain.engine.DailyQuestEngine
import com.sololeveling.sscprep.domain.engine.InfiniteQuestionGenerator
import com.sololeveling.sscprep.domain.model.DailyTask
import com.sololeveling.sscprep.domain.model.Question
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.StatProgressBar
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun QuestsScreen(
    viewModel: MainViewModel,
    onStartPomodoro: () -> Unit
) {
    val questState by viewModel.questState.collectAsState()
    val countdown by viewModel.countdownToMidnight.collectAsState()
    val isAllCompleted = DailyQuestEngine.isAllCompleted(questState)
    val overallPercentage = DailyQuestEngine.getOverallProgressPercentage(questState)

    var activePracticeTask by remember { mutableStateOf<DailyTask?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Red Penalty Alert if Active
        if (questState.penaltyActive) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SystemCrimson, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SystemCrimson.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🚨", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PENALTY QUEST: SURVIVAL ZONE ACTIVE",
                                color = SystemCrimson,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "You missed yesterday's study quota! Complete today's training drills to clear the penalty and avoid stat decay.",
                            color = TextPrimary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Daily Quest Header Card
        item {
            SystemWindowCard(borderColor = SystemPrimary, glowEffect = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "[MANDATORY DAILY QUEST]",
                            color = SystemPrimary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "DAILY SYSTEM DRILLS",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SystemSurfaceElevated,
                        border = BorderStroke(1.dp, SystemGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔥", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${questState.streak} DAYS",
                                color = SystemGold,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = SystemBorder)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reset in: $countdown",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "$overallPercentage% COMPLETED",
                        color = if (isAllCompleted) SystemSuccess else SystemPrimary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                StatProgressBar(
                    label = "DAILY GOAL PROGRESS",
                    current = overallPercentage,
                    max = 100,
                    barColor = if (isAllCompleted) SystemSuccess else SystemPrimary
                )
            }
        }

        // Daily Tasks List with Interactive Training Drills
        items(questState.tasks.size) { idx ->
            val task = questState.tasks[idx]
            val isDone = task.current >= task.target

            SystemWindowCard(
                borderColor = if (isDone) SystemSuccess else SystemBorder
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.name,
                            color = if (isDone) SystemSuccess else TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "+${task.expReward} EXP Reward upon completion",
                            color = SystemGold,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    if (isDone) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SystemSuccess.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, SystemSuccess)
                        ) {
                            Text(
                                text = "DONE ✅",
                                color = SystemSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                if (task.id == "t_focus") {
                                    onStartPomodoro()
                                } else {
                                    activePracticeTask = task
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SystemPrimary),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("TRAIN ⚡", color = SystemBg, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                StatProgressBar(
                    label = "${task.current} / ${task.target} ${task.unit}",
                    current = task.current,
                    max = task.target,
                    barColor = if (isDone) SystemSuccess else SystemPrimary
                )
            }
        }

        // Deep Focus Sanctum Card
        item {
            SystemWindowCard(borderColor = SystemPurple) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SANCTUM OF FOCUS (POMODORO)",
                            color = SystemPurple,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Launch deep study timer with ambient alpha waves (+1 min towards daily quota per min).",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Button(
                        onClick = onStartPomodoro,
                        colors = ButtonDefaults.buttonColors(containerColor = SystemPurple)
                    ) {
                        Text("ENTER 🧘", color = SystemBg, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Claim Rewards Action Button
        item {
            val canClaim = isAllCompleted && !questState.claimed
            SoloGlowingButton(
                text = if (questState.claimed) "DAILY REWARDS CLAIMED ✅" else "CLAIM DAILY QUEST REWARDS (250 EXP + 120 GOLD)",
                onClick = { viewModel.claimDailyReward() },
                modifier = Modifier.fillMaxWidth(),
                containerColor = if (canClaim) SystemSuccess else SystemSurfaceElevated,
                contentColor = if (canClaim) SystemBg else TextMuted,
                enabled = canClaim
            )
        }
    }

    // Interactive Quest Practice Modal
    activePracticeTask?.let { task ->
        QuestPracticeModal(
            task = task,
            onDismiss = { activePracticeTask = null },
            onAnswerCorrect = {
                viewModel.incrementTask(task.id, 1)
                viewModel.soundAndHaptics.playClick()
            }
        )
    }
}

@Composable
fun QuestPracticeModal(
    task: DailyTask,
    onDismiss: () -> Unit,
    onAnswerCorrect: () -> Unit
) {
    val subject = when (task.id) {
        "t_quant" -> "Quantitative Aptitude"
        "t_reas" -> "General Intelligence & Reasoning"
        "t_eng" -> "English Language"
        else -> "General Awareness"
    }

    var question by remember { mutableStateOf(InfiniteQuestionGenerator.generateBySubject(subject)) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var streakCount by remember { mutableStateOf(0) }

    fun nextQuestion() {
        question = InfiniteQuestionGenerator.generateBySubject(subject)
        selectedOption = null
        isSubmitted = false
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SystemSurface,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "[QUEST DRILL: ${task.name.take(20)}...]",
                        color = SystemPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "${task.current} / ${task.target} Cleared ⚡",
                        color = SystemGold,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Topic tag
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = SystemPrimary.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, SystemPrimary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "${question.subject} • ${question.topic}",
                        color = SystemPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Question text
                Text(
                    text = question.question,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Options
                question.options.forEachIndexed { idx, opt ->
                    val isSelected = selectedOption == idx
                    val isCorrect = isSubmitted && idx == question.correct
                    val isWrong = isSubmitted && isSelected && idx != question.correct

                    val bg = when {
                        isCorrect -> SystemGreen.copy(alpha = 0.2f)
                        isWrong -> SystemCrimson.copy(alpha = 0.2f)
                        isSelected -> SystemPrimary.copy(alpha = 0.2f)
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
                            .clickable(enabled = !isSubmitted) {
                                selectedOption = idx
                                isSubmitted = true
                                if (idx == question.correct) {
                                    streakCount++
                                    onAnswerCorrect()
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ('A' + idx).toString(),
                                color = if (isCorrect) SystemGreen else SystemPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = opt, color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }

                // Shortcut trick / explanation upon answer
                if (isSubmitted) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SystemSurfaceElevated,
                        border = BorderStroke(1.dp, SystemGold.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = if (selectedOption == question.correct) "✅ CORRECT! +1 Toward Daily Quest" else "❌ WRONG ANSWER",
                                color = if (selectedOption == question.correct) SystemGreen else SystemCrimson,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            if (!question.trick.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "⚡ Trick: ${question.trick}", color = SystemGold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (isSubmitted) {
                Button(
                    onClick = { nextQuestion() },
                    colors = ButtonDefaults.buttonColors(containerColor = SystemPrimary)
                ) {
                    Text("NEXT QUESTION ⚡", color = SystemBg, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("DONE FOR NOW", color = TextSecondary)
            }
        }
    )
}
