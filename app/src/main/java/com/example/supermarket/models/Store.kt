// =========================================================
// File: Store.kt
// 設計書ID: Store / Store.Curent location / store_拡大
// 画面名: 店舗情報モデル
// 役割:
//   - 店舗一覧／地図検索／店舗詳細／商品一覧(menu) などで使用される
//   - 店舗ID／店舗名／住所／地図位置／店舗画像などを保持
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.models

import androidx.annotation.DrawableRes

/**
 * Store
 * 🇯🇵 店舗データ
 * 🇨🇳 店铺信息数据
 *
 * 設計書に基づく必須項目：
 *  - storeId: 店舗ID
 *  - storeName: 店舗名
 *  - address: 住所
 *  - latitude / longitude: 店舗位置（現在地検索・最短ルート用）
 *  - imageRes: 店舗画像（店舗画面、店舗一覧で使用）
 *  - floorMapRes: 店舗内の平面図（item配置図）※拡大画面で使用
 */
data class Store(
    val storeId: String,
    val storeName: String,
    val address: String,

    val latitude: Double,
    val longitude: Double,

    @DrawableRes
    val imageRes: Int?,       // 店舗一覧に表示する画像
    @DrawableRes
    val floorMapRes: Int?     // 店舗内平面図（StoreMapExpandedで使用）
)
