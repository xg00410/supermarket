package com.example.supermarket.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.Product

/**
 * CartViewModel
 * 🇯🇵 カートを管理する ViewModel
 * 🇨🇳 购物车管理 ViewModel
 *
 * - mutableStateListOf を使うため、Compose が自動でUI更新を行う
 * - Flow を使わないため collectAsState() は不要
 */
class CartViewModel : ViewModel() {

    // -------------------------------
    // 🇯🇵 カート内部リスト（Compose対応）
    // 🇨🇳 购物车内部列表（Compose 可自动刷新 UI）
    // -------------------------------
    private val _cartItems = mutableStateListOf<CartItem>()

    /**
     * 🇯🇵 外部公開用：List<CartItem>
     * 🇨🇳 对外公开：List<CartItem>
     */
    val cartItems: List<CartItem>
        get() = _cartItems

    // ------------------------------
    // 🇯🇵 カートに商品を追加（既に存在 → 数量 +1）
    // 🇨🇳 加入购物车（已存在 → 数量 +1）
    // ------------------------------
    fun addToCart(product: Product) {
        val existing = _cartItems.find { it.productId == product.productId }
        if (existing != null) {
            existing.quantity++
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

    // ------------------------------
    // 🇯🇵 商品を削除
    // 🇨🇳 删除商品
    // ------------------------------
    fun removeItem(productId: Int) {
        _cartItems.removeAll { it.productId == productId }
    }

    // ------------------------------
    // 🇯🇵 数量 -1（1 以下なら削除）
    // 🇨🇳 数量 -1（若数量为 1 则删除）
    // ------------------------------
    fun decreaseItem(productId: Int) {
        val existing = _cartItems.find { it.productId == productId }
        if (existing != null) {
            if (existing.quantity > 1) {
                existing.quantity--
            } else {
                _cartItems.remove(existing)
            }
        }
    }

    // ------------------------------
    // 🇯🇵 カートを空にする
    // 🇨🇳 清空购物车
    // ------------------------------
    fun clearCart() {
        _cartItems.clear()
    }
}
