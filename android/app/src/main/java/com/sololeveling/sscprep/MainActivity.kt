package com.sololeveling.sscprep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.network.ApiClient
import com.sololeveling.sscprep.ui.components.RankBadgeChip
import com.sololeveling.sscprep.ui.screens.*
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.AuthState
import com.sololeveling.sscprep.ui.viewmodel.AuthViewModel
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiClient.init(applicationContext)

        setContent {
            SoloLevelingTheme {
                val authState by authViewModel.authState.collectAsState()

                when (authState) {
                    is AuthState.Loading -> {
                        // Splash / loading state
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SystemBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("⚔️", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "SYSTEM INITIALIZING...",
                                    color = SystemPrimary,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                CircularProgressIndicator(color = SystemPrimary)
                            }
                        }
                    }
                    is AuthState.LoggedOut, is AuthState.Error -> {
                        // Auth flow
                        var showRegister by remember { mutableStateOf(false) }
                        if (showRegister) {
                            RegisterScreen(
                                authViewModel = authViewModel,
                                onNavigateToLogin = { showRegister = false },
                                onRegisterSuccess = {
                                    viewModel.pullAndApplyCloudState()
                                }
                            )
                        } else {
                            LoginScreen(
                                authViewModel = authViewModel,
                                onNavigateToRegister = { showRegister = true },
                                onLoginSuccess = {
                                    viewModel.pullAndApplyCloudState()
                                }
                            )
                        }
                    }
                    is AuthState.LoggedIn -> {
                        MainAppScaffold(
                            viewModel = viewModel,
                            onLogout = { authViewModel.logout() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(viewModel: MainViewModel, onLogout: () -> Unit = {}) {
    var currentScreen by remember { mutableStateOf("status") }
    var showGuildDrawer by remember { mutableStateOf(false) }

    val activeRaid by viewModel.activeRaidSession.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    val bannerMessage by viewModel.systemBannerMessage.collectAsState()

    // If a raid session is currently running, jump straight to CBT screen
    LaunchedEffect(activeRaid) {
        if (activeRaid != null && !activeRaid!!.isCompleted) {
            currentScreen = "raid"
        }
    }

    Scaffold(
        topBar = {
            if (currentScreen != "raid") {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚡", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SOLO LEVELING",
                                style = MaterialTheme.typography.titleLarge,
                                color = SystemPrimary,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    },
                    actions = {
                        RankBadgeChip(rank = playerState.rank)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { showGuildDrawer = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Guild Menu", tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SystemSurface,
                        titleContentColor = TextPrimary
                    )
                )
            }
        },
        bottomBar = {
            if (currentScreen != "raid") {
                NavigationBar(
                    containerColor = SystemSurface,
                    contentColor = TextPrimary
                ) {
                    NavigationBarItem(
                        selected = currentScreen == "status",
                        onClick = { currentScreen = "status" },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Status") },
                        label = { Text("Status", style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SystemPrimary,
                            selectedTextColor = SystemPrimary,
                            indicatorColor = SystemPrimary.copy(alpha = 0.2f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen == "quests",
                        onClick = { currentScreen = "quests" },
                        icon = { Icon(Icons.Default.Assignment, contentDescription = "Quests") },
                        label = { Text("Quests", style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SystemPurple,
                            selectedTextColor = SystemPurple,
                            indicatorColor = SystemPurple.copy(alpha = 0.2f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen == "vault",
                        onClick = { currentScreen = "vault" },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = "Vault") },
                        label = { Text("Vault", style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SystemGold,
                            selectedTextColor = SystemGold,
                            indicatorColor = SystemGold.copy(alpha = 0.2f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen == "dungeons",
                        onClick = { currentScreen = "dungeons" },
                        icon = { Icon(Icons.Default.Shield, contentDescription = "Dungeons") },
                        label = { Text("Raids", style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SystemCrimson,
                            selectedTextColor = SystemCrimson,
                            indicatorColor = SystemCrimson.copy(alpha = 0.2f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        },
        containerColor = SystemBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Screen Router
            when (currentScreen) {
                "status" -> StatusScreen(
                    viewModel = viewModel,
                    onNavigateToQuests = { currentScreen = "quests" },
                    onNavigateToDungeons = { currentScreen = "dungeons" }
                )
                "quests" -> QuestsScreen(
                    viewModel = viewModel,
                    onStartPomodoro = { currentScreen = "focus" }
                )
                "vault" -> VaultScreen(viewModel = viewModel)
                "dungeons" -> DungeonsScreen(
                    viewModel = viewModel,
                    onEnterRaid = { currentScreen = "raid" }
                )
                "raid" -> CbtRaidScreen(
                    viewModel = viewModel,
                    onExitRaid = { currentScreen = "dungeons" }
                )
                "shadows" -> ShadowArmyScreen(viewModel = viewModel)
                "focus" -> FocusScreen(viewModel = viewModel)
                "shop" -> ShopScreen(viewModel = viewModel)
                "syllabus" -> SyllabusScreen()
                "leaderboard" -> LeaderboardScreen(viewModel = viewModel)
            }

            // System Alert Notification Banner
            AnimatedVisibility(
                visible = bannerMessage != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                bannerMessage?.let { msg ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SystemSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SystemPrimary),
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚡", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = msg,
                                color = TextPrimary,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // Guild Navigation Menu Bottom Sheet
    if (showGuildDrawer) {
        ModalBottomSheet(
            onDismissRequest = { showGuildDrawer = false },
            containerColor = SystemSurface,
            contentColor = TextPrimary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "HUNTER GUILD DIRECTORY",
                    style = MaterialTheme.typography.titleMedium,
                    color = SystemPrimary,
                    fontWeight = FontWeight.Black
                )
                HorizontalDivider(color = SystemBorder)

                GuildMenuItem("Shadow Army & ARISE", "👥", "Mistake extraction & commanders", SystemPurple) {
                    currentScreen = "shadows"
                    showGuildDrawer = false
                }
                GuildMenuItem("Focus Sanctum", "🧘", "Pomodoro deep work study timer", SystemPrimary) {
                    currentScreen = "focus"
                    showGuildDrawer = false
                }
                GuildMenuItem("Hunter Item Shop", "💎", "Elixirs, streak shields, relics & titles", SystemGold) {
                    currentScreen = "shop"
                    showGuildDrawer = false
                }
                GuildMenuItem("Interactive Syllabus", "📋", "SSC CGL / CHSL topic weightage", SystemPrimary) {
                    currentScreen = "syllabus"
                    showGuildDrawer = false
                }
                GuildMenuItem("National Leaderboard", "🏆", "Top Aspirant Monarch rankings", SystemGold) {
                    currentScreen = "leaderboard"
                    showGuildDrawer = false
                }

                HorizontalDivider(color = SystemBorder)

                GuildMenuItem("Logout", "🚪", "Sign out of your hunter account", SystemCrimson) {
                    showGuildDrawer = false
                    onLogout()
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun GuildMenuItem(
    title: String,
    icon: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = SystemSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, SystemBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = accentColor, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
