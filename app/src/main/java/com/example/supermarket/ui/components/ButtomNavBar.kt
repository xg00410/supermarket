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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.supermarket.ui.Routes

/**
 * BottomNavBar
 * 🇯🇵 下部ナビゲーションバー（4タブ）
 * 🇨🇳 底部导航四栏
 */
@Composable
fun BottomNavBar(navController: NavController) {

    val items = listOf(
        BottomNavItem("店舗選択", Icons.Filled.Store, Routes.STORE_SELECT),
        BottomNavItem("店舗", Icons.Filled.Home, Routes.MENU + "/store_tokyo_001"),
        BottomNavItem("カート", Icons.Filled.ShoppingCart, Routes.CART),
        BottomNavItem("マイページ", Icons.Filled.Person, Routes.PROFILE)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val current = navBackStackEntry?.destination?.route ?: ""

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = current.startsWith(item.route.substringBefore("/")),
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)
