// =========================================================
// File: HistoryRepository.kt
// 購入履歴管理（最終版）
// =========================================================

package com.example.supermarket.data

import com.example.supermarket.models.HistoryItem

object HistoryRepository {

    private val historyList = mutableListOf<HistoryItem>()

    fun addHistory(item: HistoryItem) {
        historyList.add(0, item) // 新しいものを上に
    }

    fun getAll(): List<HistoryItem> = historyList
}
