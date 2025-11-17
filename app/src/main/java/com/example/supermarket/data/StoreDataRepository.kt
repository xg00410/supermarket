package com.example.supermarket.data

import com.example.supermarket.models.Product
import com.example.supermarket.models.Store
import com.example.supermarket.models.StoreItem

/**
 * 🧪 StoreDataRepository
 * 🇯🇵 デモ用の店舗・商品データを自動生成するリポジトリ
 * 🇨🇳 用于演示的店铺/商品假数据仓库（自动生成）
 *
 * 特徴 / 特点：
 * - 日本 47 都道府県 × 各 2 店舗 = 94 店舗
 * - 各店舗につき 10 商品を自動生成（合計 940 商品）
 * - 住所には必ず都道府県名を含める → 都道府県検索が可能
 */
object StoreDataRepository {

    // 🌏 地域 → 都道府県
    // 画面側（StoreMapScreen / StoreRegionScreen）から利用される
    val regionMap: Map<String, List<String>> = mapOf(
        "hokkaido" to listOf("北海道"),
        "tohoku" to listOf("青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県"),
        "kanto" to listOf("東京都", "神奈川県", "埼玉県", "千葉県", "群馬県", "栃木県", "茨城県"),
        "chubu" to listOf("新潟県", "長野県", "山梨県", "静岡県", "愛知県", "岐阜県", "富山県", "石川県", "福井県"),
        "kinki" to listOf("大阪府", "京都府", "兵庫県", "奈良県", "滋賀県", "和歌山県"),
        "chugoku" to listOf("鳥取県", "島根県", "岡山県", "広島県", "山口県"),
        "shikoku" to listOf("徳島県", "香川県", "愛媛県", "高知県"),
        "kyushu" to listOf("福岡県", "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県")
    )

    // 🗾 全都道府県のリスト / 所有都道府县列表
    private val allPrefectures: List<String> = regionMap.values.flatten()

    // 店舗名のパターン / 店铺名模板
    private val storeNamePatterns = listOf(
        "%sスーパー %s店",
        "%sフードマーケット %s店",
        "%sマート %s店",
        "%sフレッシュ %s店"
    )

    // 市区名のダミー / 虚拟城市名
    private val citySamples = listOf("中央", "駅前", "本町", "南", "北", "東", "西")

    // 営業時間候補 / 营业时间候选
    private val openHourPatterns = listOf(
        "9:00〜21:00",
        "9:00〜22:00",
        "10:00〜20:00",
        "10:00〜21:00",
        "8:00〜22:00"
    )

    // 商品カテゴリ / 商品类别
    private val categories = listOf("飲料", "食品", "お菓子", "日用品", "冷凍食品")

    // カテゴリごとの代表商品名候補 / 每个类别的代表商品名称候选
    private val baseProductNames: Map<String, List<String>> = mapOf(
        "飲料" to listOf(
            "お〜いお茶 500ml",
            "コカ・コーラ 1.5L",
            "ミネラルウォーター 2L",
            "烏龍茶 500ml",
            "オレンジジュース 1L"
        ),
        "食品" to listOf(
            "カップラーメン 醤油",
            "カレールー 中辛",
            "パスタ 500g",
            "米 5kg",
            "食パン 6枚切り"
        ),
        "お菓子" to listOf(
            "ポテトチップス うすしお",
            "チョコレートバー",
            "ビスケット",
            "グミキャンディ",
            "せんべい"
        ),
        "日用品" to listOf(
            "ボックスティッシュ 5個パック",
            "トイレットペーパー 12ロール",
            "食器用洗剤",
            "洗濯用洗剤",
            "歯ブラシ 2本セット"
        ),
        "冷凍食品" to listOf(
            "冷凍餃子 12個入",
            "冷凍ピザ",
            "冷凍チャーハン",
            "冷凍うどん 5玉",
            "冷凍フライドポテト"
        )
    )

    /**
     * 🏬 店舗リスト / 店铺列表
     * ルール：
     * - 各都道府県に 2 店舗自動生成
     * - ID 例: store_tokyo_001, store_tokyo_002
     * - 住所：必ず「都道府県名 + ○○市○○1-1-1」の形式
     */
    private val stores: List<Store> = buildStores()

    /**
     * 🛒 商品リスト / 商品列表
     * - 各店舗 10 商品自動生成
     * - product_id は 1 から通し番号
     * - category: 「飲料」「食品」「お菓子」「日用品」「冷凍食品」など
     */
    private val products: List<Product> = buildProducts()

    // ---------------- 内部生成ロジック / 内部生成逻辑 ----------------

    // 都道府県名 → “読みやすいキー”（ID に使用）
    private fun prefectureToKey(pref: String): String {
        return when (pref) {
            "北海道" -> "hokkaido"
            "青森県" -> "aomori"
            "岩手県" -> "iwate"
            "宮城県" -> "miyagi"
            "秋田県" -> "akita"
            "山形県" -> "yamagata"
            "福島県" -> "fukushima"
            "東京都" -> "tokyo"
            "神奈川県" -> "kanagawa"
            "埼玉県" -> "saitama"
            "千葉県" -> "chiba"
            "群馬県" -> "gunma"
            "栃木県" -> "tochigi"
            "茨城県" -> "ibaraki"
            "新潟県" -> "niigata"
            "長野県" -> "nagano"
            "山梨県" -> "yamanashi"
            "静岡県" -> "shizuoka"
            "愛知県" -> "aichi"
            "岐阜県" -> "gifu"
            "富山県" -> "toyama"
            "石川県" -> "ishikawa"
            "福井県" -> "fukui"
            "大阪府" -> "osaka"
            "京都府" -> "kyoto"
            "兵庫県" -> "hyogo"
            "奈良県" -> "nara"
            "滋賀県" -> "shiga"
            "和歌山県" -> "wakayama"
            "鳥取県" -> "tottori"
            "島根県" -> "shimane"
            "岡山県" -> "okayama"
            "広島県" -> "hiroshima"
            "山口県" -> "yamaguchi"
            "徳島県" -> "tokushima"
            "香川県" -> "kagawa"
            "愛媛県" -> "ehime"
            "高知県" -> "kochi"
            "福岡県" -> "fukuoka"
            "佐賀県" -> "saga"
            "長崎県" -> "nagasaki"
            "熊本県" -> "kumamoto"
            "大分県" -> "oita"
            "宮崎県" -> "miyazaki"
            "鹿児島県" -> "kagoshima"
            "沖縄県" -> "okinawa"
            else -> pref
        }
    }

    /**
     * 店舗リスト生成 / 生成全部店铺
     */
    private fun buildStores(): List<Store> {
        val result = mutableListOf<Store>()

        allPrefectures.forEach { pref ->
            val key = prefectureToKey(pref)

            // 各都道府県につき 2 店舗生成 / 每个都道府县 2 家店
            repeat(2) { index ->
                val number = index + 1
                val storeId = "store_${key}_${"%03d".format(number)}"

                val storeNamePattern = storeNamePatterns[(number - 1) % storeNamePatterns.size]
                val city = citySamples[(number - 1) % citySamples.size]
                val storeName = storeNamePattern.format(
                    pref.replace("県", "").replace("府", "").replace("都", ""),
                    city
                )

                val address = "${pref}${city}市中央${number}-1-${number}"
                val hours = openHourPatterns[(number - 1) % openHourPatterns.size]

                result.add(
                    Store(
                        id = storeId,
                        name = storeName,
                        address = address,
                        distanceMeters = null,       // 将来 GPS 用で拡張可 / 将来可用于 GPS
                        openHours = hours,
                        floorMapUrl = null           // 今は未使用 / 目前不用
                    )
                )
            }
        }

        return result
    }

    /**
     * 商品リスト生成 / 生成全部商品
     * 各店舗 10 商品：カテゴリをローテーションしながら作成
     */
    private fun buildProducts(): List<Product> {
        val list = mutableListOf<Product>()
        var currentId = 1

        for (store in stores) {
            // 各店 10 商品生成
            repeat(10) { idx ->
                val category = categories[idx % categories.size]
                val nameList = baseProductNames[category] ?: listOf("汎用商品")
                val baseName = nameList[idx % nameList.size]

                // 价格随机一点，跟 idx、storeId 混合
                val price = when (category) {
                    "飲料" -> 90 + (idx * 5)
                    "食品" -> 150 + (idx * 10)
                    "お菓子" -> 100 + (idx * 8)
                    "日用品" -> 200 + (idx * 12)
                    "冷凍食品" -> 250 + (idx * 15)
                    else -> 100 + (idx * 5)
                }.toDouble()

                val stock = 20 + (idx * 5)

                list.add(
                    Product(
                        productId = currentId,
                        storeId = store.id,
                        name = baseName,
                        category = category,
                        price = price,
                        stock = stock,
                        imageUrl = null      // 画像は今は未使用 / 目前不用图片
                    )
                )

                currentId++
            }
        }

        return list
    }

    // ---------------- 外部公開メソッド / 对外公开的方法 ----------------

    /** 🏬 全店舗取得 / 获取全部店铺 */
    fun getStores(): List<Store> = stores

    /** 🏬 店舗IDから取得 / 通过 storeId 获取店铺 */
    fun getStoreById(storeId: String): Store? = stores.find { it.id == storeId }

    /** 🛒 店舗ごとの商品取得 / 获取某店的全部商品 */
    fun getProductsByStore(storeId: String): List<Product> =
        products.filter { it.storeId == storeId }

    /** 🌏 地域キー（"kanto" など）から都道府県一覧を取得 */
    fun getPrefecturesByRegion(region: String): List<String> =
        regionMap[region] ?: emptyList()

    /** 🌏 地域キー一覧 / 所有区域 key 列表 */
    fun getAllRegions(): List<String> = regionMap.keys.toList()

    /** 🏬 都道府県名から店舗一覧を取得 / 根据都道府县名获取其下店铺 */
    fun getStoresByPrefecture(prefecture: String): List<Store> =
        stores.filter { it.address.contains(prefecture) }

    /** 🌏 地域キーから店舗一覧を取得 / 根据区域 key 获取该区域所有店铺 */
    fun getStoresByRegion(region: String): List<Store> {
        val prefs = regionMap[region] ?: return emptyList()
        return stores.filter { store ->
            prefs.any { pref -> store.address.contains(pref) }
        }
    }

    /**
     * 📋 StoreItem 用の簡易リスト
     * StoreMapExpandedScreen などで「店舗一覧カード表示」に使用
     */
    fun getStoreItems(): List<StoreItem> =
        stores.map {
            StoreItem(
                id = it.id,
                name = it.name,
                address = it.address,
                imageRes = 0      // 今は画像未設定 / 暂时不用图片
            )
        }
}
