package com.example.aion.ui.screens

import androidx.annotation.DrawableRes
import com.example.aion.R

data class ProfileSpec(
    val displayName: String,
    val username: String,
    @DrawableRes val avatarRes: Int,
    val email: String,
    val bio: String,
)

val sampleProfile = ProfileSpec(
    displayName = "Alex Morgan",
    username = "@alexm",
    avatarRes = R.drawable.tiktok,
    email = "alex@example.com",
    bio = "Keeping focus intentional, one app at a time.",
)