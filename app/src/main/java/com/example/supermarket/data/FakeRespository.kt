package com.example.supermarket.data

import com.example.supermarket.model.Store
import com.example.supermarket.model.Product
import com.example.supermarket.model.StoreItem
import com.example.supermarket.R

/**
 * 🏪 FakeRepository.kt
 * -----------------------------------------
 * 📘 超市导购系统 - 测试数据仓库
 * デモ用の店舗・商品データを管理するリポジトリ
 * -----------------------------------------
 * ⚙️ 说明（中日对照）：
 * 本文件提供了区域、都道府县、店铺、商品等假数据，
 * 用于前端界面测试与画面跳转演示。
 * 実際のAPI連携の代わりに利用されるデモデータを管理。
 */
object FakeRepository {

    // ------------------------------------------------------------
    // 1️⃣ 区域 → 都道府県 映射 / 地域キーと都道府県の対応関係
    // ------------------------------------------------------------
    val regionMap = mapOf(
        "hokkaido" to listOf("北海道"),
        "tohoku" to listOf("青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県"),
        "kanto" to listOf("東京都", "神奈川県", "千葉県", "埼玉県", "群馬県", "茨城県", "栃木県"),
        "chubu" to listOf("新潟県", "長野県", "山梨県", "静岡県", "愛知県", "岐阜県", "富山県", "石川県", "福井県"),
        "kinki" to listOf("大阪府", "京都府", "兵庫県", "奈良県", "滋賀県", "和歌山県"),
        "kyushu" to listOf("福岡県", "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県")
    )

    // ------------------------------------------------------------
    // 2️⃣ 店铺数据（Store）／ 店舗データ
    // ------------------------------------------------------------
    private val stores = listOf(
        Store("store001", "スーパーA 亀戸店", "東京都江東区亀戸1-2-3", 120, "09:00-23:00", null),
        Store("store002", "スーパーB 秋葉原店", "東京都千代田区外神田4-5-6", 480, "24時間営業", null),
        Store("store003", "スーパーC 川崎店", "神奈川県川崎市川崎区1-1-1", null, "10:00-21:00", null),
        Store("store004", "イオン札幌店", "北海道札幌市中央区1-2-3", null, "09:00-22:00", null),
        Store("store005", "イオン大田店", "東京都大田区池上1-2-3", null, "10:00-21:00", null),
        Store("store006", "ローソン梅田店", "大阪府大阪市北区梅田1-1-1", null, "24時間営業", null)
    )

    // ------------------------------------------------------------
    // 3️⃣ 商品数据（Product）／ 商品データ
    // ------------------------------------------------------------
    private val products = listOf(
        Product("p001", "store001", "お〜いお茶 500ml", "飲料", 108, 42, null),
        Product("p002", "store001", "カップラーメン 醤油", "食品", 158, 13, null),
        Product("p010", "store002", "コカ・コーラ 1.5L", "飲料", 198, 5, null)
    )

    // ------------------------------------------------------------
    // 4️⃣ 公共访问方法（对外API）／ 公開メソッド
    // ------------------------------------------------------------

    /** 🏪 获取所有店铺／すべての店舗を取得 */
    fun getStores(): List<Store> = stores

    /** 🔍 根据店铺ID获取单个店铺／店舗IDで1件の店舗を取得 */
    fun getStoreById(id: String): Store? = stores.find { it.id == id }

    /** 📦 根据店铺ID获取商品列表／店舗IDに対応する商品一覧を取得 */
    fun getProductsByStore(storeId: String): List<Product> =
        products.filter { it.storeId == storeId }

    /** 🌏 根据区域Key获取都道府县列表／地域キーに基づいて都道府県一覧を取得 */
    fun getPrefecturesByRegion(regionKey: String): List<String> {
        return regionMap[regionKey] ?: emptyList()
    }

    /**
     * 🗾 根据区域Key获取区域内的店铺（返回 Store 类型）
     * 地域キー（例："kanto"）に属する店舗一覧を取得（Store型を返す）
     * 🔹 用于 StoreSelectScreen 搜索功能
     */
    fun getStoresByRegion(regionKey: String): List<Store> {
        val prefectures = regionMap[regionKey] ?: return emptyList()
        return stores.filter { store ->
            prefectures.any { pref -> store.address.contains(pref) }
        }
    }

    /**
     * 🏙️ 根据都道府县筛选店铺（返回 StoreItem 类型）
     * 都道府県で店舗をフィルタリング（StoreItem型で返す）
     * 🔹 用于 StoreMapExpandedScreen
     */
    fun getStoresByPrefecture(prefecture: String): List<StoreItem> {
        return stores
            .filter { it.address.contains(prefecture) }
            .map {
                StoreItem(
                    id = it.id,
                    name = it.name,
                    address = it.address,
                    imageRes = getDefaultImageForPrefecture(prefecture)
                )
            }
    }

    /** 🔎 模糊搜索：支持店名、地址、地区／曖昧検索：店名・住所・地域対応 */
    fun searchStores(keyword: String): List<StoreItem> {
        if (keyword.isBlank()) {
            return stores.map {
                StoreItem(it.id, it.name, it.address, getDefaultImageForPrefecture(it.address))
            }
        }
        return stores.filter {
            it.name.contains(keyword, ignoreCase = true) ||
                    it.address.contains(keyword, ignoreCase = true)
        }.map {
            StoreItem(it.id, it.name, it.address, getDefaultImageForPrefecture(it.address))
        }
    }

    // ------------------------------------------------------------
    // 5️⃣ 默认图片机制／デフォルト画像処理
    // ------------------------------------------------------------
    private fun getDefaultImageForPrefecture(address: String): Int {
        return when {
            address.contains("北海道") -> R.drawable.store_hokkaido1
            address.contains("東京") -> R.drawable.store_hokkaido1
            address.contains("大阪") -> R.drawable.store_hokkaido1
            else -> 0 // 无图 → No Image
        }
    }
}
