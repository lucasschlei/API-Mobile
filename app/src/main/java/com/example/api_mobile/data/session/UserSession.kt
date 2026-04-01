package com.example.api_mobile.data.session

object UserSession {
    var isLoggedIn: Boolean = false
    var userId: Int? = null
    var nome: String? = null
    var usuario: String? = null

    fun login(id: Int, nome: String, usuario: String) {
        isLoggedIn = true
        userId     = id
        this.nome  = nome
        this.usuario = usuario
    }

    fun logout() {
        isLoggedIn = false
        userId     = null
        nome       = null
        usuario    = null
    }
}
