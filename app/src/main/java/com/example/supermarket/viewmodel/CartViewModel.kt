package com.example.supermarket.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.supermarket.model.Product
import com.example.supermarket.model.CartItem

class CartViewModel : ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    // 添加商品
    fun addToCart(product: Product) {
        val existing = _cartItems.find { it.productId == product.id }
        if (existing != null) {
            val newList = _cartItems.toMutableList()
            val index = newList.indexOf(existing)
            newList[index] = existing.copy(quantity = existing.quantity + 1)
            _cartItems.clear()
            _cartItems.addAll(newList)
        } else {
            _cartItems.add(
                CartItem(
                    productId = product.id,
                    productName = product.name,
                    quantity = 1
                )
            )
        }
    }

    // 数量减少
    fun decreaseItem(productId: String) {
        val existing = _cartItems.find { it.productId == productId }
        if (existing != null) {
            if (existing.quantity > 1) {
                val newList = _cartItems.toMutableList()
                val index = newList.indexOf(existing)
                newList[index] = existing.copy(quantity = existing.quantity - 1)
                _cartItems.clear()
                _cartItems.addAll(newList)
            } else {
                _cartItems.remove(existing)
            }
        }
    }

    // 删除项目
    fun removeItem(productId: String) {
        _cartItems.removeAll { it.productId == productId }
    }

    // 清空购物车
    fun clearCart() {
        _cartItems.clear()
    }

    // 计算总数量
    fun totalCount(): Int = _cartItems.sumOf { it.quantity }
}
