package com.sololeveling.sscprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.sololeveling.sscprep.domain.model.SYSTEM_SHOP_ITEMS
import com.sololeveling.sscprep.domain.model.ShopItem
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun ShopScreen(viewModel: MainViewModel) {
    val playerState by viewModel.playerState.collectAsState()
    val inventoryState by viewModel.inventoryState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Vault Gold Header
        item {
            SystemWindowCard(borderColor = SystemGold, glowEffect = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "[SYSTEM SHOP]",
                            color = SystemGold,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "HUNTER ITEM VAULT",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SystemSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("💎", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${playerState.gold} GOLD",
                                color = SystemGold,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = SystemBorder)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    InventoryStatusBadge("Streak Shields", "${inventoryState.streakShields} Active", "🛡️", Modifier.weight(1f))
                    InventoryStatusBadge(
                        "Ruler's Scroll",
                        if (inventoryState.hasRulersAuthority) "UNLOCKED ⚡" else "LOCKED",
                        "📜",
                        Modifier.weight(1f)
                    )
                }
            }
        }

        // Shop Items List
        items(SYSTEM_SHOP_ITEMS.size) { idx ->
            val item = SYSTEM_SHOP_ITEMS[idx]
            ShopItemCard(
                item = item,
                canAfford = playerState.gold >= item.price,
                onBuy = { viewModel.buyItem(item.id) }
            )
        }
    }
}

@Composable
fun ShopItemCard(
    item: ShopItem,
    canAfford: Boolean,
    onBuy: () -> Unit
) {
    SystemWindowCard(borderColor = SystemBorder) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(item.icon, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.type.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = SystemPrimary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = SystemSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, SystemGold.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "${item.price} 💎",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = SystemGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.desc,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))
        SoloGlowingButton(
            text = if (canAfford) "PURCHASE FOR ${item.price} GOLD" else "INSUFFICIENT GOLD (${item.price})",
            onClick = onBuy,
            modifier = Modifier.fillMaxWidth(),
            enabled = canAfford,
            containerColor = SystemGold,
            contentColor = SystemBg
        )
    }
}

@Composable
fun InventoryStatusBadge(
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
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(title, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                Text(value, color = SystemPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}
