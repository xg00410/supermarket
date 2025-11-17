// =========================================================
// File: AppNavHost.kt
// アプリ全体のナビゲーション制御
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Navigation
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// ViewModel
import com.example.supermarket.viewmodel.CartViewModel

// Screens
import com.example.supermarket.ui.components.ResultTemplateScreen
import com.example.supermarket.ui.screens.*
import com.example.supermarket.ui.screens.successscreen.PasswordResetSuccessScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN,
        modifier = modifier
    ) {

        // -------------------------------
        // メイン
        // -------------------------------
        composable(Routes.MAIN) { MainScreen(navController) }

        // -------------------------------
        // ログイン
        // -------------------------------
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.LOGIN_SUCCESS) },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onForgotPassword = { navController.navigate(Routes.FIND_PASSWORD) }
            )
        }

        composable(Routes.LOGIN_SUCCESS) {
            ResultTemplateScreen(
                titleText = "ログイン成功！",
                buttonText = "ホームへ戻る",
                onButtonClick = { navController.navigate(Routes.MAIN) }
            )
        }

        // -------------------------------
        // 新規登録
        // -------------------------------
        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Routes.REGISTER_SUCCESS)
                }
            )
        }

        composable(Routes.REGISTER_SUCCESS) {
            ResultTemplateScreen(
                titleText = "登録完了！",
                buttonText = "ログインへ",
                onButtonClick = { navController.navigate(Routes.LOGIN) }
            )
        }

        // -------------------------------
        // パスワード再設定
        // -------------------------------
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

        // -------------------------------
        // 店舗選択（入口）
        // -------------------------------
        composable(Routes.STORE_SELECT) {
            StoreSelectScreen(navController)
        }

        // 位置情報許可
        composable(Routes.GPS_PERMISSION) {
            GpsPermissionScreen(navController)
        }

        // 店舗マップ画面（現在地）
        composable(Routes.STORE_MAP) {
            StoreMapScreen(navController)
        }

        // -------------------------------
        // 都道府県一覧
        // -------------------------------
        composable(Routes.STORE_REGION) {
            StoreRegionScreen(navController)
        }

        // 都道府県 → 店舗一覧
        composable(
            route = "${Routes.STORE_REGION}/{prefecture}",
            arguments = listOf(navArgument("prefecture") { type = NavType.StringType })
        ) { backStackEntry ->
            val prefecture = backStackEntry.arguments?.getString("prefecture") ?: ""
            StoreRegionDetailScreen(navController, prefecture)
        }

        // -------------------------------
        // 店舗詳細
        // -------------------------------
        composable("${Routes.STORE_DETAIL}/{storeId}") {
            val storeId = it.arguments?.getString("storeId") ?: ""

            StoreDetailScreen(
                storeId = storeId,
                onBack = { navController.popBackStack() },
                onViewStoreMap = {
                    onViewStoreMap()

                },
                onViewMenu = {
                    navController.navigate("${Routes.MENU}/$storeId")
                }
            )
        }

        // -------------------------------
        // 商品一覧（MenuScreen）
        // -------------------------------
        composable(
            route = "${Routes.MENU}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            MenuScreen(navController, storeId, cartViewModel)
        }

        // -------------------------------
        // カート
        // -------------------------------
        composable(Routes.CART) {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // -------------------------------
        // 最短ルート
        // -------------------------------
        composable(Routes.ROUTE) {
            RouteScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        // -------------------------------
// マイページ
        composable(Routes.PROFILE_EDIT) {
            ProfileEditScreen(
                onBack = { navController.popBackStack() },
                onSaved = {
                    onSaved()
                    {
                        popUpTo(Routes.PROFILE) { inclusive = true }
                    }
                }
            )
        }


// プロフィール編集
        composable(Routes.PROFILE_EDIT) {
            ProfileEditScreen(
                onBack = { navController.popBackStack() }
            )
        }

// 設定
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

// 利用規約
        composable(Routes.TERMS) {
            TermsScreen(
                onBack = { navController.popBackStack() }
            )
        }

// 注文履歴
        composable(Routes.ORDER_HISTORY) {
            OrderHistoryScreen(
                onBack = { navController.popBackStack() })
    }
}}