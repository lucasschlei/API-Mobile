package com.example.api_mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onAlreadyHaveAccount: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var fullName        by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var username        by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val cadastroState = authViewModel.cadastroState
    val usernameAvailable = username.length >= 3
    val passwordsMatch    = password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
    val passwordsMismatch = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword

    LaunchedEffect(cadastroState) {
        if (cadastroState is AuthState.Success) {
            authViewModel.resetCadastroState()
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = { AppTopBar() },
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
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardGray),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AppTextField(value = fullName, onValueChange = { fullName = it }, label = "Nome completo", placeholder = "José Maria dos Santos")
                    AppTextField(value = email, onValueChange = { email = it }, label = "Email", placeholder = "josemaria@gmail.com", keyboardType = KeyboardType.Email)

                    AppTextField(
                        value = username,
                        onValueChange = { username = it.trimStart('@') },
                        label = "Nome de usuário",
                        placeholder = "@josemaria1",
                        trailingContent = {
                            if (usernameAvailable) Text("disponível", fontSize = 11.sp, color = PrimaryBlue)
                        }
                    )
                    AppTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Criar senha",
                        placeholder = "••••••••••••",
                        isPassword = true,
                        trailingContent = {
                            if (passwordsMatch) Text("as senhas são iguais", fontSize = 11.sp, color = PrimaryBlue)
                            else if (passwordsMismatch) Text("as senhas não são iguais", fontSize = 11.sp, color = Color(0xFFCC3333))
                        }
                    )
                    AppTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirmar senha",
                        placeholder = "••••••••••••",
                        isPassword = true,
                        trailingContent = {
                            if (passwordsMatch) Text("as senhas são iguais", fontSize = 11.sp, color = PrimaryBlue)
                            else if (passwordsMismatch) Text("as senhas não são iguais", fontSize = 11.sp, color = Color(0xFFCC3333))
                        }
                    )

                    when (cadastroState) {
                        is AuthState.Error ->
                            Text(text = cadastroState.message, color = Color(0xFFCC3333), fontSize = 13.sp)
                        is AuthState.Success ->
                            Text(text = cadastroState.message, color = Color(0xFF22C55E), fontSize = 13.sp)
                        else -> {}
                    }

                    Button(
                        onClick = {
                            authViewModel.cadastro(fullName, email, username, password, confirmPassword)
                        },
                        enabled = cadastroState !is AuthState.Loading && passwordsMatch,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        if (cadastroState is AuthState.Loading) {
                            CircularProgressIndicator(color = White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Cadastrar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = White)
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        TextButton(onClick = onAlreadyHaveAccount) {
                            Text("Possuo cadastro>", fontSize = 13.sp, color = DarkText, textDecoration = TextDecoration.Underline)
                        }
                    }
                }
            }
        }
    }
}