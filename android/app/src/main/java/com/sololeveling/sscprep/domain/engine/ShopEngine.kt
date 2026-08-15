package com.sololeveling.sscprep.domain.engine

import com.sololeveling.sscprep.domain.model.InventoryState
import com.sololeveling.sscprep.domain.model.PlayerState
import com.sololeveling.sscprep.domain.model.SYSTEM_SHOP_ITEMS

object ShopEngine {

    data class PurchaseResult(
        val updatedInventory: InventoryState,
        val updatedPlayerState: PlayerState,
        val message: String,
        val success: Boolean
    )

    fun purchaseItem(
        itemId: String,
        inventory: InventoryState,
        playerState: PlayerState
    ): PurchaseResult {
        val item = SYSTEM_SHOP_ITEMS.find { it.id == itemId }
            ?: return PurchaseResult(inventory, playerState, "Item not found in Vault.", false)

        if (playerState.gold < item.price) {
            return PurchaseResult(inventory, playerState, "Insufficient Gold Crystals! Raid more gates.", false)
        }

        val updatedPlayerGold = playerState.gold - item.price
        var updatedPlayer = playerState.copy(gold = updatedPlayerGold)
        var updatedInventory = inventory

        when (itemId) {
            "item_streak_shield" -> {
                updatedInventory = updatedInventory.copy(streakShields = updatedInventory.streakShields + 1)
            }
            "item_rulers_scroll" -> {
                updatedInventory = updatedInventory.copy(hasRulersAuthority = true)
            }
            "item_title_conqueror" -> {
                updatedPlayer = updatedPlayer.copy(title = "Monarch of Syllabus")
            }
            else -> {
                val list = updatedInventory.purchasedItemIds.toMutableList()
                list.add(itemId)
                updatedInventory = updatedInventory.copy(purchasedItemIds = list)
            }
        }

        return PurchaseResult(
            updatedInventory = updatedInventory,
            updatedPlayerState = updatedPlayer,
            message = "Acquired ${item.name} successfully!",
            success = true
        )
    }
}
