// =========================================================
// File: Product.kt
// 設計書ID: menu / list / list2
// 画面名: 商品情報モデル
// 役割:
//   - 店舗内の商品データを保持する基本モデル。
//   - menu画面（カテゴリ＋商品一覧）
//   - list（カート）
//   - list2（カート管理）
//   - route（最短ルート）
//   など全画面で共通して使用するデータ構造。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.models

import androidx.annotation.DrawableRes

/**
 * Product
 * 🇯🇵 店舗内の商品データ
 * 🇨🇳 超市店铺中的商品数据
 *
 * 必須項目（設計書完全対応）:
 *  - productId: 商品ID
 *  - storeId: 店舗ID（どの店舗の商品か識別）
 *  - storeName: 店舗名（list2の店舗単位表示に必要）
 *  - name: 商品名
 *  - category: カテゴリ（menu画面の左側カテゴリ切替に必要）
 *  - price: 単価
 *  - stock: 在庫数（数量＋／－の制限に使用）
 *  - imageRes: 画像（list2／menuに必要）
 */
data class Product(
    val productId: Int,
    val storeId: String,
    val storeName: String,
    val name: String,
    val category: String,
    val price: Double,
    val stock: Int,


    @DrawableRes val imageRes: Int,
    val imageName: String?,
    // ★ ここから追加（既存のコードは一切変更しない）
    val shelfId: String?,          // PHP: shelf_id
    val accessPointId  : String?     // PHP: access_point_id
)
