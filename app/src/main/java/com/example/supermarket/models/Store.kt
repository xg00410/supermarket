package com.example.supermarket.models

/**
 * 🏪 Store.kt
 * 店舗情報モデル / 店铺信息模型
 */
data class Store(
    val id: String,                  // 店舗ID / 店铺ID
    val name: String,                // 店舗名 / 店铺名称
    val address: String,             // 住所 / 地址（务必包含都道府県）
    val distanceMeters: Int? = null, // 距離 / 距离（null = 未测量）
    val openHours: String = "不明",   // 営業時間 / 营业时间（默认“不明”）
    val floorMapUrl: String? = null  // 平面図画像URL / 平面图链接
)
