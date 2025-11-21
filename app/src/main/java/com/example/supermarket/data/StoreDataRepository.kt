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
import com.example.supermarket.models.Prefecture
import com.example.supermarket.models.Region
import kotlin.random.Random

/**
 * StoreDataRepository
 * 店舗および商品データを提供するオブジェクト。
 */
object StoreDataRepository {
    object StoreDataRepository {

        // -----------------------------------------------------
        // データ取得モード設定
        //   false: ダミーデータ（ローカルスタブ）
        //   true : DB + PHP(API) 経由で取得
        // -----------------------------------------------------
        var useDatabaseMode: Boolean = false


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
            floorMapRes = R.drawable.store_floor_map   // 共通の店内マップ画像

        ),
        Store(
            storeId = "S002",
            storeName = "スーパーB 横浜店",
            address = "神奈川県横浜市△△2-2-2",
            latitude = 35.4656,
            longitude = 139.6223,
            imageRes = R.drawable.logo,
            floorMapRes = R.drawable.store_floor_map   // 共通の店内マップ画像

        ),
        Store(
            storeId = "S003",
            storeName = "スーパーC 千葉店",
            address = "千葉県千葉市□□3-3-3",
            latitude = 35.6074,
            longitude = 140.1065,
            imageRes = R.drawable.logo,
            floorMapRes = R.drawable.store_floor_map   // 共通の店内マップ画像

        )
    )

    // -----------------------------------------------------
    // 店舗ID → 都道府県ID の簡易マッピング
    //   - 千葉県（ID=12）はあえて紐づけず、「店舗なし」の例として扱う。
    //   - 関東(regionId=3)内では、埼玉(11)・東京(13)・神奈川(14)のみ店舗あり。
    // -----------------------------------------------------
    private val storePrefectureMap: Map<String, Int> = mapOf(
        "S001" to 13, // 東京都
        "S002" to 14, // 神奈川県
        "S003" to 11  // 埼玉県（住所は仮のまま）
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
            repeat(13) {
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
    //   - productId は「店舗インデックス×1000 + テンプレートID」。
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
    // 公開 API（店舗・商品）
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

    // =========================================================
    //  店舗選択3層構造 用の API ＋ マスタ
    // =========================================================

    /**
     * 地域ID から、その地域に属する都道府県一覧を取得する。
     * （北海道 / 東北 / 関東 …）
     */
    fun getPrefecturesByRegion(regionId: Int): List<Prefecture> =
        prefectures.filter { it.regionId == regionId }

    /**
     * 地域ID から、「店舗が存在する都道府県だけ」を取得する。
     * 例:
     *   - 関東(regionId=3) の中でも、千葉県(prefectureId=12) に店舗が無い場合は返さない。
     */
    fun getPrefecturesWithStores(regionId: Int): List<Prefecture> {
        // 該当地域の全都道府県
        val regionPrefs = prefectures.filter { it.regionId == regionId }
        // 少なくとも1件でも店舗が存在する都道府県だけを返す
        return regionPrefs.filter { pref ->
            stores.any { store ->
                storePrefectureMap[store.storeId] == pref.prefectureId
            }
        }
    }

    /**
     * 都道府県ID から、その都道府県に属する店舗一覧を取得する。
     * storePrefectureMap に基づいて、該当する店舗のみを返す。
     */
    fun getStoresByPrefecture(prefectureId: Int): List<Store> {
        return stores.filter { store ->
            storePrefectureMap[store.storeId] == prefectureId
        }
    }

    /**
     * 店舗名／住所に対する簡易全文検索。
     */
    fun searchStores(keyword: String): List<Store> {
        val lower = keyword.lowercase()
        return stores.filter { store ->
            store.storeName.lowercase().contains(lower) ||
                    store.address.lowercase().contains(lower)
        }
    }

    // ---------------------------------------------------------
    //  8地域 ＋ 47都道府県のマスターデータ
    // ---------------------------------------------------------

    private val regions = listOf(
        Region(1, "北海道"),
        Region(2, "東北"),
        Region(3, "関東"),
        Region(4, "中部"),
        Region(5, "近畿"),
        Region(6, "中国"),
        Region(7, "四国"),
        Region(8, "九州・沖縄")
    )

    private val prefectures = listOf(
        Prefecture(1, 1, "北海道"),
        Prefecture(2, 2, "青森県"),
        Prefecture(3, 2, "岩手県"),
        Prefecture(4, 2, "宮城県"),
        Prefecture(5, 2, "秋田県"),
        Prefecture(6, 2, "山形県"),
        Prefecture(7, 2, "福島県"),
        Prefecture(8, 3, "茨城県"),
        Prefecture(9, 3, "栃木県"),
        Prefecture(10, 3, "群馬県"),
        Prefecture(11, 3, "埼玉県"),
        Prefecture(12, 3, "千葉県"),
        Prefecture(13, 3, "東京都"),
        Prefecture(14, 3, "神奈川県"),
        Prefecture(15, 4, "新潟県"),
        Prefecture(16, 4, "富山県"),
        Prefecture(17, 4, "石川県"),
        Prefecture(18, 4, "福井県"),
        Prefecture(19, 4, "山梨県"),
        Prefecture(20, 4, "長野県"),
        Prefecture(21, 4, "岐阜県"),
        Prefecture(22, 4, "静岡県"),
        Prefecture(23, 4, "愛知県"),
        Prefecture(24, 5, "三重県"),
        Prefecture(25, 5, "滋賀県"),
        Prefecture(26, 5, "京都府"),
        Prefecture(27, 5, "大阪府"),
        Prefecture(28, 5, "兵庫県"),
        Prefecture(29, 5, "奈良県"),
        Prefecture(30, 5, "和歌山県"),
        Prefecture(31, 6, "鳥取県"),
        Prefecture(32, 6, "島根県"),
        Prefecture(33, 6, "岡山県"),
        Prefecture(34, 6, "広島県"),
        Prefecture(35, 6, "山口県"),
        Prefecture(36, 7, "徳島県"),
        Prefecture(37, 7, "香川県"),
        Prefecture(38, 7, "愛媛県"),
        Prefecture(39, 7, "高知県"),
        Prefecture(40, 8, "福岡県"),
        Prefecture(41, 8, "佐賀県"),
        Prefecture(42, 8, "長崎県"),
        Prefecture(43, 8, "熊本県"),
        Prefecture(44, 8, "大分県"),
        Prefecture(45, 8, "宮崎県"),
        Prefecture(46, 8, "鹿児島県"),
        Prefecture(47, 8, "沖縄県")
    )
}}
