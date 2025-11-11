package com.example.supermarket.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.Product

/**
 * 🧠 CartViewModel.kt
 * --------------------------------------------------------
 * 📘 カートの状態管理クラス
 * 购物车状态管理类（负责增减、删除、统计数量）
 * --------------------------------------------------------
 */
class CartViewModel : ViewModel() {

    // 🛒 当前购物车中的商品列表
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    /** ➕ 添加商品到购物车（若已存在则数量+1） */
    fun addToCart(product: Product) {
        val existing = _cartItems.find { it.productId == product.product_id }
        if (existing != null) {
            val index = _cartItems.indexOf(existing)
            _cartItems[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            _cartItems.add(
                CartItem(
                    productId = product.product_id,
                    productName = product.name,
                    quantity = 1
                )
            )
        }
    }

    /** ➖ 减少某个商品的数量 */
    fun decreaseItem(productId: Int) {
        val existing = _cartItems.find { it.productId == productId }
        if (existing != null) {
            val index = _cartItems.indexOf(existing)
            if (existing.quantity > 1) {
                _cartItems[index] = existing.copy(quantity = existing.quantity - 1)
            } else {
                _cartItems.remove(existing)
            }
        }
    }

    /** ❌ 删除商品 */
    fun removeItem(productId: Int) {
        _cartItems.removeAll { it.productId == productId }
    }

    /** 🧹 清空购物车 */
    fun clearCart() {
        _cartItems.clear()
    }

    /** 🔢 获取购物车总数量 */
    fun totalCount(): Int = _cartItems.sumOf { it.quantity }
}
