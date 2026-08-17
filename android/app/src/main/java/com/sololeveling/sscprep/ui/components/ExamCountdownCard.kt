package com.sololeveling.sscprep.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.sololeveling.sscprep.domain.model.ExamTarget
import com.sololeveling.sscprep.domain.model.OFFICIAL_SSC_EXAMS
import com.sololeveling.sscprep.ui.theme.*

@Composable
fun ExamCountdownCard(
    selectedExamId: String = "cgl_tier1_2025",
    onExamSelected: (ExamTarget) -> Unit = {}
) {
    var activeExam by remember {
        mutableStateOf(OFFICIAL_SSC_EXAMS.find { it.id == selectedExamId } ?: OFFICIAL_SSC_EXAMS[0])
    }

    var currentTimeMs by remember { mutableStateOf(System.currentTimeMillis()) }

    // Live 1-second countdown ticker
    LaunchedEffect(Unit) {
        while (true) {
            currentTimeMs = System.currentTimeMillis()
            delay(1000)
        }
    }

    val diffMs = maxOf(0L, activeExam.targetTimestampMs - currentTimeMs)
    val days = diffMs / (1000 * 60 * 60 * 24)
    val hours = (diffMs / (1000 * 60 * 60)) % 24
    val minutes = (diffMs / (1000 * 60)) % 60
    val seconds = (diffMs / 1000) % 60

    SystemWindowCard(borderColor = SystemGold, glowEffect = true) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(activeExam.icon, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "[NATIONAL EXAM TIMER]",
                            color = SystemGold,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = activeExam.title,
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Exam Selector Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(OFFICIAL_SSC_EXAMS) { exam ->
                    val isSelected = exam.id == activeExam.id
                    val tabBorder = if (isSelected) SystemGold else SystemBorder
                    val tabBg = if (isSelected) SystemGold.copy(alpha = 0.15f) else SystemSurfaceElevated

                    Box(
                        modifier = Modifier
                            .background(tabBg, RoundedCornerShape(8.dp))
                            .border(1.dp, tabBorder, RoundedCornerShape(8.dp))
                            .clickable {
                                activeExam = exam
                                onExamSelected(exam)
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${exam.icon} ${exam.shortName}",
                            color = if (isSelected) SystemGold else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Holographic Countdown Display Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CountdownTimeBlock(value = days.toString().padStart(2, '0'), label = "DAYS", color = SystemPrimary)
                CountdownSeparator()
                CountdownTimeBlock(value = hours.toString().padStart(2, '0'), label = "HOURS", color = SystemPurple)
                CountdownSeparator()
                CountdownTimeBlock(value = minutes.toString().padStart(2, '0'), label = "MINS", color = SystemGold)
                CountdownSeparator()
                CountdownTimeBlock(value = seconds.toString().padStart(2, '0'), label = "SECS", color = SystemCrimson)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Target Post Info Footnote
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = SystemSurfaceElevated.copy(alpha = 0.6f),
                border = androidx.compose.foundation.BorderStroke(1.dp, SystemBorder.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎯", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Target Roles: ${activeExam.officialPost} • Date: ${activeExam.targetDateString}",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun CountdownTimeBlock(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(listOf(color.copy(alpha = 0.12f), Color.Transparent)),
                shape = RoundedCornerShape(8.dp)
            )
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = value,
            color = color,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            letterSpacing = 1.sp
        )
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun CountdownSeparator() {
    Text(
        text = ":",
        color = SystemBorder,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 10.dp)
    )
}
