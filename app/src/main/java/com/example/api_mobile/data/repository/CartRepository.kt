package com.example.api_mobile.data.repository

import android.util.Log
import com.example.mercadodani.data.cart.CartItem
import com.example.mercadodani.data.cart.CartManager
import com.example.mercadodani.data.network.AdicionarCarrinhoRequest
import com.example.mercadodani.data.network.CriarPedidoRequest
import com.example.mercadodani.data.network.RetrofitClient
import com.example.mercadodani.data.session.UserSession

class CartRepository {

    suspend fun adicionar(item: CartItem): Boolean {
        val uid = UserSession.userId ?: return false
        return try {
            val response = RetrofitClient.api.adicionarAoCarrinho(
                AdicionarCarrinhoRequest(uid, item.id)
            )
            if (response.isSuccessful) {
                CartManager.addItem(item)
                true
            } else {
                Log.e("CartRepository", "Erro ao adicionar: ${response.code()} - ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("CartRepository", "Exceção ao adicionar: ${e.message}")
            false
        }
    }

    suspend fun finalizarPedido(): Pair<Boolean, String> {
        val uid = UserSession.userId ?: return Pair(false, "Usuário não logado")
        return try {
            val response = RetrofitClient.api.criarPedido(CriarPedidoRequest(uid))
            Log.d("CartRepository", "Finalizar response: ${response.code()} - ${response.body()}")

            if (response.isSuccessful && response.body()?.id != null) {
                CartManager.clear()
                val total = response.body()?.total
                Pair(true, "Pedido #${response.body()?.id} realizado! Total: R$ %.2f".format(total).replace(".", ","))
            } else {
                Log.e("CartRepository", "Erro ao finalizar: ${response.errorBody()?.string()}")
                Pair(false, "Erro ao finalizar pedido")
            }
        } catch (e: Exception) {
            Log.e("CartRepository", "Exceção ao finalizar: ${e.message}")
            Pair(false, "Sem conexão com o servidor")
        }
    }
}