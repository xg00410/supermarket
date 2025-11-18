// =========================================================
// File: HistoryItem.kt
// 購入履歴データモデル（最終版）
// =========================================================

package com.example.supermarket.models

data class HistoryItem(
    val historyId: String,          // 一意ID
    val dateTime: String,           // 購入日時
    val storeName: String,          // 店舗名
    val items: List<HistoryProduct> // 商品リスト
)

data class HistoryProduct(
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageRes: Int
)
