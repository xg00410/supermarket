package com.example.supermarket.models

import androidx.annotation.DrawableRes

/**
 * 🏬 StoreItem.kt
 * 店舗リスト表示用データ / 店铺列表卡片用数据
 */
data class StoreItem(
    val id: String,
    val name: String,
    val address: String,
    @DrawableRes val imageRes: Int
)
