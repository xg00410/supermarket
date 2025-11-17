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
    // 店舗マスタ（設計書 Store / Store.Curent location 対応）
    // 本番では DB の stores テーブルと対応する想定。
    // -----------------------------------------------------
    private val stores: List<Store> = listOf(
        Store(
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            address = "神奈川県川崎市川崎区〇〇1-2-3",
            latitude = 35.5300,
            longitude = 139.7000,
            imageRes = R.drawable.store_kawasaki,    // 店舗一覧用写真
            floorMapRes = R.drawable.floor_kawasaki  // 店舗内平面図
        ),
        Store(
            storeId = "S002",
            storeName = "スーパーマーケット蒲田駅西口店",
            address = "東京都大田区西蒲田4-5-6",
            latitude = 35.5620,
            longitude = 139.7160,
            imageRes = R.drawable.store_kamata,
            floorMapRes = R.drawable.floor_kamata
        ),
        Store(
            storeId = "S003",
            storeName = "スーパーマーケット品川高輪店",
            address = "東京都港区高輪3-4-5",
            latitude = 35.6280,
            longitude = 139.7390,
            imageRes = R.drawable.store_takanawa,
            floorMapRes = R.drawable.floor_takanawa
        )
        // ★ 必要に応じて店舗を追加可能
    )

    // -----------------------------------------------------
    // 商品マスタ（設計書 menu 対応）
    // - ここではサンプルのみ記述。
    // - 実際には 100 商品まで拡張予定。
    // - 全店舗で同一商品を扱うイメージで「共通商品マスタ」として定義。
    // -----------------------------------------------------
    private val baseProducts: List<Product> = listOf(
        // 飲料カテゴリ
        Product(
            productId = 1001,
            storeId = "S001",          // 初期割り当て店舗（後で複製して他店舗にも展開）
            storeName = "スーパーマーケット川崎駅前店",
            name = "お〜いお茶 500ml",
            category = "飲料",
            price = 108.0,
            stock = 80,
            imageRes = R.drawable.drink_oolong_500
        ),
        Product(
            productId = 1002,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "コカ・コーラ 1.5L",
            category = "飲料",
            price = 198.0,
            stock = 60,
            imageRes = R.drawable.drink_cola_15
        ),
        // 食品カテゴリ
        Product(
            productId = 2001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "カップラーメン 醤油味",
            category = "食品",
            price = 158.0,
            stock = 120,
            imageRes = R.drawable.food_cupnoodle_syoyu
        ),
        Product(
            productId = 2002,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "レトルトカレー 中辛",
            category = "食品",
            price = 198.0,
            stock = 90,
            imageRes = R.drawable.food_retort_curry
        ),
        // 調味料カテゴリ
        Product(
            productId = 3001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "しょうゆ（濃口）1L",
            category = "調味料",
            price = 258.0,
            stock = 50,
            imageRes = R.drawable.seasoning_soy_sauce
        ),
        // 菓子カテゴリ
        Product(
            productId = 4001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "ポテトチップス うすしお味",
            category = "菓子",
            price = 138.0,
            stock = 70,
            imageRes = R.drawable.snack_potato_chips
        ),
        // 日用品カテゴリ
        Product(
            productId = 5001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "ボックスティッシュ 5箱入り",
            category = "日用品",
            price = 298.0,
            stock = 40,
            imageRes = R.drawable.daily_tissue_box
        ),
        // 冷蔵カテゴリ
        Product(
            productId = 6001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "牛乳 1L",
            category = "冷蔵",
            price = 198.0,
            stock = 50,
            imageRes = R.drawable.refrigerated_milk_1l
        ),
        // 冷凍カテゴリ
        Product(
            productId = 7001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "冷凍ギョーザ 12個入り",
            category = "冷凍",
            price = 298.0,
            stock = 60,
            imageRes = R.drawable.frozen_gyoza
        ),
        // その他カテゴリ
        Product(
            productId = 8001,
            storeId = "S001",
            storeName = "スーパーマーケット川崎駅前店",
            name = "エコバッグ（Mサイズ）",
            category = "その他",
            price = 198.0,
            stock = 30,
            imageRes = R.drawable.other_ecobag_m
        )
        // ★ ここから同じ形式で商品を増やしていけばOK（最終的に100件）
    )

    // -----------------------------------------------------
    // A案：共通商品マスタを全店舗に展開するイメージ
    //   - 1つの baseProducts を各店舗用に複製し storeId/storeName を差し替え
    //   - 「同じ商品が複数店舗に存在する」設計
    // -----------------------------------------------------
    private val allProducts: List<Product> by lazy {
        val result = mutableListOf<Product>()
        var runningId = 1000

        stores.forEach { store ->
            baseProducts.forEach { p ->
                result.add(
                    p.copy(
                        productId = runningId++,        // 各店舗ごとに一意なIDを振り直す
                        storeId = store.storeId,
                        storeName = store.storeName
                    )
                )
            }
        }
        result
    }

    // -----------------------------------------------------
    // 公開用関数群（画面から利用）
    // -----------------------------------------------------

    /**
     * 全店舗一覧を取得
     */
    fun getAllStores(): List<Store> = stores

    /**
     * 店舗IDから店舗情報を取得
     */
    fun getStoreById(storeId: String): Store? =
        stores.find { it.storeId == storeId }

    /**
     * 店舗IDに紐づく商品一覧を取得
     * menu画面などで使用。
     */
    fun getProductsByStore(storeId: String): List<Product> =
        allProducts.filter { it.storeId == storeId }

    /**
     * 全ての商品を取得
     * list2 や 管理機能で全件チェックしたい場合に使用可能。
     */
    fun getAllProducts(): List<Product> = allProducts
}
