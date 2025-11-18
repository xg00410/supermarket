// =========================================================
// File: BottomNavBar.kt
// 概要: アプリ全体共通のボトムナビゲーションバーを表示するコンポーネント。
//設計書ID: なし（部品）
//画面名: ボトムナビゲーションバー
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.supermarket.ui.Routes
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("店舗選択", Icons.Filled.Store, Routes.STORE_SELECT),
        BottomNavItem("ホーム", Icons.Filled.Home, Routes.MENU),   // menu はデフォルト店舗(S001)
        BottomNavItem("カート", Icons.Filled.ShoppingCart, Routes.CART),
        BottomNavItem("マイページ", Icons.Filled.Person, Routes.PROFILE)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute?.startsWith(item.route) == true,
                onClick = {
                    if (currentRoute?.startsWith(item.route) != true) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.MAIN) { inclusive = false }
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