package com.sololeveling.sscprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.domain.model.HunterLeaderboardEntry
import com.sololeveling.sscprep.domain.model.SAMPLE_HUNTER_LEADERBOARD
import com.sololeveling.sscprep.ui.components.RankBadgeChip
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*

@Composable
fun LeaderboardScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            SystemWindowCard(borderColor = SystemGold, glowEffect = true) {
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "National ranking of top candidates preparing for SSC CGL Tier-1 & Tier-2 examinations.",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        items(SAMPLE_HUNTER_LEADERBOARD.size) { idx ->
            val entry = SAMPLE_HUNTER_LEADERBOARD[idx]
            LeaderboardItemCard(entry = entry)
        }
    }
}

@Composable
fun LeaderboardItemCard(entry: HunterLeaderboardEntry) {
    val rankColor = when (entry.rankPosition) {
        1 -> SystemGold
        2 -> Color(0xFFE2E8F0)
        3 -> Color(0xFFCD7F32)
        else -> SystemBorder
    }

    SystemWindowCard(borderColor = rankColor) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (entry.rankPosition <= 3) rankColor.copy(alpha = 0.2f) else SystemSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, rankColor)
                ) {
                    Text(
                        text = "#${entry.rankPosition}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        color = if (entry.rankPosition <= 3) rankColor else TextSecondary,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = entry.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Target: ${entry.postTarget}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SystemPrimary
                    )
                }
            }

            RankBadgeChip(rank = entry.hunterRank)
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Lvl ${entry.level}", color = SystemGold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
            Text("${entry.questionsSolved} Solved", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
            Text("${entry.accuracy}% Accuracy", color = SystemSuccess, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
        }
    }
}
