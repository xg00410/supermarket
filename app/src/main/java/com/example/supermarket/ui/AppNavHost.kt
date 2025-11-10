package com.example.supermarket.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supermarket.data.FakeRepository
import com.example.supermarket.ui.screens.*
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
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.LOGIN_SUCCESS) },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Routes.REGISTER_SUCCESS) }
            )
        }

        composable(Routes.STORE_MAP) {
            StoreMapScreen(
                onRegionClick = { regionKey ->
                    navController.navigate("${Routes.STORE_REGION}?region=$regionKey")
                }
            )
        }

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

        composable("${Routes.STORE_MAP_EXPANDED}?region={region}") { backStackEntry ->
            val region = backStackEntry.arguments?.getString("region") ?: "未指定"
            val stores = FakeRepository.getStoresByPrefecture(region)
            StoreMapExpandedScreen(
                regionName = region,
                stores = stores,
                onStoreClick = { storeId ->
                    navController.navigate("${Routes.STORE_DETAIL}?storeId=$storeId")
                },
                onBack = { navController.popBackStack() }
            )
        }

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

        composable(Routes.CART) {
            val cartViewModel: CartViewModel = viewModel()
            CartScreen(
                navController = navController,
                onBack = { navController.popBackStack() },
                onGoRoute = { navController.navigate(Routes.ROUTE) },
                cartViewModel = cartViewModel
            )
        }

        composable(Routes.ROUTE) {
            val cartViewModel: CartViewModel = viewModel()
            RouteScreen(
                onBack = { navController.popBackStack() },
                cartViewModel = cartViewModel
            )
        }

        composable(Routes.STORE_SELECT) {
            StoreSelectScreen(
                navController = navController,
                onStoreClick = { storeId ->
                    navController.navigate("${Routes.STORE_DETAIL}?storeId=$storeId")
                },
                onSearchSubmit = { _ -> },
                onNearbyClick = { }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
        }

        // ✅ 登录成功后跳转优化
        composable(Routes.LOGIN_SUCCESS) {
            LoginSuccessScreen(onNext = {
                navController.navigate(Routes.STORE_SELECT) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }

        composable(Routes.PASSWORD_RESET) {
            PasswordResetScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.PASSWORD_RESET_SUCCESS) {
            PasswordResetSuccessScreen(onBackToLogin = { navController.navigate(Routes.LOGIN) })
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
    }
}
