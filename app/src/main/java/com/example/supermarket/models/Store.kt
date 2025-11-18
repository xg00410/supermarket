// =========================================================
// File: Store.kt
// 店舗データモデル（最終版）
// =========================================================

package com.example.supermarket.models

import androidx.annotation.DrawableRes

data class Store(
    val storeId: String,         // 店舗ID
    val storeName: String,       // 店舗名
    val address: String,         // 住所

    @DrawableRes
    val floorMapRes: Int         // 店内平面図（RouteScreenでも使用）
)
