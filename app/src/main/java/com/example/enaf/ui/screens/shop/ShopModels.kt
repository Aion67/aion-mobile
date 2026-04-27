package com.example.enaf.ui.screens.shop

import kotlin.collections.emptyList
import kotlin.collections.listOf

enum class ShopCurrency {
    COINS,
    DIAMONDS,
}

data class ShopItemUiModel(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val currency: ShopCurrency,
    val isAffordable: Boolean,
)

data class ShopUiState(
    val coins: Int = 0,
    val diamonds: Int = 0,
    val items: List<ShopItemUiModel> = emptyList(),
    val statusMessage: String = "",
    val isLoading: Boolean = true,
)

sealed interface ShopUiEvent {
    data class BuyClicked(val itemId: String) : ShopUiEvent
    data object Refresh : ShopUiEvent
}

fun shopPreviewState(): ShopUiState {
    return ShopUiState(
        coins = 1240,
        diamonds = 36,
        items = listOf(
            ShopItemUiModel(
                id = "mindset-scroll",
                title = "Mindset Scroll",
                description = "Unlock a daily discipline quote pack.",
                price = 300,
                currency = ShopCurrency.COINS,
                isAffordable = true,
            ),
            ShopItemUiModel(
                id = "void-grid-theme",
                title = "Ambush Theme: Void Grid",
                description = "Custom overlay visuals for distraction interrupts.",
                price = 12,
                currency = ShopCurrency.DIAMONDS,
                isAffordable = true,
            ),
            ShopItemUiModel(
                id = "streak-shield",
                title = "Streak Shield",
                description = "Protect your streak for one failed day.",
                price = 800,
                currency = ShopCurrency.COINS,
                isAffordable = true,
            ),
        ),
        statusMessage = "Spend smart. Boosters are permanent once unlocked.",
        isLoading = false,
    )
}
