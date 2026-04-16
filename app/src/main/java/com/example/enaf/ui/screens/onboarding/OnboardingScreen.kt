package com.example.enaf.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.components.EnafButton
import com.example.enaf.ui.theme.*

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EnafDarkBg)
    ) {
        // Background Kinetic Light Elements (Glows)
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-50).dp, y = (-50).dp)
                .blur(100.dp)
                .background(EnafActionBlue.copy(alpha = 0.15f), RoundedCornerShape(150.dp))
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 50.dp)
                .blur(100.dp)
                .background(EnafPink.copy(alpha = 0.1f), RoundedCornerShape(125.dp))
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onSkipClick) {
                    Text(
                        text = "Skip",
                        color = EnafTextSecondary,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            // Illustration Placeholder
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .background(EnafHeaderBg.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    .border(1.dp, EnafHeaderBorder, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Glow behind image
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .blur(40.dp)
                        .background(EnafActionBlue.copy(alpha = 0.2f), RoundedCornerShape(100.dp))
                )
                Text(
                    text = "Digital Freedom\nIllustration",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))

            // Typography Content
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reclaim Your Focus",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Experience the pulse of futuristic wellness and break free from digital noise.",
                    color = EnafTextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Carousel Indicators (Static for now)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(width = 32.dp, height = 6.dp).background(EnafActionBlue, RoundedCornerShape(3.dp)))
                Box(modifier = Modifier.size(8.dp).background(EnafProgressBg, CircleShape))
                Box(modifier = Modifier.size(8.dp).background(EnafProgressBg, CircleShape))
            }

            Spacer(modifier = Modifier.weight(0.2f))

            // CTA Button
            EnafButton(
                text = "Get Started",
                onClick = onGetStartedClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Already have an account? Log in",
                color = EnafTextMuted,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    EnafTheme(darkTheme = true) {
        OnboardingScreen()
    }
}
