// =========================================================
// File: StoreDataRepository.kt
// 画面名: 地域・都道府県マスターデータ管理
// 役割:
//   - 日本の地域（8区）と 47都道府県のマスターデータを提供する。
//   - 店舗データは API（get_stores.php）で取得するため保持しない。
// =========================================================

package com.example.supermarket.data

import com.example.supermarket.models.Region
import com.example.supermarket.models.Prefecture

object StoreDataRepository {

    // =========================================================
    // 8 地域（Region）
// =========================================================
    val regions: List<Region> = listOf(
        Region(1, "北海道"),
        Region(2, "東北"),
        Region(3, "関東"),
        Region(4, "中部"),
        Region(5, "近畿"),
        Region(6, "中国"),
        Region(7, "四国"),
        Region(8, "九州・沖縄")
    )

    // =========================================================
    // 47 都道府県（Prefecture）
    // regionId により各都道府県がどの地域に属するか判断する
    // =========================================================
    val prefectures: List<Prefecture> = listOf(
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

    // =========================================================
    // 地域IDから、その地域に属する都道府県一覧を取得する
    // =========================================================
    fun getPrefecturesByRegion(regionId: Int): List<Prefecture> {
        return prefectures.filter { it.regionId == regionId }
    }

    // =========================================================
    // 都道府県IDから都道府県名を取得（必要であれば使用）
    // =========================================================
    fun getPrefectureName(prefectureId: Int): String? {
        return prefectures.firstOrNull { it.prefectureId == prefectureId }?.prefectureName
    }
}
