package com.example.supermarket.models

/**
 * 🛍 Product.kt
 * 商品情報モデル / 商品信息模型
 */
data class Product(
    val productId: Int,           // 🆔 商品ID
    val storeId: Int,             // 🏪 店舗ID
    val name: String,             // 🏷️ 商品名
    val category: String?,        // 📂 カテゴリ
    val price: Double,            // 💴 価格
    val stock: Int,               // 📦 在庫数
    val imageUrl: String?         // 🖼️ 商品画像URL
)
