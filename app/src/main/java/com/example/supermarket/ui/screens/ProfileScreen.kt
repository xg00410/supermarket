// =========================================================
// File: ProfileScreen.kt
// 概要: ユーザーのプロフィール情報を表示する画面。
//設計書ID: なし（追加機能）
//画面名: プロファイル画面
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onOrderHistory: () -> Unit,
    onSettings: () -> Unit,
    onTerms: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("マイページ") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) { Text("プロフィール編集") }

            Button(
                onClick = onOrderHistory,
                modifier = Modifier.fillMaxWidth()
            ) { Text("注文履歴") }

            Button(
                onClick = onSettings,
                modifier = Modifier.fillMaxWidth()
            ) { Text("設定") }

            Button(
                onClick = onTerms,
                modifier = Modifier.fillMaxWidth()
            ) { Text("利用規約") }

            TextButton(onClick = onBack) {
                Text("戻る")
            }
        }
    }
}