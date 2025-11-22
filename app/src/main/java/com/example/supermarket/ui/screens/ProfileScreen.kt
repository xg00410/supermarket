// =========================================================
// File: ProfileScreen.kt
// 設計書ID: profile
// 画面名: マイページ
// 役割:
//   - ログイン中ユーザーの情報表示
//   - 編集 / 履歴 / 設定 / 利用規約
//   - ログアウト機能（確認ダイアログ付）
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.UserSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onOrderHistory: () -> Unit,
    onSettings: () -> Unit,
    onTerms: () -> Unit,
    onLogout: () -> Unit
) {
    val userName = UserSession.userName ?: "未設定"
    val userCode = UserSession.userCode ?: "-"
    val phone = UserSession.phone ?: "-"
    val email = UserSession.email ?: "-"

    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("マイページ") },
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
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ---------------- ユーザー情報 ----------------
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("名前：$userName")
                    Text("ユーザーID：$userCode")
                    Text("メール：$email")
                    Text("電話番号：$phone")
                }
            }

            Divider()

            // ---------------- メニュー ----------------
            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("情報を編集")
            }

            Button(
                onClick = onOrderHistory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.History, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("購入履歴")
            }

            Button(
                onClick = onSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("設定")
            }

            Button(
                onClick = onTerms,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Description, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("利用規約")
            }

            Spacer(modifier = Modifier.weight(1f))

            // ---------------- Logout ----------------
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("ログアウト")
            }
        }
    }

    // ---------------- ログアウト確認ダイアログ ----------------
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("確認") },
            text = { Text("ログアウトしますか？") },
            confirmButton = {
                TextButton(onClick = {
                    UserSession.clear()
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("ログアウト")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }
}
