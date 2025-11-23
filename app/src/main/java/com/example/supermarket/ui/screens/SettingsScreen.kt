// =========================================================
// File: SettingsScreen.kt
// 設計書ID: settings
// 画面名: 設定画面
// 役割:
//   - データ取得モード（DB / ダミー）の切り替えを行う。
//   - 通知 / ダークモードは仕様から除外（デザインのみだったため削除）。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.StoreDataRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    var useDbMode by remember { mutableStateOf(StoreDataRepository.useDatabaseMode) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text("データ取得モード", style = MaterialTheme.typography.titleMedium)

// 🇯🇵 DBモード固定：切替スイッチは表示しない
// 🇨🇳 固定使用 DB 模式：不再显示切换开关

            Text(
                text = "現在：DBモード（PHP / MySQL からデータを取得します）",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
