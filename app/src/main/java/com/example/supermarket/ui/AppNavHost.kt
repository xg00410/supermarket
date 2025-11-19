// =========================================================
// File: AppNavHost.kt
// 说明：应用整体导航控制（中文修正版）
// 核心修复：
//   1. 记住用户“当前选中的店铺”
//   2. BottomNav 进入菜单时不再固定为 S001
//   3. 所有进入菜单的路径都同步更新 SelectedStoreState
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
import com.example.supermarket.data.SelectedStoreState   // ★ 新增：记录当前店铺

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
        // 起始 / 登录注册
        // ------------------------
        composable(Routes.MAIN) { MainScreen(navController) }
        composable(Routes.LOGIN) { LoginScreen(navController) }

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
        // 店铺选择 3 层级
        // ------------------------
        composable(Routes.STORE_SELECT) { StoreSelectScreen(navController) }
        composable(Routes.GPS_PERMISSION) { GpsPermissionScreen(navController) }
        composable(Routes.STORE_MAP) { StoreMapScreen(navController) }

        // 第二层：区域 → 都道府县
        composable(
            route = Routes.STORE_PREFECTURE + "/{regionId}"
        ) { backStack ->
            val regionId = backStack.arguments?.getString("regionId")!!.toInt()
            StorePrefectureScreen(navController, regionId)
        }

        // 第三层：检索结果（关键词 or 都道府县）
        composable(
            route = Routes.STORE_RESULT + "/keyword={keyword}/pref={pref}"
        ) { backStack ->
            val keyword = backStack.arguments?.getString("keyword")
            val pref = backStack.arguments?.getString("pref")!!.toInt()
            StoreResultScreen(navController, keyword, pref)
        }

        // 店铺详情
        composable(
            route = "${Routes.STORE_DETAIL}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""

            // ★ 进入店铺详情时记录店铺
            SelectedStoreState.currentStoreId = storeId

            StoreDetailScreen(navController, storeId)
        }

        // 旧版本 store_region（你若不用可以不打开）
        composable(
            route = "${Routes.STORE_REGION}/{prefecture}",
            arguments = listOf(navArgument("prefecture") { type = NavType.StringType })
        ) {
            StoreRegionScreen(navController, it.arguments?.getString("prefecture") ?: "")
        }

        // 店铺地图（扩展）
        composable(
            route = "${Routes.STORE_MAP_EXPANDED}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) {
            StoreMapExpandedScreen(navController, it.arguments?.getString("storeId") ?: "")
        }

        // ------------------------
        // 商品菜单（最重要）
        // ------------------------

        // ① MENU（无参数）→ 用“最近选的店”
        composable(Routes.MENU) {
            val current = SelectedStoreState.currentStoreId
            MenuScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                storeId = current
            )
        }

        // ② MENU/{storeId} → 更新当前店铺
        composable(
            route = "${Routes.MENU}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: "S001"

            // ★ 记录用户当前选择的店铺
            SelectedStoreState.currentStoreId = storeId

            MenuScreen(navController, cartViewModel, storeId)
        }

        // ------------------------
        // 购物车
        // ------------------------
        composable(Routes.CART) {
            CartScreen(navController, cartViewModel)
        }

        composable(Routes.CART_MANAGE) {
            List2Screen(navController, cartViewModel)
        }

        // ------------------------
        // 最短路径（传入 storeId）
        // ------------------------
        composable(
            route = "${Routes.ROUTE}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: "S001"
            RouteScreen(navController, cartViewModel, storeId)
        }

        // ------------------------
        // 我的页面 / 设置 / 历史
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

        composable(Routes.SETTINGS) { SettingsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.TERMS) { TermsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.ORDER_HISTORY) { OrderHistoryScreen(navController, cartViewModel) }

        // ------------------------
        // 帮助
        // ------------------------
        composable(Routes.HELP) { HelpScreen(navController) }
    }
}
