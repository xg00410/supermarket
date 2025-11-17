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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes   // ← 必须加 import

/**
 * ProfileScreen
 * 🇯🇵 マイページ
 * 🇨🇳 个人主页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            Text("ユーザー情報", style = MaterialTheme.typography.titleMedium)

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("名前：未設定")
                    Text("メール：未設定")
                }
            }

            Divider()

            // 各メニュー項目（全て Routes.xxx に修正）
            ProfileMenuItem("情報編集") {
                navController.navigate(Routes.PROFILE_EDIT)
            }

            ProfileMenuItem("注文履歴") {
                navController.navigate(Routes.ORDER_HISTORY)
            }

            ProfileMenuItem("設定") {
                navController.navigate(Routes.SETTINGS)
            }

            ProfileMenuItem("ヘルプ") {
                navController.navigate(Routes.HELP)
            }

            ProfileMenuItem("利用規約") {
                navController.navigate(Routes.TERMS)
            }

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ログアウト")
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(text: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text, style = MaterialTheme.typography.bodyLarge)
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null)
        }
    }
}
