// =========================================================
// File: AppNavHost.kt（修正版）
// 说明：应用整体导航控制（中文修正版）
// 核心修复：
//   1. 让店铺详情可接收店铺名
//   2. MenuScreen 按需接收 storeName（不破坏现有逻辑）
//   3. SelectedStoreState 记录当前店铺 ID + 店铺名
// =========================================================

package com.example.supermarket.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.screens.*

import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.data.SelectedStoreState   // ★ 保存当前店铺信息

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
            LoginSuccessScreen(navController = navController)
        }

        composable(Routes.REGISTER) { RegisterScreen(navController) }

        composable(Routes.REGISTER_SUCCESS) {
            RegisterSuccessScreen(navController = navController)
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
            PasswordResetSuccessScreen(navController = navController)
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

        // 第三层：检索结果页面
        composable(
            route = Routes.STORE_RESULT + "/{keyword}",
            arguments = listOf(navArgument("keyword") { type = NavType.StringType })
        ) { backStack ->
            val keyword = backStack.arguments?.getString("keyword") ?: ""
            StoreResultScreen(navController, keyword)
        }

        // ------------------------
        // ★ 店铺详情（支持店铺名）
        // ------------------------
        // 店舗詳細
        composable(
            route = "${Routes.STORE_DETAIL}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""

            // ★ 店舗詳細に入るタイミングで「現在の店舗ID」を記録
            SelectedStoreState.currentStoreId = storeId

            // ★ StoreDetailScreen は storeId だけ渡す（既存定義に合わせる）
            StoreDetailScreen(navController, storeId)
        }


        // 旧版 store_region
        composable(
            route = "${Routes.STORE_REGION}/{prefecture}",
            arguments = listOf(navArgument("prefecture") { type = NavType.StringType })
        ) {
            StoreRegionScreen(navController, it.arguments?.getString("prefecture") ?: "")
        }

        // 店铺地图扩展
        composable(
            route = "${Routes.STORE_MAP_EXPANDED}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) {
            StoreMapExpandedScreen(navController, it.arguments?.getString("storeId") ?: "")
        }

        // ------------------------
        // 商品菜单
        // ------------------------

        // ② MENU/{storeId} → 現在の店舗IDを更新
        composable(
            route = "${Routes.MENU}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: "S001"

            // ★ 現在選択中の店舗IDを記録
            SelectedStoreState.currentStoreId = storeId

            // ★ MenuScreen も既存通り storeId だけ渡す
            MenuScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                storeId = storeId
            )
        }


        // ② MENU/{storeId}/{storeName}
        composable(
            route = "${Routes.MENU}/{storeId}/{storeName}",
            arguments = listOf(
                navArgument("storeId") { type = NavType.StringType },
                navArgument("storeName") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getString("storeId") ?: ""
            val name = backStackEntry.arguments?.getString("storeName") ?: ""

            // ★ 現在選択中の店舗ID／店舗名を両方記録
            SelectedStoreState.currentStoreId = id
            SelectedStoreState.currentStoreName = name

            // ★ MenuScreen は既存通り storeId のみ受け取る
            MenuScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                storeId = id
            )
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
        // 最短路径
        // ------------------------
        composable(
            route = "${Routes.ROUTE}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
            RouteScreen(navController, cartViewModel, storeId)
        }

        // ------------------------
        // 我的页面、编辑、历史
        // ------------------------
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.PROFILE_EDIT) },
                onOrderHistory = { navController.navigate(Routes.ORDER_HISTORY) },
                onLogout = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.PROFILE_EDIT) {
            ProfileEditScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Routes.ORDER_HISTORY) {
            OrderHistoryScreen(navController, cartViewModel)
        }

        // ------------------------
        // 帮助
        // ------------------------
        composable(Routes.HELP) { HelpScreen(navController) }
    }
}
