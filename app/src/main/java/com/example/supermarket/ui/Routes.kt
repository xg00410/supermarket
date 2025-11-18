// =========================================================
// File: Routes.kt
// 役割:
//   - アプリ内の全画面のルート（識別子）を一元管理する。
//   - storeId などのパラメータ付きルートもここで定義する。
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui


// 画面遷移用ルート定義 / 画面跳转路由定义
object Routes {

    // ------------------------
    // メイン / 認証系
    // ------------------------
    const val MAIN = "main"
    const val LOGIN = "login"
    const val LOGIN_SUCCESS = "login_success"
    const val REGISTER = "register"
    const val REGISTER_SUCCESS = "register_success"
    const val FIND_PASSWORD = "find_password"
    const val PASSWORD_RESET = "password_reset"
    const val PASSWORD_RESET_SUCCESS = "password_reset_success"

    // ------------------------
    // 店舗選択・地図
    // ------------------------
    const val STORE_SELECT = "store_select"
    const val GPS_PERMISSION = "gps_permission"
    const val STORE_MAP = "store_map"                 // 店舗地図検索(store_Current)
    const val STORE_REGION = "store_region"           // 都道府県一覧
    const val STORE_REGION_DETAIL = "store_region_detail" // ※今は未使用でもOK
    const val STORE_DETAIL = "store_detail"           // 店舗拡大(store_拡大)
    const val STORE_MAP_EXPANDED = "store_map_expanded" // 店舗内マップ拡大

    // ------------------------
    // 商品 / カート / ルート
    // ------------------------
    const val MENU = "menu"          // 商品一覧（店舗）
    const val CART = "cart"          // カート(list)
    const val ROUTE = "route"        // 最短ルート(route)

    // ------------------------
    // マイページ系
    // ------------------------
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile_edit"
    const val SETTINGS = "settings"
    const val TERMS = "terms"
    const val ORDER_HISTORY = "order_history"

    // ------------------------
    // ヘルプ
    // ------------------------
    const val HELP = "help"
}
