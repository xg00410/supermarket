package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.components.MainScaffold

/**
 * 🗺️ RouteScreen.kt
 * 最短ルート案内画面 / 最短路径导航画面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(navController: NavController) {
    MainScaffold(navController = navController, title = "最短ルート") { padding ->
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
