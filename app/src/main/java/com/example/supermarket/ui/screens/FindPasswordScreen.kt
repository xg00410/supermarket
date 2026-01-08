// =========================================================
// File: FindPasswordScreen.kt
// 設計書ID: findpwd
// 画面名: パスワード再設定（ID + メール入力）
// 役割:
//   - ユーザーIDとメールを入力し、次の画面へ進む。
//   - この画面ではDB更新は行わず、入力値を保持するだけ。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.PasswordResetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindPasswordScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var userId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("パスワード再設定（確認）") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ユーザーID：英数字のみ・最大文字数制限（入力禁止）
            val userIdMaxLen = 50
            val alnumRegex = Regex("^[a-zA-Z0-9]*$")

            OutlinedTextField(
                value = userId,
                onValueChange = { input ->
                    if (input.length <= userIdMaxLen && alnumRegex.matches(input)) {
                        userId = input
                    }
                },
                label = { Text("ユーザーID") },
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("登録メールアドレス") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    // 必須チェック
                    if (userId.isBlank() || email.isBlank()) {
                        errorMessage = "すべて入力してください。"
                        return@Button
                    }

                    // メール形式チェック
                    val emailPattern = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex()
                    if (!emailPattern.matches(email)) {
                        errorMessage = "メールアドレスの形式が正しくありません。"
                        return@Button
                    }

                    // 次画面で照合するため、入力値を保持して遷移する
                    PasswordResetState.userCode = userId
                    PasswordResetState.email = email

                    errorMessage = null
                    onNext()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("次へ")
            }


        }
    }
}
