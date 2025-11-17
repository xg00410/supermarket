// =========================================================
// File: TermsScreen.kt
// 概要: 利用規約を表示する画面。
//設計書ID: なし（追加機能）
//画面名: 利用規約画面
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
 * TermsScreen
 * 🇯🇵 利用規約
 * 🇨🇳 使用条款
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("利用規約") },
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
                .padding(16.dp),
        ) {
            Text("本アプリの利用規約は以下の通りです・・・（省略）")
        }
    }
}
