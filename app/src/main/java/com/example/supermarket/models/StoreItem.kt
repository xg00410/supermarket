// =========================================================
// File: StoreItem.kt
// 概要: 店舗リスト表示カードで使用される簡易店舗データモデル。
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

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
