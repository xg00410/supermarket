package com.example.supermarket.models

/**
 * 🛍 Product.kt
 * 商品情報モデル / 商品信息模型
 */
data class Product(
    val product_id: String,              // 商品ID / Product ID
    val storeId: String,         // 所属店舗ID / 所属店铺ID
    val name: String,            // 商品名 / 商品名称
    val category: String,        // カテゴリ ("飲料"など) / 分类
    val priceYen: Int,           // 価格(円) / 价格（日元）
    val stock: Int,              // 在庫数 / 库存
    val imageUrl: String?        // 画像URL / 图片链接
)
