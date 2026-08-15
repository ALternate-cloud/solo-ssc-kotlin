package com.sololeveling.sscprep.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.sololeveling.sscprep.domain.model.FallenMonster
import com.sololeveling.sscprep.domain.model.ShadowCommander
import com.sololeveling.sscprep.ui.components.RankBadgeChip
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.StatProgressBar
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun ShadowArmyScreen(viewModel: MainViewModel) {
    val shadowState by viewModel.shadowState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0: Mistake Notebook (ARISE), 1: Shadow Commanders

    val unresolvedMonsters = remember(shadowState.fallenMonsters) {
        shadowState.fallenMonsters.filter { !it.resolved }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Monarch Army Header Card
        item {
            SystemWindowCard(borderColor = SystemPurple, glowEffect = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "[SHADOW EXTRACTION SYSTEM]",
                            color = SystemPurple,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "SHADOW ARMY & NOTEBOOK",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary
                        )
                    }
                    Text("👥", fontSize = 30.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = SystemBorder)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = SystemSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemPurple.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("TOTAL SHADOWS", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text("${shadowState.totalShadows}", style = MaterialTheme.typography.titleLarge, color = SystemPurple, fontWeight = FontWeight.Bold)
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = SystemSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemGold.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("PENDING ARISE", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text("${unresolvedMonsters.size}", style = MaterialTheme.typography.titleLarge, color = SystemGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Tab Selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 0 },
                    shape = RoundedCornerShape(8.dp),
                    color = if (selectedTab == 0) SystemPurple else SystemSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedTab == 0) SystemPurple else SystemBorder)
                ) {
                    Text(
                        text = "MISTAKE EXTRACTION (${unresolvedMonsters.size})",
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = if (selectedTab == 0) SystemBg else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 1 },
                    shape = RoundedCornerShape(8.dp),
                    color = if (selectedTab == 1) SystemPrimary else SystemSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedTab == 1) SystemPrimary else SystemBorder)
                ) {
                    Text(
                        text = "SHADOW COMMANDERS",
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = if (selectedTab == 1) SystemBg else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        if (selectedTab == 0) {
            // Mistake Notebook List
            if (unresolvedMonsters.isEmpty()) {
                item {
                    SystemWindowCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🛡️", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "NO FALLEN MONSTERS PENDING",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Every mistake you make in CBT Mock Raids will appear here to be extracted into loyal Shadow Soldiers via 'ARISE'!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(unresolvedMonsters.size) { idx ->
                    val monster = unresolvedMonsters[idx]
                    FallenMonsterExtractionCard(
                        monster = monster,
                        onArise = { viewModel.extractShadow(monster.id) }
                    )
                }
            }
        } else {
            // Shadow Commanders List
            items(shadowState.commanders.size) { idx ->
                val commander = shadowState.commanders[idx]
                ShadowCommanderCard(commander = commander)
            }
        }
    }
}

@Composable
fun FallenMonsterExtractionCard(
    monster: FallenMonster,
    onArise: () -> Unit
) {
    val q = monster.question
    var expanded by remember { mutableStateOf(false) }

    SystemWindowCard(borderColor = SystemPurple) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = SystemCrimson.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, SystemCrimson.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "${q.subject} • ${q.topic}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = SystemCrimson,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(monster.timestamp, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = q.question,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val userAnsText = monster.wrongAnswerIndex?.let { q.options.getOrNull(it) } ?: "Unattempted"
            Text("Your Answer: ❌ $userAnsText", color = SystemCrimson, fontSize = 12.sp)
            val correctAnsText = q.options.getOrNull(q.correct) ?: ""
            Text("Correct: ✔️ $correctAnsText", color = SystemSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))
        SoloGlowingButton(
            text = "ARISE (EXTRACT SHADOW SOLDIER) ⚡",
            onClick = onArise,
            modifier = Modifier.fillMaxWidth(),
            containerColor = SystemPurple,
            contentColor = SystemBg
        )

        Spacer(modifier = Modifier.height(4.dp))
        TextButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(if (expanded) "HIDE SOLUTION" else "VIEW SHORTCUT & SOLUTION", color = SystemGold, fontSize = 11.sp)
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SystemSurfaceElevated, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Text(q.explanation, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                if (!q.trick.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("⚡ Shortcut: ${q.trick}", color = SystemGold, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ShadowCommanderCard(commander: ShadowCommander) {
    SystemWindowCard(borderColor = SystemBorder) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(commander.avatar, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = commander.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Subject: ${commander.subject}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SystemPrimary
                    )
                }
            }
            RankBadgeChip(rank = commander.rank)
        }

        Spacer(modifier = Modifier.height(10.dp))
        StatProgressBar(
            label = "COMMANDER LEVEL ${commander.level}",
            current = commander.exp,
            max = commander.maxExp,
            barColor = SystemPurple
        )

        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = SystemSurfaceElevated
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🛡️", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Active Buff: ${commander.buff}",
                    style = MaterialTheme.typography.labelSmall,
                    color = SystemGold
                )
            }
        }
    }
}
