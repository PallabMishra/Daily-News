package com.kmpnewsapp.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kmpnewsapp.core.designsystem.theme.KMPNewsTheme
import com.kmpnewsapp.core.designsystem.theme.PrimaryBlue
import com.kmpnewsapp.core.utils.navigation.SharedScreen
import com.kmpnewsapp.core.database.NewsDao
import com.kmpnewsapp.core.database.UserEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import org.koin.compose.koinInject
import org.jetbrains.compose.ui.tooling.preview.Preview

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var progress by remember { mutableStateOf(0f) }
        val newsDao = koinInject<NewsDao>()
        
        LaunchedEffect(Unit) {
            // Simulated loading progress
            for (i in 1..100) {
                delay(20L)
                progress = i / 100f
            }
            delay(500L)
            
            // Check if user is already logged in
            val loggedInUser = newsDao.getLoggedInUser().firstOrNull<UserEntity?>()
            val destination = if (loggedInUser != null) {
                SharedScreen.Dashboard
            } else {
                SharedScreen.Auth
            }
            
            navigator.replace(ScreenRegistry.get(destination))
        }

        SplashContent(progress)
    }
}

@Composable
fun SplashContent(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material.MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(PrimaryBlue, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "N",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Daily News",
                style = androidx.compose.material.MaterialTheme.typography.h2,
                color = androidx.compose.material.MaterialTheme.colors.onBackground
            )

            Text(
                text = "The world's stories, curated for clarity.",
                style = androidx.compose.material.MaterialTheme.typography.body2,
                color = androidx.compose.material.MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(
                modifier = Modifier.width(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "INITIALIZING",
                    style = androidx.compose.material.MaterialTheme.typography.caption.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = androidx.compose.material.MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = androidx.compose.material.MaterialTheme.colors.primary,
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                )
            }
        }

        Text(
            text = "DAILY NEWS © 2026",
            style = androidx.compose.material.MaterialTheme.typography.caption.copy(letterSpacing = 1.sp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            color = androidx.compose.material.MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Preview
@Composable
fun SplashPreview() {
    KMPNewsTheme {
        SplashContent(progress = 0.5f)
    }
}
