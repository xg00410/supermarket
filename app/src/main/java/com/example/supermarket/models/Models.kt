package com.example.supermarket.model

// 店舗情報
data class Store(
    val id: String,
    val name: String,
    val address: String,
    val distanceMeters: Int?, // null = 不知道距离（比如非GPS模式）
    val openHours: String,    // 営業時間 "09:00-23:00"
    val floorMapUrl: String?  // 平面図の画像(后面可用)
)

// 商品情報
data class Product(
    val id: String,
    val storeId: String,
    val name: String,
    val category: String,      // "飲料", "食品"...
    val priceYen: Int,
    val stock: Int,
    val imageUrl: String?      // 之后可以换成本地资源
)

// カート内アイテム
data class CartItem(
    val productId: String,
    val productName: String,
    val quantity: Int
)
