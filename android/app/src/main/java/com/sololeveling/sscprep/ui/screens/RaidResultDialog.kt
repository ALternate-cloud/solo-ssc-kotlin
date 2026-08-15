package com.sololeveling.sscprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sololeveling.sscprep.domain.model.RaidResult
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.theme.*

@Composable
fun RaidResultDialog(
    result: RaidResult,
    onDismiss: () -> Unit
) {
    val isVictory = result.isBossDefeated

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .background(SystemSurface, RoundedCornerShape(16.dp))
                .border(
                    1.dp,
                    if (isVictory) SystemSuccess else SystemCrimson,
                    RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = if (isVictory) "🏆 DUNGEON CONQUERED!" else "💀 RAID FAILED",
                    style = MaterialTheme.typography.headlineLarge,
                    color = if (isVictory) SystemGold else SystemCrimson,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "${result.bossName} has been ${if (isVictory) "SLAYN!" else "survived."}",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                HorizontalDivider(color = SystemBorder)

                // Score Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ResultMetricBox("Score", String.format("%.2f", result.rawScore), SystemGold, Modifier.weight(1f))
                    ResultMetricBox("Accuracy", "${result.accuracy}%", SystemPrimary, Modifier.weight(1f))
                    ResultMetricBox("Correct", "${result.correctCount}", SystemSuccess, Modifier.weight(1f))
                    ResultMetricBox("Wrong", "${result.wrongCount}", SystemCrimson, Modifier.weight(1f))
                }

                // Rewards breakdown
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = SystemSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SystemBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "SYSTEM REWARDS AWARDED:",
                            color = SystemPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Hunter Experience Points:", color = TextSecondary)
                            Text("+${result.expEarned} EXP", color = SystemPrimary, fontWeight = FontWeight.Bold)
                        }
                        if (result.goldEarned > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Gold Crystals Earned:", color = TextSecondary)
                                Text("+${result.goldEarned} 💎", color = SystemGold, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (result.wrongCount > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Fallen Monsters:", color = TextSecondary)
                                Text("${result.wrongCount} Ready to ARISE", color = SystemPurple, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                SoloGlowingButton(
                    text = "CLAIM LOOT & RETURN TO GUILD",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = if (isVictory) SystemGold else SystemPrimary,
                    contentColor = SystemBg
                )
            }
        }
    }
}

@Composable
fun ResultMetricBox(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = SystemSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, SystemBorder)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = valueColor, fontWeight = FontWeight.Black, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, color = TextSecondary, fontSize = 11.sp)
        }
    }
}
