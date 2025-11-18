// =========================================================
// File: MainScreen.kt
// 設計書ID: main
// 画面名: メイン画面（アプリ入口）
// 役割:
//   - ログイン / 新規登録 への遷移。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("BAROGAKI") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(32.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = { navController.navigate(Routes.LOGIN) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ログイン")
            }

            Button(
                onClick = { navController.navigate(Routes.REGISTER) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("新規登録")
            }
        }
    }
}
