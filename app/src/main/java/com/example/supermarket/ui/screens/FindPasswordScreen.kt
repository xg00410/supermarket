// =========================================================
// File: FindPasswordScreen.kt
// 画面名: パスワード探し（メール入力画面）
// 役割:
//   - ユーザーIDとメールアドレスを入力し、次のパスワード再設定画面へ進む。
//   - 「戻る」ボタンで前の画面へ戻る。
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

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
            CenterAlignedTopAppBar(
                title = { Text("パスワードを探す") }
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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (userId.isBlank() || email.isBlank()) {
                        errorMessage = "IDとメールアドレスを入力してください"
                    } else {
                        errorMessage = ""
                        onNext()       // 次へ（成功画面へ）
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("次へ")
            }

            TextButton(onClick = onBack) {
                Text("戻る")
            }
        }
    }
}