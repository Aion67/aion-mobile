package com.example.aion.ui.screens

import androidx.annotation.DrawableRes
import com.example.aion.R

data class AppDetailSpec(
    val appName: String,
    @DrawableRes val iconRes: Int,
    val creditScore: String,
    val usedTime: String,
    val remainingTime: String,
    val progress: Float,
    val lastOpened: String = "13:08",
    val dataUsage: String = "34 MB",
    val notoriety: String = "HARD",
)

val sampleAppDetails = listOf(
    AppDetailSpec(
        appName = "Instagram",
        iconRes = R.drawable.tiktok,
        creditScore = "76.23",
        usedTime = "12h 35m",
        remainingTime = "1h 35m",
        progress = 0.8f,
        lastOpened = "13:08",
        dataUsage = "34 MB",
        notoriety = "HARD",
    ),
    AppDetailSpec(
        appName = "TikTok",
        iconRes = R.drawable.tiktok,
        creditScore = "72.50",
        usedTime = "10h 15m",
        remainingTime = "2h 05m",
        progress = 0.5f,
        lastOpened = "11:42",
        dataUsage = "29 MB",
        notoriety = "MODERATE",
    ),
    AppDetailSpec(
        appName = "YouTube",
        iconRes = R.drawable.tiktok,
        creditScore = "68.10",
        usedTime = "8h 50m",
        remainingTime = "3h 10m",
        progress = 0.2f,
        lastOpened = "09:21",
        dataUsage = "18 MB",
        notoriety = "LOW",
    ),
    AppDetailSpec(
        appName = "Snapchat",
        iconRes = R.drawable.tiktok,
        creditScore = "81.40",
        usedTime = "6h 20m",
        remainingTime = "4h 40m",
        progress = 0.9f,
        lastOpened = "15:03",
        dataUsage = "41 MB",
        notoriety = "HARD",
    ),
    AppDetailSpec(
        appName = "Facebook",
        iconRes = R.drawable.tiktok,
        creditScore = "64.75",
        usedTime = "7h 05m",
        remainingTime = "3h 55m",
        progress = 0.4f,
        lastOpened = "10:55",
        dataUsage = "22 MB",
        notoriety = "MODERATE",
    ),
)
