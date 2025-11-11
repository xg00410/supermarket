package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.ui.Routes

/**
 * 🔐 PasswordResetScreen.kt
 * --------------------------------------------------------------
 * 🇯🇵 パスワード再設定画面
 * ユーザーが新しいパスワードを入力し、確認後に変更を確定する画面。
 *
 * 🇨🇳 密码重设输入界面
 * 用户可输入新的密码并确认，点击按钮后跳转到「成功画面」。
 * --------------------------------------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(onBack: () -> Unit, onSuccess: (String) -> Unit = {}) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("パスワード再設定") },
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
            // 🆕 新密码输入 / 新しいパスワード入力
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("新しいパスワード") },
                modifier = Modifier.fillMaxWidth()
            )

            // 🔁 确认密码输入 / 確認用パスワード入力
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("確認用パスワード") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }

            // ✅ 提交按钮 / 変更ボタン
            Button(
                onClick = {
                    when {
                        newPassword.isBlank() || confirmPassword.isBlank() ->
                            errorMessage = "パスワードを入力してください。"
                        newPassword != confirmPassword ->
                            errorMessage = "パスワードが一致しません。"
                        else -> {
                            errorMessage = ""
                            onSuccess(newPassword)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("変更する")
            }
        }
    }
}
