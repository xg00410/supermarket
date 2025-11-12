package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.components.MainScaffold

/**
 * 🗺️ RouteScreen.kt
 * --------------------------------------------------------
 * 🇯🇵 最短ルート案内画面
 * 🇨🇳 显示最短路径导航的页面
 * --------------------------------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    onBack: () -> Unit,
    cartViewModel: CartViewModel
) {
    MainScaffold(navController = androidx.navigation.compose.rememberNavController(), title = "最短ルート") { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("ここに最短ルート地図を表示します。")
        }
    }
}
