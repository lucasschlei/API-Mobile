package com.example.api_mobile.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mercadodani.data.repository.AuthRepository
import com.example.mercadodani.data.repository.Result
import com.example.mercadodani.data.session.UserSession
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    var loginState    by mutableStateOf<AuthState>(AuthState.Idle)
        private set
    var cadastroState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun login(usuario: String, senha: String) {
        if (usuario.isBlank() || senha.isBlank()) { loginState = AuthState.Error("Preencha todos os campos"); return }
        viewModelScope.launch {
            loginState = AuthState.Loading
            loginState = when (val r = repository.login(usuario, senha)) {
                is Result.Success -> {
                    r.data.id?.let { UserSession.login(it, r.data.nome ?: "", r.data.usuario ?: "") }
                    AuthState.Success(r.data.mensagem)
                }
                is Result.Error -> AuthState.Error(r.message)
                else -> AuthState.Error("Erro desconhecido")
            }
        }
    }

    fun cadastro(nome: String, email: String, usuario: String, senha: String, confirma: String) {
        if (nome.isBlank() || email.isBlank() || usuario.isBlank() || senha.isBlank()) { cadastroState = AuthState.Error("Preencha todos os campos"); return }
        if (senha != confirma) { cadastroState = AuthState.Error("As senhas não coincidem"); return }
        viewModelScope.launch {
            cadastroState = AuthState.Loading
            cadastroState = when (val r = repository.cadastro(nome, email, usuario, senha)) {
                is Result.Success -> AuthState.Success(r.data.mensagem)
                is Result.Error   -> AuthState.Error(r.message)
                else -> AuthState.Error("Erro desconhecido")
            }
        }
    }

    fun logout() { UserSession.logout() }
    fun resetLoginState()    { loginState    = AuthState.Idle }
    fun resetCadastroState() { cadastroState = AuthState.Idle }
}

sealed class AuthState {
    object Idle    : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String)   : AuthState()
}