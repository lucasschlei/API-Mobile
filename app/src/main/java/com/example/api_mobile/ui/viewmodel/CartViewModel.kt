package com.example.api_mobile.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mercadodani.data.cart.CartItem
import com.example.mercadodani.data.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val repository = CartRepository()

    var checkoutState by mutableStateOf<CheckoutState>(CheckoutState.Idle)
        private set

    var addState by mutableStateOf<AddToCartState>(AddToCartState.Idle)
        private set

    fun adicionarAoCarrinho(item: CartItem) {
        viewModelScope.launch {
            addState = AddToCartState.Loading
            val sucesso = repository.adicionar(item)
            addState = if (sucesso) AddToCartState.Success else AddToCartState.Error
        }
    }

    fun finalizarPedido() {
        viewModelScope.launch {
            checkoutState = CheckoutState.Loading
            val (sucesso, mensagem) = repository.finalizarPedido()
            checkoutState = if (sucesso)
                CheckoutState.Success(mensagem)
            else
                CheckoutState.Error(mensagem)
        }
    }

    fun resetCheckout() { checkoutState = CheckoutState.Idle }
    fun resetAddState() { addState = AddToCartState.Idle }
}

sealed class CheckoutState {
    object Idle    : CheckoutState()
    object Loading : CheckoutState()
    data class Success(val message: String) : CheckoutState()
    data class Error(val message: String)   : CheckoutState()
}

sealed class AddToCartState {
    object Idle    : AddToCartState()
    object Loading : AddToCartState()
    object Success : AddToCartState()
    object Error   : AddToCartState()
}