// =========================================================
// File: HelpScreen.kt
// 設計書ID: help
// 画面名: ヘルプ画面
// 役割:
//   - アプリの使い方を簡単に説明する静的な画面。
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
fun HelpScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ヘルプ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("このアプリの使い方", style = MaterialTheme.typography.titleMedium)
                Text("1. ログインまたは新規登録を行います。")
                Text("2. 店舗選択画面で行きたい店舗を選びます。")
                Text("3. 商品一覧画面でカートに商品を追加します。")
                Text("4. カート画面から最短ルートを確認できます。")
                Text("5. 買い終わった商品はルート画面でチェックし、履歴として確認できます。")
            }
        }
    }
}
