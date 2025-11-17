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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var pwd1 by remember { mutableStateOf("") }
    var pwd2 by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("パスワード再設定") }
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

            // 1回目のパスワード
            OutlinedTextField(
                value = pwd1,
                onValueChange = { pwd1 = it },
                label = { Text("新しいパスワード") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // 2回目のパスワード確認
            OutlinedTextField(
                value = pwd2,
                onValueChange = { pwd2 = it },
                label = { Text("新しいパスワード（確認）") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // エラー表示
            if (errorText.isNotEmpty()) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 登録ボタン
            Button(
                onClick = {
                    when {
                        pwd1.isBlank() || pwd2.isBlank() ->
                            errorText = "パスワードを入力してください"

                        pwd1 != pwd2 ->
                            errorText = "2回のパスワードが一致しません"

                        else -> {
                            errorText = ""
                            onSuccess() // → 成功画面へ
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("変更する")
            }

            // 戻るボタン
            TextButton(onClick = onBack) {
                Text("戻る")
            }
        }
    }
}