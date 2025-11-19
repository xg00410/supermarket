// =========================================================
// File: Routes.kt
// 役割:
//   - アプリ内の全画面のルート（識別子）を一元管理する。
//   - storeId や prefecture など、パラメータ付きルートのベース名もここで定義する。
// 備考:
//   - 実際の NavHost では、必要に応じて
//       "${Routes.STORE_DETAIL}/{storeId}"
//       "${Routes.MENU}/{storeId}"
//       "${Routes.STORE_REGION}/{prefecture}"
//     のように組み立てて使用する。
// =========================================================

package com.example.supermarket.ui

object Routes {

    // ------------------------
    // メイン・認証系
    // ------------------------
    const val MAIN = "main"                          // 起動時メイン画面
    const val LOGIN = "login"                        // ログイン画面
    const val LOGIN_SUCCESS = "login_success"        // ログイン成功
    const val REGISTER = "register"                  // 新規登録
    const val REGISTER_SUCCESS = "register_success"  // 登録成功
    const val FIND_PASSWORD = "find_password"        // パスワード再設定（本人確認）
    const val PASSWORD_RESET = "password_reset"      // パスワード再設定（入力）
    const val PASSWORD_RESET_SUCCESS = "password_reset_success"  // パスワード変更完了

    // ------------------------
    // 店舗選択・位置情報系
    // ------------------------
    const val STORE_SELECT = "store_select"          // 店舗選択（第一層：地域＋キーワード）
    const val STORE_PREFECTURE = "store_prefecture"  // 第二層：都道府県選択
    const val STORE_RESULT = "store_result"          // 第三層：検索結果一覧
    const val STORE_DETAIL = "store_detail"          // 店舗拡大（詳細）
    const val STORE_REGION = "store_region"          // 既存：都道府県別の店舗一覧（旧仕様）
    const val STORE_MAP = "store_map"                // 店舗地図（store_Current）
    const val STORE_MAP_EXPANDED = "store_map_expanded"  // 店舗内マップ拡大
    const val GPS_PERMISSION = "gps_permission"      // 位置情報許可ダイアログ

    // ------------------------
    // 商品・カート・ルート系
    // ------------------------
    const val MENU = "menu"                          // 商品一覧（店舗画面）
    const val CART = "cart"                          // カート（list1）
    const val CART_MANAGE = "cart_manage"            // カート管理（list2）
    const val ROUTE = "route"                        // 最短ルート画面

    // ------------------------
    // マイページ系
    // ------------------------
    const val PROFILE = "profile"                    // マイページトップ
    const val PROFILE_EDIT = "profile_edit"          // 会員情報編集
    const val SETTINGS = "settings"                  // 設定
    const val TERMS = "terms"                        // 利用規約
    const val ORDER_HISTORY = "order_history"        // 購入履歴一覧

    // ------------------------
    // ヘルプ
    // ------------------------
    const val HELP = "help"                          // ヘルプ画面
}
