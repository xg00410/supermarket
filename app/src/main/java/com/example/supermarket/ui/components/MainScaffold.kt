// =========================================================
// File: MainScaffold.kt
// 概要:
//   - アプリ全体の共通レイアウト（Scaffold）を提供する。
//   - 画面下部にボトムナビゲーションバーを表示し、
//     上部には AppNavHost を配置する。
//   - ログイン画面など、一部画面ではボトムバーを非表示にする。
// 設計書ID: なし（レイアウト共通部品）
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.theme.AppNavHost
import com.example.supermarket.viewmodel.CartViewModel

/**
 * MainScaffold
 * アプリ全体の共通レイアウト。
 *
 * @param navController  NavHostController（画面遷移管理）
 * @param cartViewModel  カート・履歴管理用 ViewModel
 */
@Composable
fun MainScaffold(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    // 現在のルートを取得
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ""

    // ボトムバーを表示するかどうかを判定
    val showBottomBar = when {
        currentRoute.startsWith(Routes.LOGIN) -> false
        currentRoute.startsWith(Routes.REGISTER) -> false
        currentRoute.startsWith(Routes.FIND_PASSWORD) -> false
        currentRoute.startsWith(Routes.PASSWORD_RESET) -> false
        currentRoute.startsWith(Routes.MAIN) -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            cartViewModel = cartViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
