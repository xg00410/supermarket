package com.example.supermarket.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.Product

class CartViewModel : ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    fun addToCart(product: Product) {
        val existingItem = _cartItems.find { it.productId == product.productId }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            _cartItems.add(
                CartItem(
                    productId = product.productId,
                    name = product.name,
                    price = product.price,
                    quantity = 1
                )
            )
        }
    }

    fun removeItem(productId: Int) {
        _cartItems.removeAll { it.productId == productId }
    }

    fun decreaseItem(productId: Int) {
        val existingItem = _cartItems.find { it.productId == productId }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
            } else {
                _cartItems.remove(existingItem)
            }
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }
}
