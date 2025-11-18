// =========================================================
// File: AppNavHost.kt
// 全局ナビゲーションホスト（完全修正版）
// =========================================================

package com.example.supermarket.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.supermarket.ui.screens.*
import com.example.supermarket.ui.screens.successscreen.LoginSuccessScreen
import com.example.supermarket.ui.screens.successscreen.PasswordResetSuccessScreen
import com.example.supermarket.ui.screens.successscreen.RegisterSuccessScreen
import com.example.supermarket.viewmodel.CartViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN,
        modifier = modifier
    ) {

        // --------------------------
        // メイン
        // --------------------------
        composable(Routes.MAIN) {
            MainScreen(navController)
        }

        // --------------------------
        // ログイン
        // --------------------------
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }

        composable(Routes.LOGIN_SUCCESS) {
            LoginSuccessScreen {
                navController.navigate(Routes.STORE_SELECT)
            }
        }

        // --------------------------
        // 新規登録
        // --------------------------
        composable(Routes.REGISTER) {
            RegisterScreen(navController)
        }

        composable(Routes.REGISTER_SUCCESS) {
            RegisterSuccessScreen {
                navController.navigate(Routes.LOGIN)
            }
        }

        // --------------------------
        // パスワード再設定
        // --------------------------
        composable(Routes.FIND_PASSWORD) {
            FindPasswordScreen(navController)
        }

        composable(Routes.PASSWORD_RESET) {
            PasswordResetScreen(navController)
        }

        composable(Routes.PASSWORD_RESET_SUCCESS) {
            PasswordResetSuccessScreen {
                navController.navigate(Routes.LOGIN)
            }
        }

        // --------------------------
        // 店舗選択（3段階）
        // --------------------------
        composable(Routes.STORE_SELECT) {
            StoreSelectScreen(navController)
        }

        composable(Routes.AREA_LEVEL1) {
            AreaLevel1Screen(navController)
        }

        composable(Routes.AREA_LEVEL2) {
            AreaLevel2Screen(navController)
        }

        // --------------------------
        // 店舗詳細 → 商品一覧
        // --------------------------
        composable("${Routes.STORE_DETAIL}/{storeId}") { backStack ->
            val id = backStack.arguments?.getString("storeId") ?: return@composable
            StoreDetailScreen(navController, id)
        }

        composable("${Routes.MENU}/{storeId}") { backStack ->
            val id = backStack.arguments?.getString("storeId") ?: return@composable
            MenuScreen(navController, id, cartViewModel)
        }

        // --------------------------
        // カート・管理モード
        // --------------------------
        composable(Routes.CART) {
            CartScreen(navController, cartViewModel)
        }

        composable(Routes.LIST2) {
            List2Screen(navController, cartViewModel)
        }

        // --------------------------
        // 最短ルート
        // --------------------------
        composable("${Routes.ROUTE}/{storeId}") { backStack ->
            val id = backStack.arguments?.getString("storeId") ?: return@composable
            RouteScreen(navController, cartViewModel, id)
        }

        // --------------------------
        // 履歴
        // --------------------------
        composable(Routes.ORDER_HISTORY) {
            OrderHistoryScreen(navController)
        }

        // --------------------------
        // ヘルプ・設定
        // --------------------------
        composable(Routes.SETTINGS) {
            SettingsScreen(navController)
        }

        composable(Routes.HELP) {
            HelpScreen(navController)
        }

        composable(Routes.TERMS) {
            TermsScreen(navController)
        }

        composable(Routes.GPS_PERMISSION) {
            GpsPermissionScreen(navController)
        }

        // --------------------------
        // プロフィール
        // --------------------------
        composable(Routes.PROFILE) {
            ProfileScreen(navController)
        }

        composable(Routes.PROFILE_EDIT) {
            ProfileEditScreen(navController)
        }

        // 店舗マップ表示
        composable("${Routes.STORE_MAP_EXPANDED}/{storeId}") { backStack ->
            val id = backStack.arguments?.getString("storeId") ?: return@composable
            StoreMapExpandedScreen(navController, id)
        }
    }
}
