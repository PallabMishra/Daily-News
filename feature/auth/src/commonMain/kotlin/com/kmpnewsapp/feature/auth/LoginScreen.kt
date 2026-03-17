package com.kmpnewsapp.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.kmpnewsapp.core.designsystem.components.PrimaryButton
import com.kmpnewsapp.core.designsystem.theme.BackgroundLight
import com.kmpnewsapp.core.designsystem.theme.KMPNewsTheme
import com.kmpnewsapp.core.designsystem.theme.PrimaryBlue
import com.kmpnewsapp.core.designsystem.theme.TextPrimary
import com.kmpnewsapp.core.designsystem.theme.TextSecondary
import com.kmpnewsapp.core.utils.navigation.SharedScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<AuthViewModel>()
        val state by viewModel.state.collectAsState()

        // Use a key that changes only once to avoid multiple navigation calls
        LaunchedEffect(state.isSuccess) {
            if (state.isSuccess) {
                // Ensure we only navigate if the navigator is still valid
                navigator.replace(ScreenRegistry.get(SharedScreen.Dashboard))
            }
        }

        LoginContent(
            state = state,
            onBackClick = { navigator.pop() },
            onLoginClick = { email, password ->
                if (!state.isLoading) {
                    viewModel.login(email, password)
                }
            },
            onSignUpClick = { fullName, email, password ->
                if (!state.isLoading) {
                    viewModel.signUp(fullName, email, password)
                }
            }
        )
    }
}

@Composable
fun LoginContent(
    state: AuthState = AuthState(),
    onBackClick: () -> Unit = {},
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onSignUpClick: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoginMode by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background,
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colors.onBackground)
                    }
                }
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "NewsDaily",
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.primary
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = if (isLoginMode) "Welcome Back" else "Create Account",
                    style = MaterialTheme.typography.h1
                )
                
                Text(
                    text = "Stay updated with the latest stories from around the globe.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                TabRow(
                    selectedTabIndex = if (isLoginMode) 0 else 1,
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[if (isLoginMode) 0 else 1]),
                            color = MaterialTheme.colors.primary,
                            height = 2.dp
                        )
                    },
                    divider = {
                        TabRowDefaults.Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                    }
                ) {
                    Tab(
                        selected = isLoginMode,
                        onClick = { isLoginMode = true },
                        text = { 
                            Text(
                                "Login", 
                                style = MaterialTheme.typography.subtitle1,
                                color = if (isLoginMode) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            ) 
                        }
                    )
                    Tab(
                        selected = !isLoginMode,
                        onClick = { isLoginMode = false },
                        text = { 
                            Text(
                                "Sign Up", 
                                style = MaterialTheme.typography.subtitle1,
                                color = if (!isLoginMode) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            ) 
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))

                if (!isLoginMode) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colors.primary,
                            unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                    )
                )

                if (isLoginMode) {
                    Spacer(modifier = Modifier.height(16.dp))
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colors.primary,
                            unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                if (state.error != null) {
                    Text(
                        text = state.error ?: "",
                        color = Color.Red,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                PrimaryButton(
                    text = if (isLoginMode) "Continue" else "Create Account",
                    onClick = {
                        if (isLoginMode) {
                            onLoginClick(email, password)
                        } else {
                            if (password == confirmPassword) {
                                onSignUpClick(fullName, email, password)
                            }
                        }
                    }
                )

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 16.dp),
                        color = MaterialTheme.colors.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            Text(
                text = "By continuing, you agree to our Terms of Service and Privacy Policy.",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
