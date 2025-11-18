// =========================================================
// File: CartViewModel.kt
// カート管理 ViewModel（最終版）
// =========================================================

package com.example.supermarket.viewmodel

import androidx.lifecycle.ViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.Product

class CartViewModel : ViewModel() {

    val cartItems = mutableListOf<CartItem>()

    fun addToCart(product: Product, qty: Int) {
        if (qty <= 0) return
        val existing = cartItems.find { it.product.productId == product.productId }
        if (existing != null) {
            existing.quantity += qty
        } else {
            cartItems.add(CartItem(product, qty))
        }
    }

    fun increase(product: Product) {
        val item = cartItems.find { it.product.productId == product.productId } ?: return
        if (item.quantity < item.product.stock) item.quantity++
    }

    fun decrease(product: Product) {
        val item = cartItems.find { it.product.productId == product.productId } ?: return
        item.quantity--
        if (item.quantity <= 0) cartItems.remove(item)
    }

    fun remove(product: Product) {
        cartItems.removeAll { it.product.productId == product.productId }
    }
}
