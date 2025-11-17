package com.example.supermarket.ui

/**
 * Routes.kt
 * 🇯🇵 画面遷移ルート一覧
 * 🇨🇳 所有路由常量
 */
object Routes {
    const val MAIN = "main"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val LOGIN_SUCCESS = "login_success"
    const val REGISTER_SUCCESS = "register_success"
    const val PASSWORD_RESET = "password_reset"
    const val PASSWORD_RESET_SUCCESS = "password_reset_success"

    // 店舗関連 / 店铺相关
    const val STORE_MAP = "store_map"          // 地図＋地域から選ぶ / 地图&区域选店
    const val STORE_REGION = "store_region"    // 地域→都道府県
    const val STORE_LIST = "store_list"        // 都道府県→店舗一覧
    const val STORE_DETAIL = "store_detail"    // 店舗詳細
    const val STORE_SELECT = "store_select"    // 🔹新：検索＋リストの店舗選択画面

    const val MENU = "menu"                    // 店舗内の商品一覧

    // カート・ルート / 购物车&最短路径
    const val CART = "cart"
    const val ROUTE = "route"

    // GPS 権限 / GPS 权限
    const val GPS_PERMISSION = "gps_permission"

    // マイページ系 / 个人中心相关
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile_edit"
    const val ORDER_HISTORY = "order_history"
    const val SETTINGS = "settings"
    const val HELP = "help"
    const val TERMS = "terms"
}
