package com.example.api_mobile.data.cart


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color

data class CartItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val color: Color,
    var quantity: Int = 1
)

object CartManager {
    val items = mutableStateListOf<CartItem>()

    val totalItems: Int
        get() = items.sumOf { it.quantity }

    val totalPrice: Double
        get() = items.sumOf { it.price * it.quantity }

    fun addItem(item: CartItem) {
        val existing = items.indexOfFirst { it.id == item.id }
        if (existing >= 0) {
            items[existing] = items[existing].copy(quantity = items[existing].quantity + 1)
        } else {
            items.add(item.copy(quantity = 1))
        }
    }

    fun removeItem(itemId: Int) {
        val index = items.indexOfFirst { it.id == itemId }
        if (index >= 0) {
            val current = items[index]
            if (current.quantity > 1) {
                items[index] = current.copy(quantity = current.quantity - 1)
            } else {
                items.removeAt(index)
            }
        }
    }

    fun deleteItem(itemId: Int) {
        items.removeAll { it.id == itemId }
    }

    fun clear() {
        items.clear()
    }
}