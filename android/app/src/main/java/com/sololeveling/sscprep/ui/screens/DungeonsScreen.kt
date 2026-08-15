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
import com.sololeveling.sscprep.data.PyqPapersData
import com.sololeveling.sscprep.domain.model.DungeonGate
import com.sololeveling.sscprep.domain.model.PyqPaper
import com.sololeveling.sscprep.domain.model.SYSTEM_DUNGEON_GATES
import com.sololeveling.sscprep.ui.components.RankBadgeChip
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun DungeonsScreen(
    viewModel: MainViewModel,
    onEnterRaid: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Dungeon Gates, 1: Official PYQs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Tab Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectedTab = 0 },
                shape = RoundedCornerShape(8.dp),
                color = if (selectedTab == 0) SystemPrimary else SystemSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedTab == 0) SystemPrimary else SystemBorder)
            ) {
                Text(
                    text = "GATE RAIDS & BOSSES",
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
                color = if (selectedTab == 1) SystemPurple else SystemSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedTab == 1) SystemPurple else SystemBorder)
            ) {
                Text(
                    text = "OFFICIAL PYQ PAPERS",
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = if (selectedTab == 1) SystemBg else TextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (selectedTab == 0) {
                items(SYSTEM_DUNGEON_GATES.size) { idx ->
                    val gate = SYSTEM_DUNGEON_GATES[idx]
                    DungeonGateCard(
                        gate = gate,
                        onEnter = {
                            viewModel.startRaid(gate.id)
                            onEnterRaid()
                        }
                    )
                }
            } else {
                items(PyqPapersData.papers.size) { idx ->
                    val paper = PyqPapersData.papers[idx]
                    PyqPaperCard(
                        paper = paper,
                        onEnter = {
                            viewModel.startPyqRaid(paper.id)
                            onEnterRaid()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DungeonGateCard(
    gate: DungeonGate,
    onEnter: () -> Unit
) {
    val isRedGate = gate.rank == "S" || gate.rank == "Monarch"

    SystemWindowCard(
        borderColor = if (isRedGate) SystemCrimson else SystemBorder,
        glowEffect = isRedGate
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(gate.bossAvatar, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = gate.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isRedGate) SystemCrimson else TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Boss: ${gate.bossName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SystemPurple
                    )
                }
            }
            RankBadgeChip(rank = gate.rank)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = gate.desc,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("⏱️ ${gate.timeMinutes} Mins", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                Text("❓ ${gate.questionCount} Questions", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                Text("💎 +${gate.goldReward} Gold", color = SystemGold, style = MaterialTheme.typography.labelSmall)
            }
            Text("+${gate.expReward} EXP", color = SystemPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
        }

        Spacer(modifier = Modifier.height(12.dp))
        SoloGlowingButton(
            text = if (gate.isInfiniteTower) "ENTER DEMON CASTLE GAUNTLET 👑" else "ENTER GATE RAID ⚔️",
            onClick = onEnter,
            modifier = Modifier.fillMaxWidth(),
            containerColor = if (isRedGate) SystemCrimson else SystemPrimary,
            contentColor = if (isRedGate) TextPrimary else SystemBg
        )
    }
}

@Composable
fun PyqPaperCard(
    paper: PyqPaper,
    onEnter: () -> Unit
) {
    SystemWindowCard(borderColor = SystemPurple) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(paper.bossAvatar, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = paper.exam,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${paper.year} • ${paper.shift}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SystemGold
                    )
                }
            }
            RankBadgeChip(rank = "S")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = paper.desc,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("⏱️ ${paper.durationMinutes} Mins", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                Text("❓ ${paper.totalQuestions} Questions", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                Text("🎯 Max ${paper.maxMarks} Marks", color = SystemGold, style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        SoloGlowingButton(
            text = "START OFFICIAL PYQ SIMULATION 🏆",
            onClick = onEnter,
            modifier = Modifier.fillMaxWidth(),
            containerColor = SystemPurple,
            contentColor = SystemBg
        )
    }
}
