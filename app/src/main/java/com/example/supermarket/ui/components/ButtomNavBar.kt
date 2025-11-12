package com.example.supermarket.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.supermarket.ui.Routes

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("店舗選択", Icons.Filled.Store, Routes.STORE_SELECT),
        BottomNavItem("店舗", Icons.Filled.Home, Routes.MENU),
        BottomNavItem("カート", Icons.Filled.ShoppingCart, Routes.CART),
        BottomNavItem("マイページ", Icons.Filled.Person, Routes.PROFILE)
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    // ✅ 修正：防止重复导航，确保共用 AppNavHost 的 NavController
                    if (navController.currentDestination?.route != item.route) {
                        navController.navigate(item.route) {
                            // 返回时不销毁主页面
                            popUpTo(Routes.STORE_SELECT) { inclusive = false }
                            // 避免重复实例化同一目的地
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
