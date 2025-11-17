package com.example.supermarket.ui

/**
 * =========================================================
 * ファイル名: Routes.kt
 * 役割: アプリ内全ての画面遷移ID（Route文字列）を一元管理する。
 *       設計書IDとの対応を完全に一致させるための重要ファイル。
 *
 * 注意事項:
 *   - 設計書に存在する画面IDは「そのまま」Routeとして採用する
 *   - 設計書に存在しない画面は「追加機能」として独自IDを使用
 *   - 全ての画面は必ずここで定義し、NavHostから参照する
 *
 * 更新者: 郭
 * 更新日: 2025-11-17
 * =========================================================
 */
object Routes {

    // =====================================================
    // A区（ログイン／新規登録 系列）—— 設計書IDと完全対応
    // =====================================================

    const val MAIN = "Main"                     // メイン画面
    const val LOGIN = "Login"                   // ログイン画面
    const val REGISTER = "Register"             // 新規登録画面
    const val REGISTER_SUCCESS = "Register_suc" // 登録完了
    const val LOGIN_SUCCESS = "Login_suc"       // ログイン成功画面
    const val FIND_PASSWORD = "Findpwd"         // パスワード再設定（認証）
    const val PASSWORD_RESET = "Newpwdset"      // 新パスワード設定
    const val PASSWORD_RESET_SUCCESS = "Newpwdset_suc" // パスワード再設定完了

    // =====================================================
    // B区（店舗／地図 系列）—— 設計書：Store / Store.Curent location
    // =====================================================

    const val STORE_CURRENT = "Store.Curent location"
    // 店舗検索（地図＋現在地）

    const val STORE = "Store"
    // 店舗画面（店内情報＋剖面図）

    // 以下は設計書に存在しないが、実装上必要な追加画面
    const val STORE_REGION = "StoreRegion"               // 追加機能：地域選択
    const val STORE_SELECT = "StoreSelect"               // 追加機能：店舗一覧
    const val STORE_DETAIL = "StoreDetail"               // 追加機能：店舗詳細
    const val STORE_MAP_EXPANDED = "StoreMapExpanded"    // 追加機能：剖面図拡大
    const val GPS_PERMISSION = "GpsPermission"           // 追加機能：位置情報許可

    // =====================================================
    // C区（商品／カート／最短ルート 系列）—— 設計書IDそのまま使用
    // =====================================================

    const val MENU = "menu"      // 商品一覧（カテゴリ＋商品）
    const val LIST = "list"      // カート画面（数量変更／削除）
    const val LIST2 = "list2"    // 確認画面／履歴画面
    const val ROUTE = "route"    // 最短ルート画面

    // =====================================================
    // D区（追加機能：設計書に存在しない画面）
    // =====================================================

    const val PROFILE = "Profile"
    const val PROFILE_EDIT = "ProfileEdit"
    const val SETTINGS = "Settings"
    const val HELP = "Help"
    const val TERMS = "Terms"
    const val ORDER_HISTORY = "OrderHistory"  // list2对应功能，但非設計書名

    // 設計書には存在するが、実装に未追加の画面
    const val LOGOUT = "Logout"               // ログアウト画面
    const val LOGOUT_SUCCESS = "Logout_suc"   // ログアウト完了画面
}
