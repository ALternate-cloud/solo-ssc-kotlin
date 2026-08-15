package com.sololeveling.sscprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.domain.engine.DailyQuestEngine
import com.sololeveling.sscprep.domain.model.DailyTask
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
                            text = "You failed to complete yesterday's daily study quota! Complete today's tasks immediately to clear the penalty and avoid stat decay.",
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
                            text = "PREPARING FOR SSC CGL",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SystemSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemGold)
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
                    label = "DAILY PROGRESS",
                    current = overallPercentage,
                    max = 100,
                    barColor = if (isAllCompleted) SystemSuccess else SystemPrimary
                )
            }
        }

        // Daily Tasks List
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

                    Checkbox(
                        checked = isDone,
                        onCheckedChange = { viewModel.toggleTask(task.id) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = SystemSuccess,
                            uncheckedColor = SystemBorder,
                            checkmarkColor = SystemBg
                        )
                    )
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

        // Deep Focus Quick Launcher Card
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
                            text = "Launch 25-min deep study session with ambient focus binaural alpha waves.",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Button(
                        onClick = onStartPomodoro,
                        colors = ButtonDefaults.buttonColors(containerColor = SystemPurple)
                    ) {
                        Text("ENTER", color = SystemBg, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Claim Rewards Action
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
}
