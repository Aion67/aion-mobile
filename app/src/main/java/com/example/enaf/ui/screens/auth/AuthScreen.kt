package com.example.enaf.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.components.EnafButton
import com.example.enaf.ui.components.EnafTextField
import com.example.enaf.ui.theme.*

enum class AuthMode {
    SIGN_UP, LOGIN
}

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit = {},
    initialMode: AuthMode = AuthMode.SIGN_UP
) {
    var mode by remember { mutableStateOf(initialMode) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EnafDarkBg)
    ) {
        // Background Decorations
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-40).dp, y = (-40).dp)
                .blur(80.dp)
                .background(EnafActionBlue.copy(alpha = 0.1f), RoundedCornerShape(100.dp))
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 40.dp)
                .blur(80.dp)
                .background(EnafPink.copy(alpha = 0.08f), RoundedCornerShape(100.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Logo Placeholder
            Text(
                text = "Enaf",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1.5).sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Heading
            Text(
                text = if (mode == AuthMode.SIGN_UP) "Join the Enaf journey" else "Welcome back",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Experience the pulse of futuristic wellness.",
                color = EnafTextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Form
            EnafTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                placeholder = "name@example.com",
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = EnafTextMuted, modifier = Modifier.size(20.dp))
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            EnafTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "••••••••",
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = EnafTextMuted, modifier = Modifier.size(20.dp))
                }
            )

            if (mode == AuthMode.LOGIN) {
                Text(
                    text = "Forgot Password?",
                    color = EnafActionBlue,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            EnafButton(
                text = if (mode == AuthMode.SIGN_UP) "Create Account" else "Sign In",
                onClick = onAuthSuccess
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = EnafHeaderBorder)
                Text(
                    text = "OR",
                    color = EnafTextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = EnafHeaderBorder)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Social Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SocialButton(
                    text = "Google",
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )
                SocialButton(
                    text = "Apple",
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Toggle Mode
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (mode == AuthMode.SIGN_UP) "Already have an account? " else "Don't have an account? ",
                    color = EnafTextMuted,
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = { mode = if (mode == AuthMode.SIGN_UP) AuthMode.LOGIN else AuthMode.SIGN_UP },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = if (mode == AuthMode.SIGN_UP) "Login" else "Sign Up",
                        color = EnafActionBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SocialButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, EnafHeaderBorder),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
    ) {
        // Placeholder for Icon
        Box(modifier = Modifier.size(20.dp).background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    EnafTheme(darkTheme = true) {
        AuthScreen()
    }
}
