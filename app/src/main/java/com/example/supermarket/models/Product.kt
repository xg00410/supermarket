// =========================================================
// File: Product.kt
// 商品データモデル（最終版）
// =========================================================

package com.example.supermarket.models

data class Product(
    val productId: Int,       // 商品ID
    val name: String,         // 商品名
    val price: Double,        // 単価
    val stock: Int,           // 在庫数
    val imageRes: Int,        // 商品画像
    val category: String,     // 大カテゴリ（エリアID：飲料/食品など）
    val storeName: String     // 所属店舗名（履歴＆ルート用）
)
