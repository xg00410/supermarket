// =========================================================
// File: FindPasswordScreen.kt
// 概要: ユーザーIDを入力し、パスワード再設定画面へ遷移するための画面。
// 設計書ID: Findpwd
// 画面名: パスワード再設定（認証）画面
// 対応する設計書シート:
// 更新者: 呉さん
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 🔍 FindPasswordScreen.kt
 * FindPwd 画面：ユーザーID + メールアドレス入力
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindPasswordScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var userId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("パスワードを探す") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("戻る", color = MaterialTheme.colorScheme.primary)
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (userId.isBlank() || email.isBlank()) {
                        errorMessage = "IDとメールアドレスを入力してください。"
                    } else {
                        errorMessage = ""
                        // ここ本来はサーバー照合 → OKなら次へ
                        onNext()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("次へ")
            }
        }
    }
}
