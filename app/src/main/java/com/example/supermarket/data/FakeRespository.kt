package com.example.supermarket.data

import com.example.supermarket.models.Product
import com.example.supermarket.models.StoreItem
import com.example.supermarket.R

/**
 * 🏪 FakeRepository.kt
 * デモ用の店舗・商品データを管理するリポジトリ
 * 超市导购系统 - 测试数据仓库
 */
object FakeRepository {

    // ------------------------------
    // 1️⃣ 区域映射
    // ------------------------------
    val regionMap = mapOf(
        "hokkaido" to listOf("北海道"),
        "kanto" to listOf("東京都", "神奈川県"),
        "kinki" to listOf("大阪府")
    )

    // ------------------------------
    // 2️⃣ 店铺数据
    // ------------------------------
    private val stores = listOf(
        StoreItem("store001", "スーパーA 亀戸店", "東京都江東区亀戸1-2-3", R.drawable.ic_store_placeholder),
        StoreItem("store002", "スーパーB 秋葉原店", "東京都千代田区外神田4-5-6", R.drawable.ic_store_placeholder),
        StoreItem("store003", "ローソン梅田店", "大阪府大阪市北区梅田1-1-1", R.drawable.ic_store_placeholder)
    )

    // ------------------------------
    // 3️⃣ 商品数据（使用你自己的 Product）
    // ------------------------------
    private val products = listOf(
        Product(product_id = 1, name = "お〜いお茶 500ml", price = 108.0),
        Product(product_id = 2, name = "カップラーメン 醤油", price = 158.0),
        Product(product_id = 3, name = "コカ・コーラ 1.5L", price = 198.0)
    )

    // ------------------------------
    // 4️⃣ 公共方法
    // ------------------------------

    /** 🏪 获取所有店铺 */
    fun getStores(): List<StoreItem> = stores

    /** 📦 获取店铺商品 */
    fun getProductsByStore(storeId: String): List<Product> = products

    /** 🔍 搜索店铺 */
    fun searchStores(keyword: String): List<StoreItem> {
        return stores.filter {
            it.name.contains(keyword, ignoreCase = true) ||
                    it.address.contains(keyword, ignoreCase = true)
        }
    }
}
