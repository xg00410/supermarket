package com.example.supermarket.data

import com.example.supermarket.model.Store
import com.example.supermarket.model.Product
import com.example.supermarket.ui.screens.StoreItem
import com.example.supermarket.R

object FakeRepository {

    // ------------------------------
    // 1️⃣ 区域 → 都道府県映射
    // ------------------------------
    val regionMap = mapOf(
        "hokkaido" to listOf("北海道"),
        "tohoku" to listOf("青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県"),
        "kanto" to listOf("東京都", "神奈川県", "千葉県", "埼玉県", "群馬県", "茨城県", "栃木県"),
        "chubu" to listOf("新潟県", "長野県", "山梨県", "静岡県", "愛知県", "岐阜県", "富山県", "石川県", "福井県"),
        "kinki" to listOf("大阪府", "京都府", "兵庫県", "奈良県", "滋賀県", "和歌山県"),
        "kyushu" to listOf("福岡県", "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県")
    )

    // ------------------------------
    // 2️⃣ 店铺（旧数据 + 新增扩展）
    // ------------------------------
    private val stores = listOf(
        // 原有
        Store(
            id = "store001",
            name = "スーパーA 亀戸店",
            address = "東京都江東区亀戸1-2-3",
            distanceMeters = 120,
            openHours = "09:00-23:00",
            floorMapUrl = null
        ),
        Store(
            id = "store002",
            name = "スーパーB 秋葉原店",
            address = "東京都千代田区外神田4-5-6",
            distanceMeters = 480,
            openHours = "24時間営業",
            floorMapUrl = null
        ),
        Store(
            id = "store003",
            name = "スーパーC 川崎店",
            address = "神奈川県川崎市川崎区1-1-1",
            distanceMeters = null,
            openHours = "10:00-21:00",
            floorMapUrl = null
        ),

        // 新增示例
        Store(
            id = "store004",
            name = "イオン札幌店",
            address = "北海道札幌市中央区1-2-3",
            distanceMeters = null,
            openHours = "09:00-22:00",
            floorMapUrl = null
        ),
        Store(
            id = "store005",
            name = "イオン大田店",
            address = "東京都大田区池上1-2-3",
            distanceMeters = null,
            openHours = "10:00-21:00",
            floorMapUrl = null
        ),
        Store(
            id = "store006",
            name = "ローソン梅田店",
            address = "大阪府大阪市北区梅田1-1-1",
            distanceMeters = null,
            openHours = "24時間営業",
            floorMapUrl = null
        )
    )

    // ------------------------------
    // 3️⃣ 商品数据（原样保留）
    // ------------------------------
    private val products = listOf(
        Product(
            id = "p001",
            storeId = "store001",
            name = "お〜いお茶 500ml",
            category = "飲料",
            priceYen = 108,
            stock = 42,
            imageUrl = null
        ),
        Product(
            id = "p002",
            storeId = "store001",
            name = "カップラーメン 醤油",
            category = "食品",
            priceYen = 158,
            stock = 13,
            imageUrl = null
        ),
        Product(
            id = "p010",
            storeId = "store002",
            name = "コカ・コーラ 1.5L",
            category = "飲料",
            priceYen = 198,
            stock = 5,
            imageUrl = null
        )
    )

    // ------------------------------
    // 4️⃣ 公共访问方法
    // ------------------------------

    fun getStores(): List<Store> = stores

    fun getStoreById(id: String): Store? = stores.find { it.id == id }

    fun getProductsByStore(storeId: String): List<Product> =
        products.filter { it.storeId == storeId }

    /** 根据区域Key获取都道府县 */
    fun getPrefecturesByRegion(regionKey: String): List<String> {
        return regionMap[regionKey] ?: emptyList()
    }

    /** 根据都道府县名筛选店铺 */
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

    /** 模糊搜索（支持店名、地址、地区） */
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

    // ------------------------------
    // 5️⃣ 默认图机制
    // ------------------------------
    private fun getDefaultImageForPrefecture(address: String): Int {
        return when {
            address.contains("北海道") -> R.drawable.store_hokkaido1
            address.contains("東京") -> R.drawable.store_hokkaido1
            address.contains("大阪") -> R.drawable.store_hokkaido1
            else -> 0 // 无图 → No Image
        }
    }
}
