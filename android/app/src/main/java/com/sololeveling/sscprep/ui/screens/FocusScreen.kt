package com.sololeveling.sscprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun FocusScreen(viewModel: MainViewModel) {
    val isFocusActive by viewModel.isFocusActive.collectAsState()
    val focusSeconds by viewModel.focusRemainingSeconds.collectAsState()
    val playerState by viewModel.playerState.collectAsState()

    var selectedPresetMinutes by remember { mutableStateOf(25) }

    val displayMinutes = focusSeconds / 60
    val displaySecs = focusSeconds % 60
    val formattedTime = if (isFocusActive) {
        String.format("%02d:%02d", displayMinutes, displaySecs)
    } else {
        String.format("%02d:00", selectedPresetMinutes)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SystemWindowCard(borderColor = SystemPurple, glowEffect = isFocusActive) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "[FOCUS SANCTUM]",
                        color = SystemPurple,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "DEEP STUDY POMODORO",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                }
                Text("🧘", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Activate high-alpha deep concentration. Complete sessions to earn +60 EXP and level up your Hunter Vitality stat.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Circular Timer Visual
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
                .background(SystemSurface)
                .border(
                    3.dp,
                    Brush.sweepGradient(listOf(SystemPurple, SystemPrimary, SystemGold, SystemPurple)),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formattedTime,
                    color = if (isFocusActive) SystemGold else TextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 44.sp,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isFocusActive) "CONCENTRATION ACTIVE" else "READY TO FOCUS",
                    color = if (isFocusActive) SystemPurple else TextSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Preset Duration Selectors
        if (!isFocusActive) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PresetButton("25 MINS", 25, selectedPresetMinutes) { selectedPresetMinutes = 25 }
                PresetButton("50 MINS", 50, selectedPresetMinutes) { selectedPresetMinutes = 50 }
                PresetButton("15 MINS", 15, selectedPresetMinutes) { selectedPresetMinutes = 15 }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Start / Stop Button
        SoloGlowingButton(
            text = if (isFocusActive) "ABORT FOCUS SESSION 🛑" else "BEGIN DEEP FOCUS SESSION ⚡",
            onClick = {
                if (isFocusActive) {
                    viewModel.stopFocusSession()
                } else {
                    viewModel.startFocusSession(selectedPresetMinutes)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            containerColor = if (isFocusActive) SystemCrimson else SystemPurple,
            contentColor = if (isFocusActive) TextPrimary else SystemBg
        )
    }
}

@Composable
fun RowScope.PresetButton(
    label: String,
    minutes: Int,
    currentSelected: Int,
    onSelect: () -> Unit
) {
    val isSelected = minutes == currentSelected
    Surface(
        modifier = Modifier
            .weight(1f)
            .height(42.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) SystemPurple else SystemSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) SystemPurple else SystemBorder),
        onClick = onSelect
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if (isSelected) SystemBg else TextPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
