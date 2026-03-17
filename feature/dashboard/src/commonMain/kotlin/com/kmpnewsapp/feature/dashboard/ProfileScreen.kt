package com.kmpnewsapp.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kmpnewsapp.core.database.NewsDao
import com.kmpnewsapp.core.designsystem.theme.*
import com.kmpnewsapp.core.utils.navigation.SharedScreen
import org.koin.compose.koinInject
import coil3.compose.SubcomposeAsyncImage
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        // Use the parent navigator to replace the entire Dashboard with Auth
        val mainNavigator = navigator.parent ?: navigator
        
        val newsDao = koinInject<NewsDao>()
        val user by newsDao.getLoggedInUser().collectAsState(initial = null)
        val scope = rememberCoroutineScope()
        
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) { factory.createPermissionsController() }
        BindEffect(controller)
        
        var showImageSourceDialog by remember { mutableStateOf(false) }
        var isCameraOpen by remember { mutableStateOf(false) }

        val pickerLauncher = rememberImagePickerLauncher(
            selectionMode = SelectionMode.Single,
            scope = scope,
            onResult = { byteArrays: List<ByteArray> ->
                byteArrays.firstOrNull()?.let { bytes: ByteArray ->
                    scope.launch {
                        user?.let { u ->
                            newsDao.insertUser(u.copy(profileImage = bytes))
                        }
                    }
                }
            }
        )

        val cameraState = rememberPeekabooCameraState(onCapture = { bytes: ByteArray? ->
            if (bytes != null) {
                scope.launch {
                    user?.let { u ->
                        newsDao.insertUser(u.copy(profileImage = bytes))
                    }
                }
            }
            isCameraOpen = false
        })

        if (isCameraOpen) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                PeekabooCamera(
                    modifier = Modifier.fillMaxSize(),
                    state = cameraState,
                    permissionDeniedContent = {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Camera permission is required",
                                    color = Color.White,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { isCameraOpen = false }) {
                                    Text("Go Back")
                                }
                            }
                        }
                    }
                )
                
                IconButton(
                    onClick = { isCameraOpen = false },
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close Camera", tint = Color.White)
                }

                Button(
                    onClick = { cameraState.capture() },
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Capture", tint = Color.Black)
                }
            }
            return
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.background,
                    title = {
                        Text(
                            text = "Account",
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                )
            },
            backgroundColor = MaterialTheme.colors.background
        ) { padding ->
            if (showImageSourceDialog) {
                AlertDialog(
                    onDismissRequest = { showImageSourceDialog = false },
                    title = { Text("Update Profile Photo") },
                    text = { Text("Choose a photo from your gallery or capture a new one.") },
                    confirmButton = {
                        TextButton(onClick = { 
                            showImageSourceDialog = false
                            scope.launch {
                                try {
                                    controller.providePermission(Permission.CAMERA)
                                    isCameraOpen = true
                                } catch (e: Exception) {
                                    // Permission denied or error
                                }
                            }
                        }) {
                            Text("Camera")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { 
                            showImageSourceDialog = false
                            pickerLauncher.launch()
                        }) {
                            Text("Gallery")
                        }
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Profile Header
                    
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (user?.profileImage != null) {
                                SubcomposeAsyncImage(
                                    model = user?.profileImage,
                                    contentDescription = "Profile Photo",
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                val initials = remember(user?.fullName) {
                                    user?.fullName?.split(" ")
                                        ?.filter { it.isNotBlank() }
                                        ?.take(2)
                                        ?.joinToString("") { it.take(1) }
                                        ?.uppercase() ?: "GU"
                                }
                                Text(
                                    text = initials,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        }

                        // Edit Button Overlay - Positioned relative to the 100.dp Box which is NOT clipped
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.primary)
                                .clickable { showImageSourceDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Photo",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = user?.fullName ?: "Guest User",
                        style = MaterialTheme.typography.h2
                    )
                    Text(
                        text = user?.email ?: "guest@example.com",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                }

                item {
                    val themeScreenModel = koinInject<ThemeScreenModel>()
                    val isDarkTheme by themeScreenModel.isDarkTheme.collectAsState()

                    SectionTitle("Settings")
                    SettingItem(Icons.Default.Notifications, "Notifications") { }
                    SettingItem(Icons.Default.Lock, "Security") { }
                    SettingItem(Icons.Default.Settings, "Language", "English") { }
                    
                    // Dark Mode Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Brightness4, contentDescription = null, tint = MaterialTheme.colors.onBackground, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Dark Mode",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colors.onBackground
                        )
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { themeScreenModel.toggleTheme() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colors.primary,
                                checkedTrackColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    SectionTitle("Info")
                    SettingItem(Icons.Default.Info, "About Daily News") { }
                    SettingItem(Icons.Default.Call, "Help & Support") { }
                }

                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    Button(
                        onClick = { 
                            scope.launch {
                                newsDao.clearUser()
                                mainNavigator.replace(ScreenRegistry.get(SharedScreen.Auth))
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            contentColor = AccentRed
                        ),
                        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Out", style = MaterialTheme.typography.button)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    @Composable
    private fun SectionTitle(title: String) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
    }

    @Composable
    private fun SettingItem(
        icon: ImageVector,
        title: String,
        value: String? = null,
        onClick: () -> Unit
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colors.onBackground
            ),
            shape = RoundedCornerShape(0.dp) // Keep it rectangular like a list item
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colors.onBackground, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title, 
                    style = MaterialTheme.typography.subtitle1, 
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colors.onBackground
                )
                value?.let {
                    Text(text = it, style = MaterialTheme.typography.body2, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}
