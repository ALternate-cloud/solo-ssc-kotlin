package com.sololeveling.sscprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.sync.SyncStatus
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.AuthViewModel
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    authViewModel: AuthViewModel,
    onAccountDeleted: () -> Unit = {}
) {
    val playerState by viewModel.playerState.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()

    var soundEnabled by remember { mutableStateOf(viewModel.soundAndHaptics.isSoundEnabled) }
    var hapticsEnabled by remember { mutableStateOf(viewModel.soundAndHaptics.isHapticsEnabled) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showDevCheatDialog by remember { mutableStateOf(false) }
    var devTapCount by remember { mutableStateOf(0) }
    var isDeleting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Text("⚙️", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "SYSTEM SETTINGS",
                    color = SystemPrimary,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleLarge,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Audio, cloud sync and hunter account controls",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Section 1: Audio & Haptics
        SystemWindowCard(borderColor = SystemPrimary) {
            Text(
                text = "AUDIO & HAPTIC FEEDBACK",
                color = SystemPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Sound Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Sound Effects & Chords", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Text("Button clicks, level up chords, and ARISE audio", color = TextSecondary, fontSize = 12.sp)
                }
                Switch(
                    checked = soundEnabled,
                    onCheckedChange = {
                        soundEnabled = it
                        viewModel.soundAndHaptics.isSoundEnabled = it
                        if (it) viewModel.soundAndHaptics.playClick()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SystemPrimary,
                        checkedTrackColor = SystemPrimary.copy(alpha = 0.3f),
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = SystemSurfaceElevated
                    )
                )
            }

            HorizontalDivider(color = SystemBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))

            // Haptic Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Haptic Vibrations", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Text("Tactile feedback on taps, damage, and level-ups", color = TextSecondary, fontSize = 12.sp)
                }
                Switch(
                    checked = hapticsEnabled,
                    onCheckedChange = {
                        hapticsEnabled = it
                        viewModel.soundAndHaptics.isHapticsEnabled = it
                        if (it) viewModel.soundAndHaptics.triggerHaptic("medium")
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SystemPrimary,
                        checkedTrackColor = SystemPrimary.copy(alpha = 0.3f),
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = SystemSurfaceElevated
                    )
                )
            }
        }

        // Section 2: Cloud Sync Status
        SystemWindowCard(borderColor = SystemPurple) {
            Text(
                text = "HUNTER CLOUD SYNC",
                color = SystemPurple,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Cloud Connection", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    val statusText = when (syncStatus) {
                        SyncStatus.SYNCED -> "Cloud Synced & Live 🟢"
                        SyncStatus.SYNCING -> "Synchronizing progress... 🔄"
                        SyncStatus.OFFLINE -> "Offline Mode 🟡"
                        SyncStatus.ERROR -> "Connection Error 🔴"
                        SyncStatus.IDLE -> "Connected 🟢"
                    }
                    Text(statusText, color = TextSecondary, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        viewModel.pullAndApplyCloudState()
                        viewModel.showBanner("Cloud synchronization refreshed!")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SystemPurple.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SystemPurple)
                ) {
                    Text("SYNC NOW", color = SystemPurple, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Section 3: App Information
        SystemWindowCard(borderColor = SystemBorder) {
            Text(
                text = "SYSTEM INFORMATION",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Application", color = TextSecondary)
                Text("Solo Leveling: SSC Prep", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        devTapCount++
                        if (devTapCount >= 5) {
                            devTapCount = 0
                            viewModel.soundAndHaptics.playLevelUp()
                            showDevCheatDialog = true
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Version", color = TextSecondary)
                Text("${com.sololeveling.sscprep.BuildConfig.VERSION_NAME} (Build ${com.sololeveling.sscprep.BuildConfig.VERSION_CODE})", color = SystemPrimary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Hunter ID", color = TextSecondary)
                Text(playerState.name, color = TextPrimary, fontWeight = FontWeight.Medium)
            }
        }

        // Section 4: Danger Zone (Permanent Account Deletion)
        SystemWindowCard(borderColor = SystemCrimson) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚠️", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "DANGER ZONE",
                    color = SystemCrimson,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Permanently purge your hunter account, level progress, shadows, and rank from the online database. This action cannot be reversed.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = SystemCrimson,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (isDeleting) {
                CircularProgressIndicator(
                    color = SystemCrimson,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Button(
                    onClick = { showDeleteConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SystemCrimson),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null, tint = TextPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PERMANENTLY DELETE ACCOUNT",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Confirmation Alert Dialog for Account Deletion
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = SystemSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚠️", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "DELETE ACCOUNT?",
                        color = SystemCrimson,
                        fontWeight = FontWeight.Black
                    )
                }
            },
            text = {
                Text(
                    text = "Are you completely sure you want to permanently delete your Hunter profile? All your stats, gold, shadows, and mock test records will be erased forever.",
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        isDeleting = true
                        errorMessage = null
                        authViewModel.deleteAccount(
                            onSuccess = {
                                isDeleting = false
                                onAccountDeleted()
                            },
                            onError = { err ->
                                isDeleting = false
                                errorMessage = err
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SystemCrimson)
                ) {
                    Text("YES, DELETE EVERYTHING", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("CANCEL", color = TextSecondary)
                }
            }
        )
    }

    var devPasscodeInput by remember { mutableStateOf("") }
    var isDevKeyUnlocked by remember { mutableStateOf(false) }
    var devKeyError by remember { mutableStateOf<String?>(null) }

    // Secret Developer God-Mode Dialog (Protected by monarch2026 Master Key)
    if (showDevCheatDialog) {
        AlertDialog(
            onDismissRequest = {
                showDevCheatDialog = false
                devPasscodeInput = ""
                devKeyError = null
            },
            containerColor = SystemSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👑", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isDevKeyUnlocked) "ARCHITECT GOD CONSOLE" else "DEVELOPER CLEARANCE",
                        color = SystemGold,
                        fontWeight = FontWeight.Black
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (!isDevKeyUnlocked) {
                        Text(
                            text = "Enter the Architect Master Key to access developer debug and testing controls:",
                            color = TextPrimary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedTextField(
                            value = devPasscodeInput,
                            onValueChange = {
                                devPasscodeInput = it
                                devKeyError = null
                            },
                            label = { Text("Architect Master Key", color = TextSecondary) },
                            singleLine = true,
                            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SystemGold,
                                unfocusedBorderColor = SystemBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        if (devKeyError != null) {
                            Text(
                                text = devKeyError!!,
                                color = SystemCrimson,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(
                            text = "👑 Clearance Granted: Welcome Creator. Select a Developer Command:",
                            color = SystemGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Button(
                            onClick = {
                                viewModel.activateGodMode()
                                showDevCheatDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SystemPurple),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("⚡ LEVEL 100 MONARCH (+999k Gold, +500 Stats)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.recordDemonTowerFloor(100, 15, 5000)
                                showDevCheatDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SystemPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("🗼 UNLOCK ALL 100 DEMON CASTLE FLOORS", color = SystemBg, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                for (i in 1..10) { viewModel.recordArenaMatch(won = true) }
                                showDevCheatDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SystemCrimson),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("⚔️ BOOST ARENA ELO (+2500 Monarch Sovereign)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                if (!isDevKeyUnlocked) {
                    Button(
                        onClick = {
                            if (devPasscodeInput.trim() == "monarch2026") {
                                isDevKeyUnlocked = true
                                devKeyError = null
                                viewModel.soundAndHaptics.playLevelUp()
                            } else {
                                devKeyError = "⛔ Access Denied: Invalid Security Clearance"
                                viewModel.soundAndHaptics.playAlert()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SystemGold)
                    ) {
                        Text("AUTHENTICATE ⚡", color = SystemBg, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDevCheatDialog = false
                    devPasscodeInput = ""
                    devKeyError = null
                }) {
                    Text("CLOSE", color = TextSecondary)
                }
            }
        )
    }
}
