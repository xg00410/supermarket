// =========================================================
// File: Product.kt
// 概要: 商品情報（商品名、価格、カテゴリ、画像パスなど）を保持するデータモデル。
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.models

/**
 * 🛍 Product.kt
 * 商品情報モデル / 商品信息模型
 */
data class Product(
    val productId: Int,           // 🆔 商品ID
    val storeId: String,          // 🏪 店舗ID（Store.id と一致する String）
    val name: String,             // 🏷️ 商品名
    val category: String?,        // 📂 カテゴリ
    val price: Double,            // 💴 価格
    val stock: Int,               // 📦 在庫数
    val imageUrl: String?         // 🖼️ 商品画像URL
)
