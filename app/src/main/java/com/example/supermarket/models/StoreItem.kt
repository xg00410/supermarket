package com.example.supermarket.model

import androidx.annotation.DrawableRes

/**
 * 🏪 StoreItem.kt
 * ----------------------------------------
 * 📘 店舗リスト画面用データモデル
 * 用于表示列表中每个店铺的简要信息（店铺ID、名称、地址、图片资源等）
 * ----------------------------------------
 * - id: 店铺ID
 * - name: 店铺名称
 * - address: 店铺地址
 * - imageRes: 显示在店铺卡片上的图片资源
 */
data class StoreItem(
    val id: String,                 // 🆔 店舗ID / 店铺ID
    val name: String,               // 🏪 店舗名 / 店铺名称
    val address: String,            // 📍 住所 / 地址
    @DrawableRes val imageRes: Int  // 🖼️ 画像リソースID / 图片资源ID
)
