package com.example.enaf.ui.screens.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.local.entity.WalletLedgerEntity
import com.example.enaf.data.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null
    private var walletLedgerId: String? = null

    init {
        observeActiveSession()
    }

    fun onEvent(event: ShopUiEvent) {
        when (event) {
            is ShopUiEvent.BuyClicked -> buyItem(event.itemId)
            ShopUiEvent.Refresh -> refresh()
        }
    }

    private fun observeActiveSession() {
        viewModelScope.launch {
            localRepository.observeActiveSession().collect { session ->
                val userId = session?.userId
                if (userId == null) {
                    _uiState.value = ShopUiState(
                        items = buildShopCatalog(coins = 0, diamonds = 0),
                        statusMessage = "No active profile. Start a guest session to earn rewards.",
                        isLoading = false,
                    )
                    return@collect
                }

                currentUserId = userId
                loadShopState(userId)
            }
        }
    }

    private fun refresh() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            loadShopState(userId)
        }
    }

    private fun buyItem(itemId: String) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            val state = _uiState.value
            val item = state.items.firstOrNull { it.id == itemId } ?: return@launch

            if (item.currency == ShopCurrency.COINS && state.coins < item.price) {
                _uiState.value = state.copy(statusMessage = "Not enough coins for ${item.title}.")
                return@launch
            }
            if (item.currency == ShopCurrency.DIAMONDS && state.diamonds < item.price) {
                _uiState.value = state.copy(statusMessage = "Not enough diamonds for ${item.title}.")
                return@launch
            }

            val updatedCoins = if (item.currency == ShopCurrency.COINS) {
                state.coins - item.price
            } else {
                state.coins
            }
            val updatedDiamonds = if (item.currency == ShopCurrency.DIAMONDS) {
                state.diamonds - item.price
            } else {
                state.diamonds
            }
            val ledgerId = walletLedgerId ?: "wallet-$userId"

            localRepository.upsertWalletLedger(
                WalletLedgerEntity(
                    id = ledgerId,
                    userId = userId,
                    coinsBalanceCache = updatedCoins,
                    diamondsBalanceCache = updatedDiamonds,
                    updatedAtEpochMillis = System.currentTimeMillis(),
                )
            )

            walletLedgerId = ledgerId
            _uiState.value = state.copy(
                coins = updatedCoins,
                diamonds = updatedDiamonds,
                items = buildShopCatalog(updatedCoins, updatedDiamonds),
                statusMessage = "Purchased ${item.title}.",
            )
        }
    }

    private suspend fun loadShopState(userId: String) {
        val wallet = localRepository.getWalletLedger(userId)
        val coins = wallet?.coinsBalanceCache ?: 0
        val diamonds = wallet?.diamondsBalanceCache ?: 0
        walletLedgerId = wallet?.id ?: "wallet-$userId"

        _uiState.value = ShopUiState(
            coins = coins,
            diamonds = diamonds,
            items = buildShopCatalog(coins, diamonds),
            statusMessage = "Spend smart. Boosters are permanent once unlocked.",
            isLoading = false,
        )
    }

    private fun buildShopCatalog(coins: Int, diamonds: Int): List<ShopItemUiModel> {
        return listOf(
            ShopItemUiModel(
                id = "mindset-scroll",
                title = "Mindset Scroll",
                description = "Unlock a daily discipline quote pack.",
                price = 300,
                currency = ShopCurrency.COINS,
                isAffordable = coins >= 300,
            ),
            ShopItemUiModel(
                id = "void-grid-theme",
                title = "Ambush Theme: Void Grid",
                description = "Custom overlay visuals for distraction interrupts.",
                price = 12,
                currency = ShopCurrency.DIAMONDS,
                isAffordable = diamonds >= 12,
            ),
            ShopItemUiModel(
                id = "streak-shield",
                title = "Streak Shield",
                description = "Protect your streak for one failed day.",
                price = 800,
                currency = ShopCurrency.COINS,
                isAffordable = coins >= 800,
            ),
        )
    }
}
