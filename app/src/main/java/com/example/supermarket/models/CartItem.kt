// =========================================================
// File: CartItem.kt
// 概要: カート内商品の数量・小計金額などを管理するモデル。
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.models

/**
 * CartItem
 * 🇯🇵 カートに入っている商品データ
 * 🇨🇳 购物车中的商品数据
 *
 * - productId：git 商品ID
 * - name：商品名
 * - price：价格
 * - quantity：数量
 */
data class CartItem(
    val productId: Int,
    val name: String,
    val price: Double,
    var quantity: Int
)

/**
 * CartItem → Product 変換
 * 🇯🇵 カートに入っている商品を Product に変換する簡易関数
 * 🇨🇳 将购物车商品转换为 Product 的简易方法
 *
 * ※ CartViewModel.addToCart(Product) が必要とするため
 *   简化字段，只使用最基本的 productId, name, price。
 */
fun CartItem.toProduct(): Product =
    Product(
        productId = this.productId,
        storeId = "",            // カートでは不要なので空文字でOK
        name = this.name,
        category = null,         // カートではカテゴリ情報は保持しない
        price = this.price,
        stock = 0,               // 在庫は不要なので0
        imageUrl = null          // 画像は不要なのでnull
    )
