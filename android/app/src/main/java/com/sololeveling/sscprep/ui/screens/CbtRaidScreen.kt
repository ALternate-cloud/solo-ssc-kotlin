package com.sololeveling.sscprep.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.domain.model.RaidResult
import com.sololeveling.sscprep.domain.model.RaidSession
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.StatProgressBar
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun CbtRaidScreen(
    viewModel: MainViewModel,
    onExitRaid: () -> Unit
) {
    val activeSession by viewModel.activeRaidSession.collectAsState()
    val raidResult by viewModel.latestRaidResult.collectAsState()
    var showSubmitConfirm by remember { mutableStateOf(false) }
    var showPaletteSheet by remember { mutableStateOf(false) }

    val session = activeSession
    if (session == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SystemBg),
            contentAlignment = Alignment.Center
        ) {
            Text("No active Raid session.", color = TextSecondary)
        }
        return
    }

    // Timer formatting
    val minutes = session.timeRemainingSeconds / 60
    val seconds = session.timeRemainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val isTimeWarning = session.timeRemainingSeconds < 120

    val currentQ = session.questions[session.currentIndex]
    val currentAns = session.answers[session.currentIndex]
    val isFlagged = session.flags[session.currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
    ) {
        // CBT Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SystemSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SystemBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = session.gate.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Question ${session.currentIndex + 1} of ${session.questions.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SystemPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isTimeWarning) SystemCrimson.copy(alpha = 0.2f) else SystemSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isTimeWarning) SystemCrimson else SystemBorder
                        )
                    ) {
                        Text(
                            text = "⏱️ $timeFormatted",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            color = if (isTimeWarning) SystemCrimson else SystemGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { showPaletteSheet = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.GridView, contentDescription = "Question Palette", tint = SystemPrimary)
                    }
                }
            }
        }

        // Live Boss Battle Combat Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SystemSurfaceElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, SystemBorder.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(session.gate.bossAvatar, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = session.gate.bossName,
                            style = MaterialTheme.typography.labelSmall,
                            color = SystemCrimson,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "SSC CGL Marking: +2.0 / -0.50",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                StatProgressBar(
                    label = "BOSS HP",
                    current = session.bossHp,
                    max = session.bossMaxHp,
                    barColor = SystemCrimson
                )
            }
        }

        // Question Content & Options Scrollable Area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Subject & Topic Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = SystemPrimary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SystemPrimary.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "${currentQ.subject} • ${currentQ.topic}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = SystemPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isFlagged) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = SystemPurple.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemPurple)
                    ) {
                        Text(
                            text = "FLAGGED FOR REVIEW",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = SystemPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Question Text
            Text(
                text = currentQ.question,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Options List
            currentQ.options.forEachIndexed { optIdx, optText ->
                val isSelected = currentAns == optIdx
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clickable { viewModel.selectOption(optIdx) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) SystemPrimary.copy(alpha = 0.18f) else SystemSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) SystemPrimary else SystemBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { viewModel.selectOption(optIdx) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = SystemPrimary,
                                unselectedColor = SystemBorder
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val prefix = ('A' + optIdx).toString()
                        Text(
                            text = "($prefix) $optText",
                            color = if (isSelected) TextPrimary else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        // Bottom Controls Dock
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SystemSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SystemBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { viewModel.toggleFlag() }) {
                        Icon(
                            Icons.Default.Flag,
                            contentDescription = "Flag",
                            tint = if (isFlagged) SystemPurple else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isFlagged) "UNFLAG" else "FLAG",
                            color = if (isFlagged) SystemPurple else TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (currentAns != null) {
                        TextButton(onClick = { viewModel.clearOption() }) {
                            Text("CLEAR RESPONSE", color = SystemCrimson, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = { showSubmitConfirm = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SystemCrimson),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SUBMIT RAID", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.prevQuestion() },
                        enabled = session.currentIndex > 0,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemBorder)
                    ) {
                        Text("PREVIOUS", color = TextPrimary)
                    }

                    Button(
                        onClick = {
                            if (session.currentIndex < session.questions.size - 1) {
                                viewModel.nextQuestion()
                            } else {
                                showSubmitConfirm = true
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SystemPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (session.currentIndex < session.questions.size - 1) "NEXT ➔" else "FINISH",
                            color = SystemBg,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Question Palette Bottom Sheet
    if (showPaletteSheet) {
        AlertDialog(
            onDismissRequest = { showPaletteSheet = false },
            containerColor = SystemSurface,
            title = {
                Text("Question Navigator", color = SystemPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PaletteLegend("Answered", SystemSuccess)
                        PaletteLegend("Flagged", SystemPurple)
                        PaletteLegend("Unanswered", SystemSurfaceElevated)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 240.dp)
                    ) {
                        items(session.questions.size) { idx ->
                            val isAns = session.answers[idx] != null
                            val isFlag = session.flags[idx]
                            val isCurr = session.currentIndex == idx

                            val bgColor = when {
                                isCurr -> SystemPrimary
                                isFlag -> SystemPurple
                                isAns -> SystemSuccess
                                else -> SystemSurfaceElevated
                            }

                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bgColor)
                                    .clickable {
                                        viewModel.goToQuestion(idx)
                                        showPaletteSheet = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    color = if (isCurr) SystemBg else TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPaletteSheet = false }) {
                    Text("CLOSE", color = TextSecondary)
                }
            }
        )
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirm) {
        var answeredCount = session.answers.count { it != null }
        var unattemptedCount = session.questions.size - answeredCount

        AlertDialog(
            onDismissRequest = { showSubmitConfirm = false },
            containerColor = SystemSurface,
            title = {
                Text("Confirm Raid Submission", color = SystemCrimson, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("Are you ready to submit your CBT Mock Exam?", color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Answered: $answeredCount", color = SystemSuccess, fontWeight = FontWeight.Bold)
                    Text("• Unattempted: $unattemptedCount", color = SystemGold, fontWeight = FontWeight.Bold)
                    Text("• Time Remaining: $timeFormatted", color = TextSecondary)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirm = false
                        viewModel.submitRaid()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SystemCrimson)
                ) {
                    Text("CONFIRM SUBMIT", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirm = false }) {
                    Text("RETURN", color = TextSecondary)
                }
            }
        )
    }

    // Raid Result Modal
    if (raidResult != null) {
        RaidResultDialog(
            result = raidResult!!,
            onDismiss = {
                viewModel.dismissRaid()
                onExitRaid()
            }
        )
    }
}

@Composable
fun PaletteLegend(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
    }
}
