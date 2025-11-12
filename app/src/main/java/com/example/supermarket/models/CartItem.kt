package com.example.supermarket.models

/**
 * 🛒 カート内アイテム
 */
data class CartItem(
    val productId: Int,
    val name: String,
    val price: Double,
    var quantity: Int
)

/**
 * 🧩 CartItem → Product 変換関数
 */
fun CartItem.toProduct(): Product {
    return Product(
        productId = this.productId,
        storeId = 0,
        name = this.name,
        category = null,
        price = this.price,
        stock = 0,
        imageUrl = null
    )
}
