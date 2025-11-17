// =========================================================
// File: FindPasswordScreen.kt
// 画面名: パスワード探し（メール入力画面）
// 役割:
//   - ユーザーIDとメールアドレスを入力し、次のパスワード再設定画面へ進む。
//   - 「戻る」ボタンで前の画面へ戻る。
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ヘルプ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "アプリの使い方：\n" +
                        "・店舗を検索する\n" +
                        "・商品を選択する\n" +
                        "・カートから最短ルートへ進む\n" +
                        "・マイページで情報を確認する",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
