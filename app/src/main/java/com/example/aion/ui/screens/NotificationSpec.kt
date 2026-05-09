package com.example.aion.ui.screens

import androidx.annotation.DrawableRes
import com.example.aion.R

data class NotificationSpec(
    val id: Long,
    val appName: String,
    val title: String,
    val message: String,
    val timestamp: String,
    @param:DrawableRes val iconRes: Int,
    val isRead: Boolean = false,
)

val sampleNotifications = listOf(
    NotificationSpec(1, "Instagram", "Instagram Limit Reached", "You hit your daily limit for Instagram.", "12:30", R.drawable.tiktok, isRead = false),
    NotificationSpec(2, "TikTok", "TikTok Limit Reached", "You've reached your TikTok limit today.", "11:15", R.drawable.tiktok, isRead = false),
    NotificationSpec(3, "YouTube", "YouTube Suggestion", "Try reducing autoplay to save time.", "09:42", R.drawable.tiktok, isRead = true),
)
