package com.example.api_mobile.data.repository

import com.example.api_mobile.data.network.CadastroRequest
import com.example.api_mobile.data.network.CadastroResponse
import com.example.api_mobile.data.network.LoginRequest
import com.example.api_mobile.data.network.LoginResponse
import com.example.api_mobile.data.network.RetrofitClient

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class AuthRepository {

    suspend fun login(usuario: String, senha: String): Result<LoginResponse> {
        return try {
            val response = RetrofitClient.api.login(LoginRequest(usuario, senha))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.sucesso) {
                    Result.Success(body)
                } else {
                    Result.Error(body?.mensagem ?: "Usuário ou senha inválidos")
                }
            } else {
                Result.Error("Erro ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Sem conexão com o servidor. Verifique sua internet.")
        }
    }

    suspend fun cadastro(
        nome: String,
        email: String,
        usuario: String,
        senha: String
    ): Result<CadastroResponse> {
        return try {
            val response = RetrofitClient.api.cadastro(
                CadastroRequest(nome, email, usuario, senha)
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.sucesso) {
                    Result.Success(body)
                } else {
                    Result.Error(body?.mensagem ?: "Erro ao cadastrar")
                }
            } else {
                Result.Error("Erro ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Sem conexão com o servidor. Verifique sua internet.")
        }
    }
}