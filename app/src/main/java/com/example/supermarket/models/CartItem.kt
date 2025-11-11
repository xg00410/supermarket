package com.example.supermarket.models

/**
 * 🛒 CartItem.kt
 * カート内アイテム情報 / 购物车项目数据模型
 */
data class CartItem(
    val productId: String,   // 商品ID / Product ID
    val productName: String, // 商品名 / Product Name
    val quantity: Int        // 数量 / Quantity
)
