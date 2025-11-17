// =========================================================
// File: Routes.kt
// 役割:
//   - アプリ内の全画面のルート（識別子）を一元管理する。
//   - storeId などのパラメータ付きルートもここで定義する。
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui

object Routes {

    // ------------------------
    // メイン / 認証系
    // ------------------------
    const val MAIN = "main"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val LOGIN_SUCCESS = "login_success"
    const val REGISTER_SUCCESS = "register_success"

    // ------------------------
    // パスワード再設定
    // ------------------------
    const val FIND_PASSWORD = "find_password"
    const val PASSWORD_RESET = "password_reset"
    const val PASSWORD_RESET_SUCCESS = "password_reset_success"

    // ------------------------
    // 店舗選択関連
    // ------------------------
    const val STORE_SELECT = "store_select"          // 店舗選択トップ
    const val GPS_PERMISSION = "gps_permission"       // 位置情報許可
    const val STORE_MAP = "store_map"                 // 店舗マップ（現在地）
    const val STORE_REGION = "store_region"           // 都道府県一覧
    const val STORE_REGION_DETAIL = "store_region_detail" // 都道府県の店舗一覧
    const val STORE_DETAIL = "store_detail"           // 店舗詳細

    // ------------------------
    // 商品 / カート
    // ------------------------
    const val MENU = "menu"          // 商品一覧（店舗）
    const val CART = "cart"          // カート
    const val ROUTE = "route"        // 最短ルート

    // ------------------------
    // マイページ系
    // ------------------------
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile_edit"
    const val SETTINGS = "settings"
    const val TERMS = "terms"
    const val ORDER_HISTORY = "order_history"
}