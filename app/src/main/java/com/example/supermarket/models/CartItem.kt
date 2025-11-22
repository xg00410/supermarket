// =========================================================
// File: CartItem.kt
// 設計書ID: list / list2
// 画面名: カート / カート管理画面
// 役割:
//   - カート内商品の情報（店舗ID／商品画像／数量など）を保持するモデル。
//   - 店舗ごとにグループ化し、list2（管理画面）で管理できるようにする。
// =========================================================

package com.example.supermarket.models

import androidx.annotation.DrawableRes

/**
 * CartItem
 * カートに入っている商品データ（店舗情報つき）
 */
data class CartItem(
    val productId: Int,      // 商品ID
    val storeId: String,     // 店舗ID
    val storeName: String,   // 店舗名
    val name: String,        // 商品名
    val category: String,    // カテゴリ
    val price: Double,       // 単価
    var quantity: Int,       // 数量
    @DrawableRes val imageRes: Int?   // 商品画像（null の場合はダミー画像を利用）
)
