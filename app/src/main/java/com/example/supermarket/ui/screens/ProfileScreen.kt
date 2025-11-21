// =========================================================
// File: ProfileScreen.kt
// 設計書ID: profile
// 画面名: マイページ
// 役割:
//   - 登録情報の簡単な表示。
//   - 「編集」「履歴」「設定」「利用規約」への導線を表示。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.StoreDataRepository


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
            TopAppBar(
                title = { Text("マイページ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Edit, contentDescription = "戻る")
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

            Text("ユーザー情報（ダミー）", style = MaterialTheme.typography.titleMedium)

            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("名前：テストユーザー")
                    Text("メール：test@example.com")
                    Text("電話番号：090-xxxx-xxxx")
                }
            }

            Divider()

            // 編集
            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("情報を編集する")
            }

            // 履歴
            Button(
                onClick = onOrderHistory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("購入履歴を見る")
            }

            // 設定
            Button(
                onClick = onSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("設定")
            }
            // -------------------------------------------------
            // データ取得モード切り替え（ダミー or DB）
            // -------------------------------------------------
            var useDbMode by remember { mutableStateOf(StoreDataRepository.useDatabaseMode) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("データ取得モード（DB を使用）")
                    Switch(
                        checked = useDbMode,
                        onCheckedChange = { checked ->
                            useDbMode = checked
                            StoreDataRepository.useDatabaseMode = checked
                        }
                    )
                }
                Text(
                    text = if (useDbMode) {
                        "現在：DBモード（PHP / MySQL）"
                    } else {
                        "現在：ダミーデータモード"
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // 利用規約
            Button(
                onClick = onTerms,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Description, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("利用規約")
            }
        }
    }
}
