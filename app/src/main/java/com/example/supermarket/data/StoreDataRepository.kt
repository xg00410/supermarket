// =========================================================
// File: StoreDataRepository.kt
// 店舗・商品データ管理（最終版）
// =========================================================

package com.example.supermarket.data

import com.example.supermarket.R
import com.example.supermarket.models.Product
import com.example.supermarket.models.Store

object StoreDataRepository {

    private val stores = listOf(
        Store(
            storeId = "S001",
            storeName = "東京中央スーパー",
            address = "東京都中央区1-1-1",
            floorMapRes = R.drawable.logo
        ),
        Store(
            storeId = "S002",
            storeName = "神奈川横浜スーパー",
            address = "神奈川県横浜市2-2-2",
            floorMapRes = R.drawable.logo
        ),
        Store(
            storeId = "S003",
            storeName = "千葉幕張スーパー",
            address = "千葉県千葉市3-3-3",
            floorMapRes = R.drawable.logo
        )
    )

    fun getAllStores(): List<Store> = stores

    fun getStore(storeId: String): Store? =
        stores.find { it.storeId == storeId }

    // 商品100個テンプレート
    private val baseProducts = List(100) { index ->
        Product(
            productId = index + 1,
            name = "商品${index + 1}",
            price = (100..600).random().toDouble(),
            stock = (5..100).random(),
            imageRes = R.drawable.logo,
            category = listOf("飲料", "食品", "菓子", "日用品").random(),
            storeName = "" // ★ 後で付与
        )
    }

    // 店舗ごとにランダム割当
    fun getProductsByStore(storeId: String): List<Product> {
        val store = getStore(storeId) ?: return emptyList()
        val count = (30..60).random()

        return baseProducts
            .shuffled()
            .take(count)
            .map { it.copy(storeName = store.storeName) } // ★ 店舗名埋め込み
    }
}
