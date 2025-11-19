package com.example.supermarket.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.Product
import com.example.supermarket.models.OrderHistory
import com.example.supermarket.models.OrderHistoryItem
import java.time.LocalDateTime

class CartViewModel : ViewModel() {

    // カート内商品
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    // 履歴
    private val _orderHistory = mutableStateListOf<OrderHistory>()
    val orderHistory: List<OrderHistory> get() = _orderHistory

    // -------------------------
    // カート操作
    // -------------------------

    fun addToCart(product: Product, quantity: Int) {
        if (quantity <= 0) return

        val existing = _cartItems.find { it.productId == product.productId }
        if (existing != null) {
            existing.quantity += quantity
        } else {
            _cartItems.add(
                CartItem(
                    productId = product.productId,
                    storeId = product.storeId,
                    storeName = product.storeName,   // ★ 新增
                    name = product.name,
                    category = product.category,     // ★ 新增
                    price = product.price,
                    quantity = quantity,
                    imageRes = product.imageRes
                )
            )
        }
    }

    fun increaseQuantity(productId: Int) {
        _cartItems.find { it.productId == productId }?.let { it.quantity++ }
    }

    fun decreaseQuantity(productId: Int) {
        _cartItems.find { it.productId == productId }?.let {
            if (it.quantity > 1) it.quantity--
        }
    }

    fun removeItem(productId: Int) {
        _cartItems.removeAll { it.productId == productId }
    }

    fun totalPrice(): Double {
        return _cartItems.sumOf { it.quantity * it.price }
    }

    // -------------------------
    // 履歴操作（RouteScreen の終了ボタン用）
    // -------------------------
    fun addHistory(storeId: String, storeName: String, items: List<CartItem>) {
        if (items.isEmpty()) return

        val historyItems = items.map {
            OrderHistoryItem(
                productId = it.productId,
                name = it.name,
                price = it.price,
                quantity = it.quantity
            )
        }

        val historyEntry = OrderHistory(
            orderId = System.currentTimeMillis(),
            storeId = storeId,
            storeName = storeName,
            orderedAt = LocalDateTime.now(),
            items = historyItems
        )

        _orderHistory.add(historyEntry)
    }

    // チェックした商品だけ削除する
    fun removeCheckedItems(checkedIds: Set<Int>) {
        _cartItems.removeAll { it.productId in checkedIds }
    }
}
