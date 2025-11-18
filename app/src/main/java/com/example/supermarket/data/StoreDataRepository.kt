// =========================================================
// File: StoreDataRepository.kt
// 役割:
//   - 店舗情報(Store)と商品情報(Product)を管理する簡易リポジトリ。
//   - 設計書の店舗画面／menu／list／list2／route で共通利用。
//   - 実際のDB／PHP連携を行う前のスタブデータとして使用。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.data

import com.example.supermarket.R
import com.example.supermarket.models.Product
import com.example.supermarket.models.Store

object StoreDataRepository {

    // -----------------------------------------------------
    // 店舗マスタ
    // -----------------------------------------------------
    private val stores: List<Store> = listOf(
        Store(
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            address = "神奈川県川崎市川崎区〇〇1-2-3",
            latitude = 35.5300,
            longitude = 139.7000,
            imageRes = R.drawable.logo,         // ★ 全部 logo
            floorMapRes = R.drawable.logo       // ★ 全部 logo
        ),
        Store(
            storeId = "S002",
            storeName = "スーパーマーケット蒲田駅西口店",
            address = "東京都大田区西蒲田4-5-6",
            latitude = 35.5620,
            longitude = 139.7160,
            imageRes = R.drawable.logo,         // ★
            floorMapRes = R.drawable.logo       // ★
        ),
        Store(
            storeId = "S003",
            storeName = "スーパーマーケット品川高輪店",
            address = "東京都港区高輪3-4-5",
            latitude = 35.6280,
            longitude = 139.7390,
            imageRes = R.drawable.logo,         // ★
            floorMapRes = R.drawable.logo       // ★
        )
    )

    // -----------------------------------------------------
    // 共通商品マスタ
    // -----------------------------------------------------
    private val baseProducts: List<Product> = listOf(
        // 飲料カテゴリ
        Product(
            productId = 1001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "お〜いお茶 500ml",
            category = "飲料",
            price = 108.0,
            stock = 80,
            imageRes = R.drawable.logo      // ★ 全商品 logo
        ),
        Product(
            productId = 1002,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "コカ・コーラ 1.5L",
            category = "飲料",
            price = 198.0,
            stock = 60,
            imageRes = R.drawable.logo
        ),

        // 食品
        Product(
            productId = 2001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "カップラーメン 醤油味",
            category = "食品",
            price = 158.0,
            stock = 120,
            imageRes = R.drawable.logo
        ),
        Product(
            productId = 2002,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "レトルトカレー 中辛",
            category = "食品",
            price = 198.0,
            stock = 90,
            imageRes = R.drawable.logo
        ),

        // 調味料
        Product(
            productId = 3001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "しょうゆ（濃口）1L",
            category = "調味料",
            price = 258.0,
            stock = 50,
            imageRes = R.drawable.logo
        ),

        // 菓子
        Product(
            productId = 4001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "ポテトチップス うすしお味",
            category = "菓子",
            price = 138.0,
            stock = 70,
            imageRes = R.drawable.logo
        ),

        // 日用品
        Product(
            productId = 5001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "ボックスティッシュ 5箱入り",
            category = "日用品",
            price = 298.0,
            stock = 40,
            imageRes = R.drawable.logo
        ),

        // 冷蔵
        Product(
            productId = 6001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "牛乳 1L",
            category = "冷蔵",
            price = 198.0,
            stock = 50,
            imageRes = R.drawable.logo
        ),

        // 冷凍
        Product(
            productId = 7001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "冷凍ギョーザ 12個入り",
            category = "冷凍",
            price = 298.0,
            stock = 60,
            imageRes = R.drawable.logo
        ),

        // その他
        Product(
            productId = 8001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "エコバッグ（Mサイズ）",
            category = "その他",
            price = 198.0,
            stock = 30,
            imageRes = R.drawable.logo
        )
    )

    // -----------------------------------------------------
    // 全店舗 × 全商品 = 展開後の商品一覧
    // -----------------------------------------------------
    private val internalAllProducts: List<Product> by lazy {
        val result = mutableListOf<Product>()
        var runningId = 1000

        stores.forEach { store ->
            baseProducts.forEach { p ->
                result.add(
                    p.copy(
                        productId = runningId++,
                        storeId = store.storeId,
                        storeName = store.storeName
                    )
                )
            }
        }

        result
    }

    // -----------------------------------------------------
    // 公開 API
    // -----------------------------------------------------

    fun getAllStores(): List<Store> = stores

    fun getStoreById(storeId: String): Store? =
        stores.find { it.storeId == storeId }

    fun getProductsByStore(storeId: String): List<Product> =
        internalAllProducts.filter { it.storeId == storeId }

    fun getAllProducts(): List<Product> = internalAllProducts
}
