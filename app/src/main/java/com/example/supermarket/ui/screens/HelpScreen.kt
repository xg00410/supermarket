// =========================================================
// File: HelpScreen.kt
// 概要: アプリの操作方法やFAQを表示するヘルプ画面。
// 設計書ID: なし（追加機能）
//画面名: ヘルプ画面
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * HelpScreen
 * 🇯🇵 ヘルプ
 * 🇨🇳 帮助中心
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ヘルプ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("• 店舗の選び方")
            Text("• 商品検索について")
            Text("• カート機能について")
            Text("• 最短ルートの仕様")
            Text("• その他よくある質問")
        }
    }
}
