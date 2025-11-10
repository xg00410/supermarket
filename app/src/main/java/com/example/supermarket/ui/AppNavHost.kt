package com.example.supermarket.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supermarket.R
import com.example.supermarket.ui.screens.*
import com.example.supermarket.viewmodel.CartViewModel

/**
 * 🗺️ アプリ全体のナビゲーションを管理するコンポーネント
 * 全局导航控制文件（负责管理所有页面的跳转关系）
 */
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val STORE_MAP = "store_map"
    const val STORE_REGION = "store_region"
    const val STORE_MAP_EXPANDED = "store_map_expanded"
    const val STORE_DETAIL = "store_detail"
    const val MENU = "menu"
    const val CART = "cart"
    const val ROUTE = "route"
    const val STORE_SELECT = "store_select"

    // 🆕 新增的功能画面
    const val PASSWORD_RESET = "password_reset"
    const val PASSWORD_RESET_SUCCESS = "password_reset_success"
    const val LOGIN_SUCCESS = "login_success"
    const val REGISTER_SUCCESS = "register_success"
    const val GPS_PERMISSION = "gps_permission"
    const val SETTINGS = "settings"
    const val HELP = "help"
    const val TERMS = "terms"
    const val PROFILE_EDIT = "profile_edit"
    const val ORDER_HISTORY = "order_history"
}

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // 🔹 ログイン画面 / 登录画面
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.LOGIN_SUCCESS) },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        // 🔹 新規登録画面 / 新规注册画面
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Routes.REGISTER_SUCCESS) }
            )
        }

        // 🔹 日本地図画面（地域選択）/ 日本地图（选择区域）
        composable(Routes.STORE_MAP) {
            StoreMapScreen(
                onRegionClick = { regionKey ->
                    navController.navigate("${Routes.STORE_REGION}?region=$regionKey")
                }
            )
        }

        // 🔹 都道府県リスト画面
        composable("${Routes.STORE_REGION}?region={region}") { backStackEntry ->
            val region = backStackEntry.arguments?.getString("region") ?: "未指定"
            StoreRegionScreen(
                regionName = region,
                onPrefectureClick = { prefecture ->
                    navController.navigate("${Routes.STORE_MAP_EXPANDED}?region=$prefecture")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // 🔹 店舗一覧画面
        composable("${Routes.STORE_MAP_EXPANDED}?region={region}") { backStackEntry ->
            val region = backStackEntry.arguments?.getString("region") ?: "未指定"
            val stores = com.example.supermarket.data.FakeRepository.getStoresByPrefecture(region)

            StoreMapExpandedScreen(
                regionName = region,
                stores = stores,
                onStoreClick = { storeId ->
                    navController.navigate("${Routes.STORE_DETAIL}?storeId=$storeId")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // 🔹 店舗詳細画面
        composable("${Routes.STORE_DETAIL}?storeId={storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            StoreDetailScreen(
                storeId = storeId,
                onBack = { navController.popBackStack() },
                onGoMenu = { sid ->
                    navController.navigate("${Routes.MENU}?storeId=$sid")
                }
            )
        }

        // 🔹 商品画面
        composable("${Routes.MENU}?storeId={storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            val cartViewModel: CartViewModel = viewModel()
            MenuScreen(
                storeId = storeId,
                onBack = { navController.popBackStack() },
                onGoCart = { navController.navigate(Routes.CART) },
                cartViewModel = cartViewModel
            )
        }

        // 🔹 カート画面
        composable(Routes.CART) {
            val cartViewModel: CartViewModel = viewModel()
            CartScreen(
                onBack = { navController.popBackStack() },
                onGoRoute = { navController.navigate(Routes.ROUTE) },
                cartViewModel = cartViewModel
            )
        }

        // 🔹 最短ルート画面
        composable(Routes.ROUTE) {
            val cartViewModel: CartViewModel = viewModel()
            RouteScreen(
                onBack = { navController.popBackStack() },
                cartViewModel = cartViewModel
            )
        }

        // 🔹 店舗検索（現在地）
        composable(Routes.STORE_SELECT) {
            StoreSelectScreen(
                onStoreClick = { storeId ->
                    navController.navigate("${Routes.STORE_DETAIL}?storeId=$storeId")
                },
                onSearchSubmit = { _ -> },
                onNearbyClick = { }
            )
        }

        // 🆕 追加画面群
        composable(Routes.PASSWORD_RESET) {
            PasswordResetScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.PASSWORD_RESET_SUCCESS) {
            PasswordResetSuccessScreen(onBackToLogin = { navController.navigate(Routes.LOGIN) })
        }

        composable(Routes.LOGIN_SUCCESS) {
            LoginSuccessScreen(onNext = { navController.navigate(Routes.STORE_MAP) })
        }

        composable(Routes.REGISTER_SUCCESS) {
            RegisterSuccessScreen(onNext = { navController.navigate(Routes.LOGIN) })
        }

        composable(Routes.GPS_PERMISSION) {
            GpsPermissionScreen(
                onGrant = { navController.popBackStack() },
                onDeny = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.HELP) {
            HelpScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.TERMS) {
            TermsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.PROFILE_EDIT) {
            ProfileEditScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.ORDER_HISTORY) {
            OrderHistoryScreen(onBack = { navController.popBackStack() })
        }
        composable("profile") { ProfileScreen() }


    }
}
