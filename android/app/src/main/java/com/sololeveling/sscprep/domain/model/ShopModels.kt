package com.sololeveling.sscprep.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ShopItem(
    val id: String,
    val name: String,
    val icon: String,
    val price: Int,
    val desc: String,
    val type: String
)

val SYSTEM_SHOP_ITEMS = listOf(
    ShopItem(
        id = "item_focus_elixir",
        name = "Elixir of Absolute Focus",
        icon = "🧪",
        price = 40,
        desc = "Grants 25-minute enhanced Pomodoro Focus session with Binaural Beats and +50 EXP upon completion.",
        type = "consumable"
    ),
    ShopItem(
        id = "item_streak_shield",
        name = "Streak Preservation Crystal",
        icon = "💎",
        price = 100,
        desc = "Protects your Daily Quest streak from breaking if you miss studying for a single day.",
        type = "utility"
    ),
    ShopItem(
        id = "item_rulers_scroll",
        name = "Ruler's Authority Formula Scroll",
        icon = "📜",
        price = 80,
        desc = "Unlocks permanent instant formula cheat sheets and trick shortcuts inside Mock Test Boss battles.",
        type = "relic"
    ),
    ShopItem(
        id = "item_title_conqueror",
        name = "Title: \"Monarch of Syllabus\"",
        icon = "👑",
        price = 250,
        desc = "Equip an illustrious title shown in your Hunter Status card and gain +10% Gold from all Raids.",
        type = "title"
    )
)

@Serializable
data class InventoryState(
    val purchasedItemIds: List<String> = emptyList(),
    val streakShields: Int = 1,
    val hasRulersAuthority: Boolean = false
)
