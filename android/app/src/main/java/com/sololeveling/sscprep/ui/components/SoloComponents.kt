package com.sololeveling.sscprep.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.ui.theme.*

@Composable
fun SystemWindowCard(
    modifier: Modifier = Modifier,
    borderColor: Color = SystemBorder,
    glowEffect: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = if (glowEffect) {
                    Brush.linearGradient(listOf(SystemPrimary, SystemPurple, SystemBorder))
                } else {
                    Brush.linearGradient(listOf(borderColor, borderColor.copy(alpha = 0.4f)))
                },
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SystemSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            content = content
        )
    }
}

@Composable
fun StatProgressBar(
    label: String,
    current: Int,
    max: Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = if (max > 0) (current.toFloat() / max.toFloat()).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "stat_progress")

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = barColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$current / $max",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SystemSurfaceElevated)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(barColor.copy(alpha = 0.7f), barColor)
                        )
                    )
            )
        }
    }
}

@Composable
fun RankBadgeChip(
    rank: String,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (rank.uppercase()) {
        "E" -> Pair(Color(0xFF94A3B8), "E-RANK")
        "D" -> Pair(Color(0xFF4ADE80), "D-RANK")
        "C" -> Pair(Color(0xFF38BDF8), "C-RANK")
        "B" -> Pair(Color(0xFFC084FC), "B-RANK")
        "A" -> Pair(Color(0xFFFB923C), "A-RANK")
        "S" -> Pair(Color(0xFFFDE047), "S-RANK")
        "MONARCH" -> Pair(Color(0xFFC084FC), "MONARCH")
        else -> Pair(SystemPrimary, rank)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.6f))
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun SoloGlowingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = SystemPrimary,
    contentColor: Color = SystemBg,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(46.dp)
            .border(
                1.dp,
                if (enabled) containerColor.copy(alpha = 0.8f) else Color.Transparent,
                RoundedCornerShape(8.dp)
            ),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = SystemSurfaceElevated,
            disabledContentColor = TextMuted
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
fun StatAllocationRow(
    statName: String,
    statAbbr: String,
    statValue: Int,
    statDescription: String,
    pointsAvailable: Int,
    onAllocate: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = statAbbr,
                    color = SystemPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = statName,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
            Text(
                text = statDescription,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$statValue",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            IconButton(
                onClick = onAllocate,
                enabled = pointsAvailable > 0,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (pointsAvailable > 0) SystemPrimary else SystemSurfaceElevated)
            ) {
                Text(
                    text = "+",
                    color = if (pointsAvailable > 0) SystemBg else TextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
