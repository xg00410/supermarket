// =========================================================
// File: CartItem.kt
// 設計書ID: list / list2
// 画面名: カート / カート管理画面
// 役割:
//   - カート内商品の情報（店舗ID／商品画像／数量など）を保持するモデル。
//   - 店舗ごとにグループ化し、list2（管理画面）で管理できるようにする。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.models

import androidx.annotation.DrawableRes

/**
 * CartItem
 * 🇯🇵 カートに入っている商品データ（店舗情報つき）
 * 🇨🇳 购物车中的商品数据（含店铺信息）
 */
data class CartItem(
    val productId: Int,
    val storeId: String,
    val storeName: String,
    val name: String,
    val category: String,
    val price: Double,
    var quantity: Int,
    val imageRes: Int?
)
