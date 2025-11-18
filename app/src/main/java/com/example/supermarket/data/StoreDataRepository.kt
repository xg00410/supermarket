// =========================================================
// File: StoreDataRepository.kt
// 役割:
//   - 店舗情報(Store)と商品情報(Product)を管理する簡易リポジトリ。
//   - 設計書の店舗画面／menu／list／list2／route で共通利用。
//   - 実際のDB／PHP連携を行う前のスタブデータとして使用。
//   - 各店舗ごとに 30〜80 件のランダム商品を持ち、在庫数も 5〜100 の範囲で付与する。
// 備考:
//   - 本来はDB側で一度だけランダム生成して固定すべきだが、
//     ここでは Random(seed) を使って「毎回同じ疑似ランダムデータ」を生成している。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.data

import com.example.supermarket.R
import com.example.supermarket.models.Product
import com.example.supermarket.models.Store
import kotlin.random.Random

/**
 * StoreDataRepository
 * 店舗および商品データを提供するオブジェクト。
 */
object StoreDataRepository {

    // -----------------------------------------------------
    // 店舗マスタ
    // -----------------------------------------------------
    private val stores: List<Store> = listOf(
        Store(
            storeId = "S001",
            storeName = "スーパーA 新宿店",
            address = "東京都新宿区○○1-1-1",
            latitude = 35.6900,
            longitude = 139.7000,
            imageRes = R.drawable.logo,      // 仮: 全店舗共通の画像
            floorMapRes = R.drawable.logo   // 仮: 全店舗共通の店内マップ
        ),
        Store(
            storeId = "S002",
            storeName = "スーパーB 横浜店",
            address = "神奈川県横浜市△△2-2-2",
            latitude = 35.4656,
            longitude = 139.6223,
            imageRes = R.drawable.logo,
            floorMapRes = R.drawable.logo
        ),
        Store(
            storeId = "S003",
            storeName = "スーパーC 千葉店",
            address = "千葉県千葉市□□3-3-3",
            latitude = 35.6074,
            longitude = 140.1065,
            imageRes = R.drawable.logo,
            floorMapRes = R.drawable.logo
        )
    )

    // -----------------------------------------------------
    // 商品テンプレート定義（100件）
    //   - 各テンプレートは「商品名・カテゴリ・価格」のみを持つ。
//   - 実際の Product 生成時に、店舗ごとの storeId / storeName / stock / imageRes を付与。
// -----------------------------------------------------
    private data class ProductTemplate(
        val templateId: Int,
        val name: String,
        val category: String,
        val price: Double
    )

    // 8カテゴリ（設計書に合わせて固定）
    private val categories = listOf(
        "飲料", "食品", "菓子", "調味料",
        "日用品", "冷蔵", "冷凍", "その他"
    )

    // 100件のテンプレートを自動生成
    private val productTemplates: List<ProductTemplate> = buildProductTemplates()

    private fun buildProductTemplates(): List<ProductTemplate> {
        val list = mutableListOf<ProductTemplate>()
        var id = 1

        categories.forEach { category ->
            // 各カテゴリにつき 12〜13品程度を作るイメージ
            repeat(13) { idx ->
                if (id > 100) return@forEach
                list.add(
                    ProductTemplate(
                        templateId = id,
                        name = "$category 商品$id",
                        price = 100.0 + (id * 5),
                        category = category
                    )
                )
                id++
            }
        }

        // 念のため100件に切り詰める
        return list.take(100)
    }

    // -----------------------------------------------------
    // 店舗ごとの商品リスト生成
    //   - 各店舗ごとに 30〜80 件のテンプレートをランダム選択。
//   - 在庫数を 5〜100 の範囲でランダム付与。
//   - productId は「店舗ごとに一意」かつ「全体でも一意」になるように採番。
// -----------------------------------------------------
    private val internalAllProducts: List<Product> = buildStoreProducts()

    private fun buildStoreProducts(): List<Product> {
        val result = mutableListOf<Product>()

        stores.forEachIndexed { storeIndex, store ->
            // 店舗ごとに seed を固定して、毎回同じ疑似乱数列になるようにする
            val random = Random(store.storeId.hashCode())

            // 30〜80件の範囲で商品数を決める
            val productCount = random.nextInt(from = 30, until = 81)

            // テンプレートをシャッフルして先頭から productCount 件を採用
            val selectedTemplates = productTemplates.shuffled(random).take(productCount)

            selectedTemplates.forEach { tmpl ->
                val stock = random.nextInt(from = 5, until = 101)

                // productId は 「店舗インデックス×1000 + テンプレートID」
                val productId = (storeIndex + 1) * 1000 + tmpl.templateId

                val product = Product(
                    productId = productId,
                    storeId = store.storeId,
                    storeName = store.storeName,
                    name = tmpl.name,
                    category = tmpl.category,
                    price = tmpl.price,
                    stock = stock,
                    imageRes = R.drawable.logo   // 仮: 共通ダミー画像
                )
                result.add(product)
            }
        }

        return result
    }

    // -----------------------------------------------------
    // 公開 API
    // -----------------------------------------------------

    /**
     * 登録されている全店舗一覧を返す。
     */
    fun getAllStores(): List<Store> = stores

    /**
     * 店舗IDから1件の店舗情報を取得する。
     */
    fun getStoreById(storeId: String): Store? =
        stores.find { it.storeId == storeId }

    /**
     * 店舗IDに紐づく商品一覧を取得する。
     */
    fun getProductsByStore(storeId: String): List<Product> =
        internalAllProducts.filter { it.storeId == storeId }

    /**
     * 全店舗・全商品の一覧を取得する（デバッグ用）。
     */
    fun getAllProducts(): List<Product> = internalAllProducts
}
