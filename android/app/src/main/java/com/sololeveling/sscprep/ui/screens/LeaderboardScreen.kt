package com.sololeveling.sscprep.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.sololeveling.sscprep.network.ApiClient
import com.sololeveling.sscprep.network.LeaderboardEntryDto
import com.sololeveling.sscprep.ui.components.RankBadgeChip
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun LeaderboardScreen(viewModel: MainViewModel) {
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var leaderboard by remember { mutableStateOf<List<LeaderboardEntryDto>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    fun fetchLeaderboard() {
        coroutineScope.launch {
            isLoading = true
            error = null
            try {
                val response = ApiClient.apiService.getLeaderboard()
                if (response.success) {
                    leaderboard = response.leaderboard
                } else {
                    error = "Failed to fetch leaderboard"
                }
            } catch (e: Exception) {
                error = e.localizedMessage ?: "An error occurred"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchLeaderboard()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                SystemWindowCard(borderColor = SystemGold, glowEffect = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "[NATIONAL HUNTER RANKINGS]",
                                color = SystemGold,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "TOP SSC ASPIRANT MONARCHS",
                                style = MaterialTheme.typography.headlineMedium,
                                color = TextPrimary
                            )
                        }
                        IconButton(onClick = { fetchLeaderboard() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = SystemPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "National ranking of top candidates preparing for SSC CGL Tier-1 & Tier-2 examinations.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SystemPrimary)
                    }
                }
            } else if (error != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Error: $error",
                                color = SystemCrimson,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            SoloGlowingButton(
                                text = "RETRY",
                                onClick = { fetchLeaderboard() },
                                containerColor = SystemPrimary,
                                contentColor = SystemBg
                            )
                        }
                    }
                }
            } else {
                items(leaderboard) { entry ->
                    LeaderboardItemCard(entry = entry)
                }
            }
        }
    }
}

@Composable
fun LeaderboardItemCard(entry: LeaderboardEntryDto) {
    val rankColor = when (entry.rankPosition) {
        1 -> SystemGold
        2 -> Color(0xFFE2E8F0)
        3 -> Color(0xFFCD7F32)
        else -> SystemBorder
    }

    val rankText = when (entry.rankPosition) {
        1 -> "#1 🥇"
        2 -> "#2 🥈"
        3 -> "#3 🥉"
        else -> "#${entry.rankPosition}"
    }

    SystemWindowCard(borderColor = rankColor) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (entry.rankPosition <= 3) rankColor.copy(alpha = 0.2f) else SystemSurfaceElevated,
                    border = BorderStroke(1.dp, rankColor)
                ) {
                    Text(
                        text = rankText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        color = if (entry.rankPosition <= 3) rankColor else TextSecondary,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = entry.hunterName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = entry.title.ifEmpty { "Hunter" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = SystemPrimary
                    )
                }
            }

            RankBadgeChip(rank = entry.rank)
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Lvl ${entry.level}", color = SystemGold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
            Text("${entry.totalQuestionsSolved} Solved", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
            Text("${entry.mockTestsCleared} Cleared", color = SystemSuccess, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
        }
    }
}
