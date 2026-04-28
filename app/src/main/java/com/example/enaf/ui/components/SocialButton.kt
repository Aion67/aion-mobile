package com.example.enaf.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.theme.EnafHeaderBorder

/**
 * Reusable social authentication button for sign-in flows.
 * Used in Auth and Onboarding screens for OAuth integration.
 *
 * @param text Button label (e.g., "Continue with Google")
 * @param onClick Callback when button is clicked
 * @param modifier Composable modifier
 */
@Composable
fun SocialButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, EnafHeaderBorder),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        // Placeholder for icon (can be replaced with actual provider icons)
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    RoundedCornerShape(4.dp)
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
