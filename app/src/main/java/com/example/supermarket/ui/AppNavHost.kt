// =========================================================
// File: AppNavHost.kt
// アプリ全体のナビゲーション制御
// 設計書ID: なし（ナビゲーション）
// 役割:
//   - 画面ID（Routes）に基づいて各画面を NavHost に登録する。
//   - Login → Main → 店舗選択 → Menu → カート → Route → 履歴
//     という一連の導線をここで定義する。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.screens.*
import com.example.supermarket.ui.screens.successscreen.LoginSuccessScreen
import com.example.supermarket.ui.screens.successscreen.PasswordResetSuccessScreen
import com.example.supermarket.ui.screens.successscreen.RegisterSuccessScreen
import com.example.supermarket.viewmodel.CartViewModel

/**
 * アプリ全体の画面遷移を管理する NavHost。
 *
 * @param navController  画面遷移を行う NavHostController
 * @param cartViewModel  カート・履歴を共有する ViewModel
 * @param modifier       親の Scaffold から渡される余白など
 */
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

        // ------------------------
        // メイン・認証系
        // ------------------------
        composable(Routes.MAIN) {
            MainScreen(navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        composable(Routes.LOGIN_SUCCESS) {
            LoginSuccessScreen(
                onGoStoreSelect = {
                    navController.navigate(Routes.STORE_SELECT) {
                        popUpTo(Routes.MAIN) { inclusive = false }
                    }
                }
            )
        }


        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(Routes.REGISTER_SUCCESS) }
            )
        }

        composable(Routes.REGISTER_SUCCESS) {
            RegisterSuccessScreen(
                onBackToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }

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

        // ------------------------
        // 店舗選択・位置情報
        // ------------------------
        composable(Routes.STORE_SELECT) {
            StoreSelectScreen(navController)
        }

        composable(Routes.GPS_PERMISSION) {
            GpsPermissionScreen(navController)
        }

        composable(Routes.STORE_MAP) {
            StoreMapScreen(navController)
        }

        composable(
            route = "${Routes.STORE_REGION}/{prefecture}",
            arguments = listOf(navArgument("prefecture") { type = NavType.StringType })
        ) { backStackEntry ->
            val prefecture = backStackEntry.arguments?.getString("prefecture") ?: ""
            StoreRegionScreen(
                navController = navController,
                prefecture = prefecture
            )
        }

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

        composable(
            route = "${Routes.STORE_MAP_EXPANDED}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            StoreMapExpandedScreen(
                navController = navController,
                storeId = storeId
            )
        }

        // ------------------------
        // 商品一覧・カート
        // ------------------------
        composable(Routes.MENU) {
            // デフォルト店舗（仮に S001 とする）
            MenuScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                storeId = "S001"
            )
        }

        composable(
            route = "${Routes.MENU}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: "S001"
            MenuScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                storeId = storeId
            )
        }

        composable(Routes.CART) {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        composable(Routes.CART_MANAGE) {
            List2Screen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // ------------------------
        // 最短ルート
        // ------------------------
        composable(
            route = "${Routes.ROUTE}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: "S001"
            RouteScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                storeId = storeId
            )
        }

        // ------------------------
        // マイページ・履歴・設定
        // ------------------------
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
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.TERMS) {
            TermsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ORDER_HISTORY) {
            OrderHistoryScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // ------------------------
        // ヘルプ
        // ------------------------
        composable(Routes.HELP) {
            HelpScreen(navController)
        }
    }
}
