// =========================================================
// File: AppNavHost.kt
// 概要: 画面遷移（ログイン、店舗選択、商品一覧、カート、ルート案内等）を管理するナビゲーションホスト。
//設計書ID: なし（ナビゲーション制御）
//画面名: 画面遷移管理
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.screens.*
import com.example.supermarket.ui.screens.successscreen.LoginSuccessScreen
import com.example.supermarket.ui.screens.successscreen.RegisterSuccessScreen
import com.example.supermarket.ui.screens.successscreen.PasswordResetSuccessScreen
import com.example.supermarket.viewmodel.CartViewModel

/**
 * 🚏 AppNavHost
 * 🇯🇵 アプリ全体のナビゲーションを定義
 * 🇨🇳 控制整套应用的导航结构（最核心文件）
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {

        // ------------------ Main ------------------
        composable(Routes.MAIN) {
            MainScreen(navController)
        }

        // ------------------ Login 系 ------------------
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.LOGIN_SUCCESS) },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onForgotPassword = { navController.navigate(Routes.PASSWORD_RESET) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Routes.REGISTER_SUCCESS) }
            )
        }

        // 成功画面
        composable(Routes.LOGIN_SUCCESS) {
            LoginSuccessScreen(
                // 🔹「次へ」→ 店舗選択画面（リスト式）
                onNext = { navController.navigate(Routes.STORE_SELECT) }
            )
        }

        composable(Routes.REGISTER_SUCCESS) {
            RegisterSuccessScreen(
                onNext = { navController.navigate(Routes.LOGIN) }
            )
        }

        composable(Routes.PASSWORD_RESET) {
            PasswordResetScreen(
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Routes.PASSWORD_RESET_SUCCESS)
                }
            )
        }

        composable(Routes.PASSWORD_RESET_SUCCESS) {
            PasswordResetSuccessScreen(
                onNext = { navController.navigate(Routes.LOGIN) }
            )
        }

        // ------------------ 店舗選択（新・検索＋リスト） ------------------
        composable(Routes.STORE_SELECT) {
            StoreSelectScreen(
                navController = navController,

            )
        }

        // ------------------ 店舗選択（旧・地図＋地域） ------------------
        composable(Routes.STORE_MAP) {
            StoreMapScreen(
                navController = navController,
                onRegionClick = { region ->
                    navController.navigate("${Routes.STORE_REGION}/$region")
                },
                onNearbyClick = {
                    navController.navigate(Routes.GPS_PERMISSION)
                }
            )
        }

        // ------------------ 地域 → 都道府県 ------------------
        composable("${Routes.STORE_REGION}/{region}") { backStack ->
            val region = backStack.arguments?.getString("region") ?: ""

            StoreRegionScreen(
                regionName = region,
                onPrefectureClick = { pref ->
                    navController.navigate("${Routes.STORE_LIST}/$pref")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ------------------ 都道府県 → 店舗一覧 ------------------
        composable("${Routes.STORE_LIST}/{prefecture}") { back ->
            val pref = back.arguments?.getString("prefecture") ?: ""
            val stores = StoreDataRepository.getStoresByPrefecture(pref)

            StoreMapExpandedScreen(
                regionName = pref,
                stores = stores,
                onStoreClick = { id ->
                    navController.navigate("${Routes.STORE_DETAIL}/$id")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ------------------ 店舗詳細 ------------------
        composable("${Routes.STORE_DETAIL}/{storeId}") { back ->
            val id = back.arguments?.getString("storeId") ?: ""
            StoreDetailScreen(
                storeId = id,
                onGoMenu = {
                    navController.navigate("${Routes.MENU}/$id")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ------------------ 店舗メニュー（商品一覧） ------------------
        composable("${Routes.MENU}/{storeId}") { back ->
            val storeId = back.arguments?.getString("storeId") ?: ""

            MenuScreen(
                navController = navController,
                storeId = storeId,
                onGoCart = { navController.navigate(Routes.CART) },
                onBack = { navController.popBackStack() },
                cartViewModel = cartViewModel
            )
        }

        // ------------------ カート ------------------
        composable(Routes.CART) {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                onBack = { navController.popBackStack() },
                onGoRoute = { navController.navigate(Routes.ROUTE) }
            )
        }

        // ------------------ 最短ルート ------------------
        composable(Routes.ROUTE) {
            RouteScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // ------------------ GPS 权限 ------------------
        composable(Routes.GPS_PERMISSION) {
            GpsPermissionScreen(
                onGrant = { navController.popBackStack() },
                onDeny = { navController.popBackStack() }
            )
        }

        // ------------------ マイページ ------------------
        composable(Routes.PROFILE) {
            ProfileScreen(navController)
        }

        composable(Routes.PROFILE_EDIT) {
            ProfileEditScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ORDER_HISTORY) {
            OrderHistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HELP) {
            HelpScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.TERMS) {
            TermsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
