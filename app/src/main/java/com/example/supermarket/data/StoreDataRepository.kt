// =========================================================
// File: StoreDataRepository.kt
// 役割:
//   - 店舗情報(Store) と 地域/都道府県マスタを管理するリポジトリ。
//   - Menu 画面などで取得した「DB 商品リスト」のキャッシュも保持する。
//   - ★ ダミー商品データ生成ロジックはすべて削除済み。
// =========================================================

package com.example.supermarket.data

import com.example.supermarket.R
import com.example.supermarket.models.Product
import com.example.supermarket.models.Store
import com.example.supermarket.models.Prefecture
import com.example.supermarket.models.Region

/**
 * StoreDataRepository
 * 🇯🇵 店舗および地域マスタ＋DB商品キャッシュを提供するオブジェクト。
 * 🇨🇳 负责提供店铺与地区主数据，同时保存从DB取得的商品缓存。
 */
object StoreDataRepository {

    // -----------------------------------------------------
    // DB から取得した「店舗別商品リスト」のキャッシュ
    //   key: storeId
    //   value: その店舗の Product 一覧
    //   - MenuScreen で取得した結果をここに保存し，
    //     CartScreen / RouteScreen から在庫確認などに利用する。
    // -----------------------------------------------------
    val latestDbProducts: MutableMap<String, List<Product>> = mutableMapOf()

    // -----------------------------------------------------
    // 店舗マスタ
    //   ※ 現時点では店舗情報はローカル固定。
    //      store_id 自体は DB の stores テーブルと対応している。
    // -----------------------------------------------------
    private val stores: List<Store> = listOf(
        Store(
            storeId = "S001",
            storeName = "スーパーA 新宿店",
            address = "東京都新宿区○○1-1-1",
            latitude = 35.6900,
            longitude = 139.7000,
            imageRes = R.drawable.logo,              // 店舗一覧用画像
            floorMapRes = R.drawable.store_floor_map // 店内マップ画像
        ),
        Store(
            storeId = "S002",
            storeName = "スーパーB 横浜店",
            address = "神奈川県横浜市△△2-2-2",
            latitude = 35.4656,
            longitude = 139.6223,
            imageRes = R.drawable.logo,
            floorMapRes = R.drawable.store_floor_map
        ),
        Store(
            storeId = "S003",
            storeName = "スーパーC 千葉店",
            address = "千葉県千葉市□□3-3-3",
            latitude = 35.6074,
            longitude = 140.1065,
            imageRes = R.drawable.logo,
            floorMapRes = R.drawable.store_floor_map
        )
    )

    // -----------------------------------------------------
    // 店舗ID → 都道府県ID の簡易マッピング
    // -----------------------------------------------------
    private val storePrefectureMap: Map<String, Int> = mapOf(
        "S001" to 13, // 東京都
        "S002" to 14, // 神奈川県
        "S003" to 11  // 埼玉県（住所は仮のまま）
    )

    // =====================================================
    //  公開 API（店舗）
    // =====================================================

    /**
     * 登録されている全店舗一覧を返す。
     */
    fun getAllStores(): List<Store> = stores

    /**
     * 店舗IDから1件の店舗情報を取得する。
     */
    fun getStoreById(storeId: String): Store? =
        stores.find { it.storeId == storeId }

    // =====================================================
    //  店舗選択3層構造 用の API ＋ マスタ
    // =====================================================

    /**
     * 地域ID から、その地域に属する都道府県一覧を取得する。
     * （北海道 / 東北 / 関東 …）
     */
    fun getPrefecturesByRegion(regionId: Int): List<Prefecture> =
        prefectures.filter { it.regionId == regionId }

    /**
     * 地域ID から、「店舗が存在する都道府県だけ」を取得する。
     */
    fun getPrefecturesWithStores(regionId: Int): List<Prefecture> {
        val regionPrefs = prefectures.filter { it.regionId == regionId }
        return regionPrefs.filter { pref ->
            stores.any { store ->
                storePrefectureMap[store.storeId] == pref.prefectureId
            }
        }
    }

    /**
     * 都道府県ID から、その都道府県に属する店舗一覧を取得する。
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

    // -----------------------------------------------------
    //  8地域 ＋ 47都道府県のマスターデータ
    // -----------------------------------------------------

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
}
