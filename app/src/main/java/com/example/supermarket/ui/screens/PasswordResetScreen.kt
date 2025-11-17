// =========================================================
// File: PasswordResetScreen.kt
// 設計書ID: Newpwdset
// 画面名: 新パスワード設定画面
// 概要: 新しいパスワードの設定を行う再設定画面。
// 更新者: 呉さん
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons


/**
 * PasswordResetScreen
 * 🇯🇵 パスワード再設定画面
 * 🇨🇳 密码重新设定画面
 *
 * @param onBack 戻るボタン押下時 / 返回按钮
 * @param onSuccess パスワード変更成功時 / 修改成功后的处理
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var userId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("パスワード再設定") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("登録メールアドレス") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("新しいパスワード") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("新しいパスワード（確認）") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorText != null) {
                Text(
                    text = errorText!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (userId.isBlank() || email.isBlank() ||
                        newPassword.isBlank() || confirmPassword.isBlank()
                    ) {
                        errorText = "すべての項目を入力してください"
                    } else if (newPassword != confirmPassword) {
                        errorText = "パスワードが一致しません"
                    } else {
                        // TODO: 実際はサーバー側で更新 / 实际开发中应调用后端接口
                        errorText = null
                        onSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("パスワードを変更する")
            }
        }
    }
}
