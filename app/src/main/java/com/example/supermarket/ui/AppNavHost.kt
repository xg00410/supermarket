// =========================================================
// File: AppNavHost.kt
// アプリ全体のナビゲーション制御
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.supermarket.ui.screens.*
import com.example.supermarket.ui.screens.successscreen.LoginSuccessScreen
import com.example.supermarket.ui.screens.successscreen.RegisterSuccessScreen
import com.example.supermarket.ui.screens.successscreen.PasswordResetSuccessScreen
import com.example.supermarket.viewmodel.CartViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN,
        modifier = modifier
    ) {

        // メイン
        composable(Routes.MAIN) {
            MainScreen(navController)
        }

        // ログイン
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.LOGIN_SUCCESS) },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onForgotPassword = { navController.navigate(Routes.FIND_PASSWORD) }
            )
        }

        composable(Routes.LOGIN_SUCCESS) {
            LoginSuccessScreen(navController)
        }

        // 新規登録
        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(Routes.REGISTER_SUCCESS) }
            )
        }

        composable(Routes.REGISTER_SUCCESS) {
            RegisterSuccessScreen(navController)
        }

        // パスワード再設定フロー
        composable(Routes.FIND_PASSWORD) {
            FindPasswordScreen(
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate(Routes.PASSWORD_RESET) }
            )
        }

        composable(Routes.PASSWORD_RESET) {
            PasswordResetScreen(
                onBack = { navController.popBackStack() },
                onSuccess = { navController.navigate(Routes.PASSWORD_RESET_SUCCESS) }
            )
        }

        composable(Routes.PASSWORD_RESET_SUCCESS) {
            PasswordResetSuccessScreen(
                onBackToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }

        // 店舗選択・地図
        composable(Routes.STORE_SELECT) {
            StoreSelectScreen(navController)
        }

        composable(Routes.GPS_PERMISSION) {
            GpsPermissionScreen(navController)
        }

        composable(Routes.STORE_MAP) {
            StoreMapScreen(navController)
        }

        // 都道府県 → 店舗一覧
        composable(Routes.STORE_REGION) {
            StoreRegionScreen(navController)
        }

        composable(
            route = "${Routes.STORE_REGION}/{prefecture}",
            arguments = listOf(navArgument("prefecture") { type = NavType.StringType })
        ) { backStackEntry ->
            val prefecture = backStackEntry.arguments?.getString("prefecture") ?: ""
            StoreRegionDetailScreen(
                navController = navController,
                prefecture = prefecture
            )
        }

        // 店舗詳細
        composable(
            route = "${Routes.STORE_DETAIL}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            StoreDetailScreen(
                navController = navController,
                storeId = storeId
            )
        }


        // 店舗内マップ拡大
        composable(
            route = "${Routes.STORE_MAP_EXPANDED}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            StoreMapExpandedScreen(navController, storeId)
        }

        // 商品一覧（デフォルトS001用）
        composable(Routes.MENU) {
            MenuScreen(
                navController = navController,
                storeId = "S001",
                cartViewModel = cartViewModel
            )
        }

        // 商品一覧（storeId付き）
        composable(
            route = "${Routes.MENU}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: "S001"
            MenuScreen(
                navController = navController,
                storeId = storeId,
                cartViewModel = cartViewModel
            )
        }
        composable("menu/{storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            MenuScreen(navController, cartViewModel, storeId)
        }

        composable("store_map_expanded/{storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            StoreMapExpandedScreen(navController, storeId)
        }


        // カート
        composable(Routes.CART) {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // 最短ルート
        composable(Routes.ROUTE) {
            RouteScreen(navController)
        }

        // マイページ
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.PROFILE_EDIT) },
                onOrderHistory = { navController.navigate(Routes.ORDER_HISTORY) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onTerms = { navController.navigate(Routes.TERMS) }
            )
        }

        composable(Routes.PROFILE_EDIT) {
            ProfileEditScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.TERMS) {
            TermsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.ORDER_HISTORY) {
            OrderHistoryScreen(onBack = { navController.popBackStack() })
        }

        // ヘルプ
        composable(Routes.HELP) {
            HelpScreen(navController)
        }
    }
}
