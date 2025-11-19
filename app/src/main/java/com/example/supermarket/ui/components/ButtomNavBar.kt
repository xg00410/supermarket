package com.example.supermarket.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.supermarket.data.SelectedStoreState   // ★ 追加
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute?.startsWith(item.route) == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    val targetRoute = when (item.route) {
                        Routes.MENU -> {
                            // ★ 直近の店舗IDを使って商品一覧を開く
                            val storeId = SelectedStoreState.currentStoreId
                            "${Routes.MENU}/$storeId"
                        }
                        else -> item.route
                    }

                    navController.navigate(targetRoute) {
                        // ここでは popUpTo を使わず、スタックを必要以上に潰さない
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
