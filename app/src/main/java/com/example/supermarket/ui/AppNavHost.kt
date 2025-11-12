package com.example.supermarket.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supermarket.data.FakeRepository
import com.example.supermarket.ui.screens.*
import com.example.supermarket.ui.screens.successscreen.*
import com.example.supermarket.viewmodel.CartViewModel

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
    const val PROFILE = "profile"
}

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // 🔹 ログイン
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.LOGIN_SUCCESS) },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        // 🔹 新規登録
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Routes.REGISTER_SUCCESS) }
            )
        }

        // 🔹 店舗マップ
        composable(Routes.STORE_MAP) {
            StoreMapScreen(
                onRegionClick = { regionKey ->
                    navController.navigate("${Routes.STORE_REGION}?region=$regionKey")
                }
            )
        }

        // 🔹 地域別マップ
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

        // 🔹 店舗リスト（地域拡大表示）
        composable("${Routes.STORE_MAP_EXPANDED}?region={region}") { backStackEntry ->
            val region = backStackEntry.arguments?.getString("region") ?: "未指定"

            val storeItems = FakeRepository.getStoresByPrefecture(region).map {
                com.example.supermarket.models.StoreItem(
                    id = it.id,
                    name = it.name,
                    address = it.address,
                    imageRes = 0
                )
            }

            StoreMapExpandedScreen(
                regionName = region,
                stores = storeItems,
                onStoreClick = { storeId ->
                    // ✅ 修正：路径参数形式，避免闪退
                    navController.navigate("${Routes.STORE_DETAIL}/$storeId")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // 🔹 店舗詳細画面
        composable("${Routes.STORE_DETAIL}/{storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            StoreDetailScreen(
                storeId = storeId,
                onBack = { navController.popBackStack() },
                onGoMenu = { sid ->
                    navController.navigate("${Routes.MENU}?storeId=$sid")
                }
            )
        }

        // 🔹 メニュー画面
        composable("${Routes.MENU}?storeId={storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            val cartViewModel: CartViewModel = viewModel()
            MenuScreen(
                navController = navController,
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
                navController = navController,
                onBack = { navController.popBackStack() },
                onGoRoute = { navController.navigate(Routes.ROUTE) },
                cartViewModel = cartViewModel
            )
        }

        // 🔹 経路表示
        composable(Routes.ROUTE) {
            val cartViewModel: CartViewModel = viewModel()
            RouteScreen(
                onBack = { navController.popBackStack() },
                cartViewModel = cartViewModel
            )
        }

        // 🔹 店舗選択画面
        composable(Routes.STORE_SELECT) {
            StoreSelectScreen(
                navController = navController,
                onStoreClick = { storeId ->
                    // ✅ 修正一致
                    navController.navigate("${Routes.STORE_DETAIL}/$storeId")
                },
                onSearchSubmit = { _ -> },
                onNearbyClick = { }
            )
        }

        // 🔹 プロフィール
        composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
        }

        // 🔹 ログイン成功
        composable(Routes.LOGIN_SUCCESS) {
            LoginSuccessScreen(
                onNext = {
                    navController.navigate(Routes.STORE_SELECT) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🔹 パスワードリセット
        composable(Routes.PASSWORD_RESET) {
            PasswordResetScreen(
                onBack = { navController.popBackStack() },
                onSuccess = { navController.navigate(Routes.PASSWORD_RESET_SUCCESS) }
            )
        }

        // 🔹 パスワードリセット成功
        composable(Routes.PASSWORD_RESET_SUCCESS) {
            PasswordResetSuccessScreen(
                onNext = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.PASSWORD_RESET) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🔹 新規登録成功
        composable(Routes.REGISTER_SUCCESS) {
            RegisterSuccessScreen(
                onNext = { navController.navigate(Routes.LOGIN) }
            )
        }

        // 🔹 GPS権限画面
        composable(Routes.GPS_PERMISSION) {
            GpsPermissionScreen(
                onGrant = { navController.popBackStack() },
                onDeny = { navController.popBackStack() }
            )
        }

        // 🔹 設定 / ヘルプ / 利用規約 / プロフィール編集 / 注文履歴
        composable(Routes.SETTINGS) { SettingsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.HELP) { HelpScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.TERMS) { TermsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.PROFILE_EDIT) { ProfileEditScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.ORDER_HISTORY) { OrderHistoryScreen(onBack = { navController.popBackStack() }) }
    }
}
