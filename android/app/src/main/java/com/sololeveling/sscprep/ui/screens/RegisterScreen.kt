package com.sololeveling.sscprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.AuthState
import com.sololeveling.sscprep.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authState) {
        if (authState is AuthState.LoggedIn) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "👑",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "HUNTER",
            color = SystemPurple,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 4.sp
        )
        Text(
            text = "AWAKENING",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                localError = null
                authViewModel.clearError()
            },
            label = { Text("Email") },
            placeholder = { Text("hunter@example.com", color = TextMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SystemPurple,
                unfocusedBorderColor = SystemBorder,
                focusedLabelColor = SystemPurple,
                unfocusedLabelColor = TextSecondary,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { 
                username = it
                localError = null
                authViewModel.clearError()
            },
            label = { Text("Choose Username") },
            placeholder = { Text("shadow_monarch", color = TextMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SystemPurple,
                unfocusedBorderColor = SystemBorder,
                focusedLabelColor = SystemPurple,
                unfocusedLabelColor = TextSecondary,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                localError = null
                authViewModel.clearError()
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SystemPurple,
                unfocusedBorderColor = SystemBorder,
                focusedLabelColor = SystemPurple,
                unfocusedLabelColor = TextSecondary,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = TextSecondary)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { 
                confirmPassword = it
                localError = null
                authViewModel.clearError()
            },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SystemPurple,
                unfocusedBorderColor = SystemBorder,
                focusedLabelColor = SystemPurple,
                unfocusedLabelColor = TextSecondary,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle confirm password visibility", tint = TextSecondary)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        val displayError = localError ?: (authState as? AuthState.Error)?.message

        if (displayError != null) {
            Text(
                text = displayError,
                color = SystemCrimson,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (authState is AuthState.Loading) {
            CircularProgressIndicator(
                color = SystemPurple,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        } else {
            SoloGlowingButton(
                text = "AWAKEN",
                onClick = {
                    when {
                        email.isBlank() || !email.contains("@") -> localError = "Please enter a valid email"
                        username.length < 3 -> localError = "Username must be at least 3 characters"
                        password.length < 4 -> localError = "Password must be at least 4 characters"
                        password != confirmPassword -> localError = "Passwords do not match"
                        else -> authViewModel.register(username, password, email)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                containerColor = SystemPurple
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Already Awakened? ", color = TextSecondary)
            Text(
                text = "Login",
                color = SystemPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { 
                    authViewModel.clearError()
                    onNavigateToLogin() 
                }
            )
        }
    }
}
