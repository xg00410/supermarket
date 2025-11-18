// =========================================================
// File: CartItem.kt
// カート内の商品モデル（最終版）
// =========================================================

package com.example.supermarket.models

data class CartItem(
    val product: Product,   // 商品データ本体
    var quantity: Int       // 選択数量
)
