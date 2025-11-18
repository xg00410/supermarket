// =========================================================
// File: SettingsScreen.kt
// 設定画面（SimpleListItem 版）
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.SimpleListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            SimpleListItem(
                text = "利用規約",
                onClick = { navController.navigate(Routes.TERMS) }
            )

            SimpleListItem(
                text = "ヘルプ・お問い合わせ",
                onClick = { navController.navigate(Routes.HELP) }
            )

            SimpleListItem(
                text = "位置情報の設定",
                onClick = { navController.navigate(Routes.GPS_PERMISSION) }
            )
        }
    }
}
