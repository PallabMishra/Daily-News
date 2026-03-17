package com.kmpnewsapp.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.kmpnewsapp.core.designsystem.theme.*
import com.kmpnewsapp.feature.map.MapScreen
import com.kmpnewsapp.feature.news.ui.HomeFeedScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        DashboardContent()
    }
}

@Composable
fun DashboardContent() {
    TabNavigator(HomeTab) {
        Scaffold(
            bottomBar = {
                Surface(
                    elevation = 0.dp,
                    color = MaterialTheme.colors.surface,
                    modifier = Modifier.height(72.dp).fillMaxWidth()
                ) {
                    Column {
                        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TabNavItem(HomeTab)
                            TabNavItem(MapTab)
                            TabNavItem(ProfileTab)
                        }
                    }
                }
            },
            backgroundColor = MaterialTheme.colors.background
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val selected = tabNavigator.current == tab
    
    IconButton(
        onClick = { tabNavigator.current = tab },
        modifier = Modifier.weight(1f)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = if (selected) tab.selectedIcon() else tab.unselectedIcon(),
                contentDescription = tab.options.title,
                tint = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tab.options.title,
                style = MaterialTheme.typography.caption.copy(
                    color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                )
            )
        }
    }
}

private fun Tab.selectedIcon(): ImageVector = when (this) {
    HomeTab -> Icons.Filled.Home
    MapTab -> Icons.Filled.Place
    ProfileTab -> Icons.Filled.Person
    else -> Icons.Filled.Home
}

private fun Tab.unselectedIcon(): ImageVector = when (this) {
    HomeTab -> Icons.Outlined.Home
    MapTab -> Icons.Outlined.Place
    ProfileTab -> Icons.Outlined.Person
    else -> Icons.Outlined.Home
}

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(index = 0u, title = "Home")

    @Composable
    override fun Content() {
        HomeFeedScreen().Content()
    }
}

object MapTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(index = 2u, title = "Map")

    @Composable
    override fun Content() {
        MapScreen().Content()
    }
}

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(index = 3u, title = "Profile")

    @Composable
    override fun Content() {
        ProfileScreen().Content()
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    KMPNewsTheme {
        DashboardContent()
    }
}
