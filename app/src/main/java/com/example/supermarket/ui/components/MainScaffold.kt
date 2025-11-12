package com.example.supermarket.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.supermarket.ui.components.BottomNavBar

/**
 * 🧱 MainScaffold.kt
 * ---------------------------------------------------------
 * 📘 共通画面レイアウト（底部导航栏付き）
 * ---------------------------------------------------------
 * 🇯🇵 共通のScaffoldレイアウトを提供し、すべての主要画面で
 *     一貫したトップバー・ボトムバーを利用できるようにする。
 *
 * 🇨🇳 提供一个统一的 Scaffold 模板，使所有主要画面共享
 *     顶部标题栏与底部导航栏，保持界面一致性。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    title: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) } // ✅ 共用控制器
    ) { innerPadding ->
        content(innerPadding)
    }
}
