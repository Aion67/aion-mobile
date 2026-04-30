package com.example.aion.ui.screens

import androidx.annotation.DrawableRes

data class AppDetailSpec(
    val appName: String,
    @DrawableRes val iconRes: Int,
    val creditScore: String,
    val usedTime: String,
    val remainingTime: String,
    val progress: Float,
)
