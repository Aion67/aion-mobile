package com.example.enaf.ui.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.components.EnafTopAppBar
import com.example.enaf.ui.theme.EnafActionBlue
import com.example.enaf.ui.theme.EnafCardBg
import com.example.enaf.ui.theme.EnafDarkBg
import com.example.enaf.ui.theme.EnafHeaderBorder
import com.example.enaf.ui.theme.EnafTextMuted
import com.example.enaf.ui.theme.EnafTextSecondary
import com.example.enaf.ui.theme.EnafTheme

@Composable
fun ShopScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { EnafTopAppBar() },
        containerColor = EnafDarkBg
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Warrior Shop",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                WalletCard(coins = 1240, diamonds = 36)
            }

            item {
                Text(
                    text = "Focus Boosters",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ShopItemCard(
                        title = "Mindset Scroll",
                        description = "Unlock a daily discipline quote pack.",
                        price = "300 coins"
                    )
                    ShopItemCard(
                        title = "Ambush Theme: Void Grid",
                        description = "Custom overlay visuals for distraction interrupts.",
                        price = "12 diamonds"
                    )
                    ShopItemCard(
                        title = "Streak Shield",
                        description = "Protect your streak for one failed day.",
                        price = "800 coins"
                    )
                }
            }
        }
    }
}

@Composable
private fun WalletCard(coins: Int, diamonds: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(14.dp))
            .border(1.dp, EnafHeaderBorder, RoundedCornerShape(14.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Balance", color = EnafTextMuted, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("$coins Coins", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("$diamonds Diamonds", color = EnafTextSecondary, fontSize = 14.sp)
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(EnafActionBlue.copy(alpha = 0.14f), RoundedCornerShape(12.dp))
        )
    }
}

@Composable
private fun ShopItemCard(
    title: String,
    description: String,
    price: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(14.dp))
            .border(1.dp, EnafHeaderBorder, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        Text(text = description, color = EnafTextSecondary, fontSize = 13.sp, lineHeight = 18.sp)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = price, color = EnafActionBlue, fontWeight = FontWeight.Bold)
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = EnafActionBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Buy", color = Color.White)
            }
        }
    }
}

@Preview
@Composable
private fun ShopScreenPreview() {
    EnafTheme(darkTheme = true) {
        ShopScreen()
    }
}
