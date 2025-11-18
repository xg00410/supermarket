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

// 画面遷移用ルート定義
object Routes {

    // ------------------------
    // アプリ起動・メイン
    // ------------------------
    const val MAIN = "main"                  // メイン画面（ロゴ＋ログイン／新規登録）

    // ------------------------
    // 認証系
    // ------------------------
    const val LOGIN = "login"                // ログイン画面
    const val LOGIN_SUCCESS = "login_success"// ログイン成功メッセージ画面
    const val REGISTER = "register"          // 新規登録画面
    const val REGISTER_SUCCESS = "register_success" // 新規登録成功

    // パスワード関連
    const val FIND_PASSWORD = "find_password"           // パスワード再設定（ID＋メール入力）
    const val PASSWORD_RESET = "password_reset"         // パスワード再設定（新パスワード入力）
    const val PASSWORD_RESET_SUCCESS = "password_reset_success" // パスワード変更成功

    // ------------------------
    // 位置情報・GPS 許可
    // ------------------------
    const val GPS_PERMISSION = "gps_permission" // 位置情報の権限確認画面

    // ------------------------
    // 店舗選択・地図・エリア
    // ------------------------
    const val STORE_SELECT = "store_select"    // 店舗選択（検索＋都道府県）
    const val STORE_REGION = "store_region"    // 都道府県別店舗一覧
    const val STORE_MAP = "store_map"          // 現在地から店舗を地図表示
    const val STORE_MAP_EXPANDED = "store_map_expanded" // 店舗地図拡大（storeId付き）
    const val STORE_DETAIL = "store_detail"    // 店舗拡大画面（storeId付き）

    // ------------------------
    // 商品一覧・カート・ルート
    // ------------------------
    const val MENU = "menu"                    // 店舗内の商品一覧（storeId付き）
    const val CART = "cart"                    // カート画面（list）
    const val CART_MANAGE = "cart_manage"      // カート管理画面（list2）
    const val ROUTE = "route"                  // 最短ルート画面

    // ------------------------
    // マイページ系
    // ------------------------
    const val PROFILE = "profile"              // マイページトップ
    const val PROFILE_EDIT = "profile_edit"    // 会員情報編集
    const val SETTINGS = "settings"            // 設定
    const val TERMS = "terms"                  // 利用規約
    const val ORDER_HISTORY = "order_history"  // 購入履歴一覧

    // ------------------------
    // ヘルプ
    // ------------------------
    const val HELP = "help"                    // ヘルプ画面
}
