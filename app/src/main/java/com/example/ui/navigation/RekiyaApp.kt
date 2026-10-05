package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Job
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JobDetailScreen
import com.example.ui.screens.JobsScreen
import com.example.ui.screens.SavedJobsScreen
import com.example.ui.viewmodel.JobViewModel

enum class BottomNavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME("මුල් පිටුව", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
    JOBS("රැකියා", Icons.Filled.Search, Icons.Outlined.Search, "nav_jobs"),
    SAVED("සුරැකි රැකියා", Icons.Filled.Bookmark, Icons.Filled.BookmarkBorder, "nav_saved"),
    ACCOUNT("ගිණුම", Icons.Filled.Person, Icons.Outlined.Person, "nav_account")
}

sealed class Screen {
    data class Main(val tab: BottomNavTab = BottomNavTab.HOME) : Screen()
    data class JobDetail(val job: Job) : Screen()
    object Admin : Screen()
}

@Composable
fun RekiyaApp(
    viewModel: JobViewModel,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main(BottomNavTab.HOME)) }
    val savedJobs by viewModel.savedJobs.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    val currentTab = when (val s = currentScreen) {
        is Screen.Main -> s.tab
        else -> null
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (currentTab != null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    BottomNavTab.entries.forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                currentScreen = Screen.Main(tab)
                            },
                            icon = {
                                if (tab == BottomNavTab.SAVED && savedJobs.isNotEmpty()) {
                                    BadgedBox(
                                        badge = {
                                            Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                                Text("${savedJobs.size}")
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.title
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title
                                    )
                                }
                            },
                            label = { Text(tab.title) },
                            modifier = Modifier.testTag(tab.testTag),
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Main -> {
                    when (screen.tab) {
                        BottomNavTab.HOME -> {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToJobDetail = { job ->
                                    currentScreen = Screen.JobDetail(job)
                                },
                                onNavigateToJobsTab = {
                                    currentScreen = Screen.Main(BottomNavTab.JOBS)
                                }
                            )
                        }
                        BottomNavTab.JOBS -> {
                            JobsScreen(
                                viewModel = viewModel,
                                onNavigateToJobDetail = { job ->
                                    currentScreen = Screen.JobDetail(job)
                                }
                            )
                        }
                        BottomNavTab.SAVED -> {
                            SavedJobsScreen(
                                viewModel = viewModel,
                                onNavigateToJobDetail = { job ->
                                    currentScreen = Screen.JobDetail(job)
                                },
                                onNavigateToJobs = {
                                    currentScreen = Screen.Main(BottomNavTab.JOBS)
                                }
                            )
                        }
                        BottomNavTab.ACCOUNT -> {
                            AccountScreen(
                                viewModel = viewModel,
                                onNavigateToAdmin = {
                                    currentScreen = Screen.Admin
                                }
                            )
                        }
                    }
                }
                is Screen.JobDetail -> {
                    JobDetailScreen(
                        job = screen.job,
                        onBack = {
                            currentScreen = Screen.Main(currentTab ?: BottomNavTab.HOME)
                        },
                        onToggleSave = {
                            viewModel.toggleSaveJob(screen.job)
                        }
                    )
                }
                is Screen.Admin -> {
                    AdminScreen(
                        viewModel = viewModel,
                        onBack = {
                            currentScreen = Screen.Main(BottomNavTab.ACCOUNT)
                        }
                    )
                }
            }
        }
    }
}
