package com.sololeveling.sscprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import com.sololeveling.sscprep.domain.model.HUNTER_RANKS
import com.sololeveling.sscprep.domain.model.PlayerState
import com.sololeveling.sscprep.domain.model.SSC_TARGET_POSTS
import com.sololeveling.sscprep.domain.model.TargetPost
import com.sololeveling.sscprep.ui.components.RankBadgeChip
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.StatAllocationRow
import com.sololeveling.sscprep.ui.components.StatProgressBar
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun StatusScreen(
    viewModel: MainViewModel,
    onNavigateToQuests: () -> Unit,
    onNavigateToDungeons: () -> Unit
) {
    val playerState by viewModel.playerState.collectAsState()
    var showPostPicker by remember { mutableStateOf(false) }
    var showNameEdit by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf(playerState.name) }

    val currentPost = SSC_TARGET_POSTS.find { it.id == playerState.targetPostId } ?: SSC_TARGET_POSTS[0]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Top Holographic Notification
        item {
            SystemWindowCard(
                borderColor = SystemPrimary,
                glowEffect = true
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "[SYSTEM NOTIFICATION]",
                            color = SystemPrimary,
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "HUNTER STATUS WINDOW",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary
                        )
                    }
                    RankBadgeChip(rank = playerState.rank)
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = SystemBorder)
                Spacer(modifier = Modifier.height(12.dp))

                // Hunter Name & Class
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = playerState.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary
                            )
                            IconButton(
                                onClick = { showNameEdit = true },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Name",
                                    tint = SystemPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = "Title: ${playerState.title}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SystemPurple
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SystemSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "LEVEL",
                                style = MaterialTheme.typography.labelSmall,
                                color = SystemPrimary
                            )
                            Text(
                                text = "${playerState.level}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = SystemGold,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // HP, MP, and EXP Bars
                StatProgressBar(
                    label = "HP (STAMINA & ENDURANCE)",
                    current = playerState.hp,
                    max = playerState.maxHp,
                    barColor = HpRed
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatProgressBar(
                    label = "MP (FOCUS & MANA)",
                    current = playerState.mp,
                    max = playerState.maxMp,
                    barColor = MpBlue
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatProgressBar(
                    label = "EXP TO NEXT LEVEL",
                    current = playerState.exp,
                    max = playerState.maxExp,
                    barColor = ExpGold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Gold Crystals & Unallocated Stat Points
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = SystemSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemGold.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("💎", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("GOLD CRYSTALS", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                Text("${playerState.gold}", style = MaterialTheme.typography.titleMedium, color = SystemGold, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = SystemSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemPrimary.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚡", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("STAT POINTS", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                Text("${playerState.unallocatedPoints}", style = MaterialTheme.typography.titleMedium, color = SystemPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Target SSC CGL Post Card
        item {
            SystemWindowCard(borderColor = SystemPurple) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ASSIGNED MONARCH OBJECTIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = SystemPurple,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentPost.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(currentPost.icon, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ministry: ${currentPost.ministry}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "Expected Cutoff: ${currentPost.cutoffTarget}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SystemGold
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { showPostPicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SystemPurple),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("CHANGE TARGET SSC POST", color = SystemPurple, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Stat Points Allocation Window
        item {
            SystemWindowCard(borderColor = SystemPrimary) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PLAYER ATTRIBUTE STATS",
                        style = MaterialTheme.typography.titleMedium,
                        color = SystemPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    if (playerState.unallocatedPoints > 0) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = SystemPrimary.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SystemPrimary)
                        ) {
                            Text(
                                text = "${playerState.unallocatedPoints} PTS AVAILABLE",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = SystemPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                StatAllocationRow(
                    statName = "Intelligence (Concept Mastery)",
                    statAbbr = "INT",
                    statValue = playerState.stats.intelligence,
                    statDescription = "Boosts Maximum MP & Reasoning accuracy",
                    pointsAvailable = playerState.unallocatedPoints,
                    onAllocate = { viewModel.allocateStat("INT") }
                )
                HorizontalDivider(color = SystemBorder.copy(alpha = 0.5f))

                StatAllocationRow(
                    statName = "Vitality (Endurance & HP)",
                    statAbbr = "VIT",
                    statValue = playerState.stats.vitality,
                    statDescription = "Boosts Maximum HP & Study Stamina",
                    pointsAvailable = playerState.unallocatedPoints,
                    onAllocate = { viewModel.allocateStat("VIT") }
                )
                HorizontalDivider(color = SystemBorder.copy(alpha = 0.5f))

                StatAllocationRow(
                    statName = "Agility (Solving Speed)",
                    statAbbr = "AGI",
                    statValue = playerState.stats.agility,
                    statDescription = "Shortens time required per complex question",
                    pointsAvailable = playerState.unallocatedPoints,
                    onAllocate = { viewModel.allocateStat("AGI") }
                )
                HorizontalDivider(color = SystemBorder.copy(alpha = 0.5f))

                StatAllocationRow(
                    statName = "Sense (Negative Mark Avoidance)",
                    statAbbr = "SEN",
                    statValue = playerState.stats.sense,
                    statDescription = "Heightens precision on tricky options",
                    pointsAvailable = playerState.unallocatedPoints,
                    onAllocate = { viewModel.allocateStat("SEN") }
                )
                HorizontalDivider(color = SystemBorder.copy(alpha = 0.5f))

                StatAllocationRow(
                    statName = "Strength (Habit Discipline)",
                    statAbbr = "STR",
                    statValue = playerState.stats.strength,
                    statDescription = "Increases daily quest EXP rewards",
                    pointsAvailable = playerState.unallocatedPoints,
                    onAllocate = { viewModel.allocateStat("STR") }
                )
            }
        }

        // Hunter Achievements & Milestones
        item {
            SystemWindowCard {
                Text(
                    text = "HUNTER MILESTONES & RECORDS",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MilestoneBox("Solved", "${playerState.milestones.totalQuestionsSolved}", "📝", Modifier.weight(1f))
                    MilestoneBox("Mocks", "${playerState.milestones.mockTestsCleared}", "⚔️", Modifier.weight(1f))
                    MilestoneBox("Shadows", "${playerState.milestones.shadowsExtracted}", "👥", Modifier.weight(1f))
                    MilestoneBox("Streak", "${playerState.milestones.streakDays}d", "🔥", Modifier.weight(1f))
                }
            }
        }

        // Quick action buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SoloGlowingButton(
                    text = "DAILY QUESTS",
                    onClick = onNavigateToQuests,
                    modifier = Modifier.weight(1f),
                    containerColor = SystemPurple
                )
                SoloGlowingButton(
                    text = "ENTER DUNGEON",
                    onClick = onNavigateToDungeons,
                    modifier = Modifier.weight(1f),
                    containerColor = SystemPrimary
                )
            }
        }
    }

    // Name Edit Dialog
    if (showNameEdit) {
        AlertDialog(
            onDismissRequest = { showNameEdit = false },
            containerColor = SystemSurface,
            title = { Text("Update Hunter Name", color = SystemPrimary, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Hunter Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = SystemPrimary,
                        unfocusedBorderColor = SystemBorder
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setPlayerName(nameInput)
                        showNameEdit = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SystemPrimary)
                ) {
                    Text("SAVE", color = SystemBg, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNameEdit = false }) {
                    Text("CANCEL", color = TextSecondary)
                }
            }
        )
    }

    // Target Post Picker Dialog
    if (showPostPicker) {
        AlertDialog(
            onDismissRequest = { showPostPicker = false },
            containerColor = SystemSurface,
            title = { Text("Select Target SSC CGL Post", color = SystemPurple, fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 350.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SSC_TARGET_POSTS.size) { idx ->
                        val post = SSC_TARGET_POSTS[idx]
                        val isSelected = post.id == playerState.targetPostId
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setTargetPost(post.id)
                                    showPostPicker = false
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) SystemPurple.copy(alpha = 0.2f) else SystemSurfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SystemPurple else SystemBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(post.icon, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(post.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(post.ministry, color = TextSecondary, fontSize = 11.sp)
                                    Text("Cutoff: ${post.cutoffTarget}", color = SystemGold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPostPicker = false }) {
                    Text("CLOSE", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun MilestoneBox(
    title: String,
    value: String,
    icon: String,
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
            Text(icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, color = SystemPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(title, color = TextSecondary, fontSize = 10.sp)
        }
    }
}
