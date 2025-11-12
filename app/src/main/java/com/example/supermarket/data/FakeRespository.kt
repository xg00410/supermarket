package com.example.supermarket.data

import com.example.supermarket.models.Product
import com.example.supermarket.models.Store
import com.example.supermarket.models.StoreItem

/**
 * 🧠 FakeRepository.kt
 * 開発・テスト用のダミーデータ
 */
object FakeRepository {


    // 🏬 店舗データ（使用命名参数，确保与 Store.kt 的字段顺序无关）
    private val stores = listOf(
        Store("store001", "スーパーA 亀戸店", "東京都江東区亀戸1-2-3", 120, "09:00-23:00", null),
        Store("store002", "スーパーB 秋葉原店", "東京都千代田区外神田4-5-6", 480, "24時間営業", null),
        Store("store003", "スーパーC 川崎店", "神奈川県川崎市川崎区1-1-1", null, "10:00-21:00", null)
    )


    // 🛒 商品データ
    private val products = listOf(
        Product(1, 1, "お〜いお茶 500ml", "飲料", 108.0, 50, null),
        Product(2, 1, "カップラーメン 醤油", "食品", 158.0, 30, null),
        Product(3, 2, "コカ・コーラ 1.5L", "飲料", 198.0, 40, null),
        Product(4, 2, "ポテトチップス うすしお", "お菓子", 128.0, 60, null),
        Product(5, 3, "サッポロ黒ラベル 350ml", "酒類", 198.0, 25, null)
    )

    // 🌏 地域→都道府県（外部から参照するので public）
    val regionMap: Map<String, List<String>> = mapOf(
        "hokkaido" to listOf("北海道"),
        "tohoku" to listOf("青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県"),
        "kanto" to listOf("東京都", "神奈川県", "埼玉県", "千葉県", "群馬県", "栃木県", "茨城県"),
        "chubu" to listOf("新潟県", "長野県", "山梨県", "静岡県", "愛知県", "岐阜県", "富山県", "石川県", "福井県"),
        "kinki" to listOf("大阪府", "京都府", "兵庫県", "奈良県", "滋賀県", "和歌山県"),
        "kyushu" to listOf("福岡県", "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県")
    )

    /** 🏬 全店舗 */
    fun getStores(): List<Store> = stores

    /** 🏬 店舗IDで取得（Store.id は String） */
    fun getStoreById(storeId: String): Store? = stores.find { it.id == storeId }

    /** 🛒 店舗ごとの商品 */
    fun getProductsByStore(storeId: String): List<Product> =
        products.filter { it.storeId.toString() == storeId }


    /** 🌏 地域の都道府県 */
    fun getPrefecturesByRegion(region: String): List<String> =
        regionMap[region] ?: emptyList()

    /** 🗾 全地域キー */
    fun getAllRegions(): List<String> = regionMap.keys.toList()

    /** 🏪 StoreItem 形式 */
    fun getStoreItems(): List<StoreItem> =
        stores.map { StoreItem(it.id, it.name, it.address, 0) }
    /** 🏬 都道府県（県名）から店舗を取得 */
    fun getStoresByPrefecture(prefecture: String): List<Store> {
        return getStores().filter { it.address.contains(prefecture) }
    }
    /** 🏬 地域（例: "kanto"）から店舗を取得 */
    fun getStoresByRegion(region: String): List<Store> {
        val prefectures = regionMap[region] ?: return emptyList()
        return getStores().filter { store ->
            prefectures.any { pref -> store.address.contains(pref) }
        }
    }

}
