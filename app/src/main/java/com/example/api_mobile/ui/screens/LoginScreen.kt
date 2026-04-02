package com.example.api_mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.api_mobile.ui.components.AppBottomNavigation
import com.example.api_mobile.ui.components.AppTextField
import com.example.api_mobile.ui.components.AppTopBar
import com.example.api_mobile.ui.viewmodel.AuthState
import com.example.api_mobile.ui.viewmodel.AuthViewModel
import com.example.mercadodani.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onFirstAccess: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    val loginState = authViewModel.loginState

    LaunchedEffect(loginState) {
        if (loginState is AuthState.Success) {
            authViewModel.resetLoginState()
            onLoginSuccess()
        }
    }

    Scaffold(
        topBar = { AppTopBar(showLogo = false) },
        bottomBar = { AppBottomNavigation(selectedIndex = 2, onItemSelected = onBottomNavSelected) },
        containerColor = White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardGray),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AppTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Usuário",
                        placeholder = "@sm1llle"
                    )
                    AppTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Senha",
                        placeholder = "••••••••••••",
                        isPassword = true
                    )

                    if (loginState is AuthState.Error) {
                        Text(
                            text = loginState.message,
                            color = Color(0xFFCC3333),
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = { authViewModel.login(username, password) },
                        enabled = loginState !is AuthState.Loading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        if (loginState is AuthState.Loading) {
                            CircularProgressIndicator(color = White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Entrar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = White)
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextButton(onClick = onForgotPassword) {
                            Text("Esqueceu a senha>", fontSize = 13.sp, color = DarkText, textDecoration = TextDecoration.Underline)
                        }
                        TextButton(onClick = onFirstAccess) {
                            Text("Primeiro acesso>", fontSize = 13.sp, color = DarkText, textDecoration = TextDecoration.Underline)
                        }
                    }
                }
            }
        }
    }
}