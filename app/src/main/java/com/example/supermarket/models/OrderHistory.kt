// =========================================================
// File: OrderHistory.kt
// 購入履歴の1件分を表すデータモデル
// =========================================================

package com.example.supermarket.models

import java.time.LocalDateTime

data class OrderHistoryItem(
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class OrderHistory(
    val orderId: Long,             // 一意ID（System.currentTimeMillis() でOK）
    val storeId: String,
    val storeName: String,
    val orderedAt: LocalDateTime,  // 日付＋時刻
    val totalPrice: Int,
    val items: List<OrderHistoryItem>
)
