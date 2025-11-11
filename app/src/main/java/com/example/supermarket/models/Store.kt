package com.example.supermarket.models

/**
 * 🏪 Store.kt
 * 店舗情報モデル / 店铺信息模型
 */
data class Store(
    val id: String,              // 店舗ID / 店铺ID
    val name: String,            // 店舗名 / 店铺名称
    val address: String,         // 住所 / 地址
    val distanceMeters: Int?,    // 距離 (nullの場合は未測定)
    val openHours: String,       // 営業時間 / 营业时间
    val floorMapUrl: String?     // 平面図画像URL / 平面图链接
)
