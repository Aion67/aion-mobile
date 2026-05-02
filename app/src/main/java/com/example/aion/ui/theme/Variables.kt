package com.example.aion.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

object Variables {
    // Brand Colors (Constant across themes)
    val PrimaryBrand = Color(0xFF6750A4)
    val SuccessGreen = Color(0xFF4CAF50)
    val WarningRed = Color(0xFFF44336)
    val NeutralGray = Color(0xFFE0E0E0)
    val InstagramPink = Color(0xFFEC4899)
    val EmeraldGreen = Color(0xFF10B981)
    val SelectionBlue = Color(0xFF007BFF)
    
    // Default Light Theme Colors (Reference only, components should use MaterialTheme.colorScheme)
    val SchemesSurface = Color(0xFFFEF7FF)
    val SchemesOnSurface = Color(0xFF1D1B20)
    val SchemesSurfaceVariant = Color(0xFFE7E0EB)
    val SchemesOnSurfaceVariant = Color(0xFF49454F)
    val SchemesOutline = Color(0xFF79747E)
    val SchemesPrimaryContainer = Color(0xFFEADDFF)
    
    val SchemesSurfaceContainerHigh = Color(0xFFECE6F0)
    val SchemesSurfaceContainerHighest = Color(0xFFE6E0E9)
    val SchemesOnPrimaryContainer = Color(0xFF4F378A)
    val SchemesTertiaryContainer = Color(0xFFFFD8E4)
    val SchemesOnTertiaryContainer = Color(0xFF633B48)

    // Typography Sizes
    val StaticHeadlineSmallSize = 24.sp
    val StaticHeadlineSmallLineHeight = 32.sp
    
    val StaticLabelMediumSize = 12.sp
    val StaticLabelMediumLineHeight = 16.sp
    val StaticLabelMediumTracking = 0.5.sp

    val StaticTitleLargeSize = 22.sp
    val StaticTitleLargeLineHeight = 28.sp
    val StaticTitleLargeTracking = 0.sp

    val StaticTitleMediumSize = 16.sp
    val StaticTitleMediumLineHeight = 24.sp
    val StaticTitleMediumTracking = 0.15.sp
    
    val StaticLabelLargeSize = 14.sp
    val StaticLabelLargeLineHeight = 20.sp
    val StaticLabelLargeTracking = 0.1.sp

    val StaticTitleSmallSize = 14.sp
    val StaticTitleSmallLineHeight = 20.sp
    val StaticTitleSmallTracking = 0.1.sp

    val StaticBodyLargeSize = 16.sp
    val StaticBodyLargeLineHeight = 24.sp
    val StaticBodyLargeTracking = 0.5.sp

    val StaticBodyMediumSize = 14.sp
    val StaticBodyMediumLineHeight = 20.sp
    val StaticBodyMediumTracking = 0.25.sp
    val StaticBodySmallSize = 12.sp
    val StaticBodySmallLineHeight = 16.sp
    val StaticBodySmallTracking = 0.4.sp
    
    // Font Families
    val TitleFontFamily = FontFamily.Default
    val BodyFontFamily = FontFamily.Default
}
