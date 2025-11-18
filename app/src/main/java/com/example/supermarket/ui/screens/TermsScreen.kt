// =========================================================
// File: TermsScreen.kt
// 設計書ID: terms
// 画面名: 利用規約画面
// 役割:
//   - 利用規約文言を表示する。
//   - 戻るボタンで前の画面に戻る。
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("利用規約") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            Text(
                text = "ここに利用規約の文言を表示します。（ダミーテキスト）",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
