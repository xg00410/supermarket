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
    // 認証周り / 认证相关
    const val MAIN = "main"
    const val LOGIN = "login"
    const val LOGIN_SUCCESS = "login_success"
    const val REGISTER = "register"
    const val REGISTER_SUCCESS = "register_success"
    const val FIND_PASSWORD = "find_password"
    const val PASSWORD_RESET = "password_reset"
    const val PASSWORD_RESET_SUCCESS = "password_reset_success"

    // 店舗選択・地図 / 店铺选择・地图
    const val STORE_SELECT = "store_select"
    const val GPS_PERMISSION = "gps_permission"
    const val STORE_MAP = "store_map"          // 現在地付きマップ / 带当前位置地图
    const val STORE_REGION = "store_region"    // 地域→店舗一覧 / 区域→店铺一览
    const val STORE_DETAIL = "store_detail"    // 店舗詳細 / 店铺详情

    // 店舗画面・カート・ルート / 店铺画面・购物车・路径
    const val MENU = "menu"        // 店舗画面（商品一覧）/ 店铺画面（商品列表）
    const val CART = "cart"        // カート画面 list / 购物车画面 list
    const val ROUTE = "route"      // ルート案内画面 / 最短路线导航画面

    // マイページ周り / 个人中心相关
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile_edit"
    const val SETTINGS = "settings"
    const val TERMS = "terms"
    const val ORDER_HISTORY = "order_history"

    // ヘルプ / 帮助
    const val HELP = "help"
}
