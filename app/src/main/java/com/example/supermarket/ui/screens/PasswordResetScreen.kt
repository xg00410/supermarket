// =========================================================
// File: PasswordResetScreen.kt
// 設計書ID: newpwdset
// 画面名: パスワード再設定（新パスワード入力）
// 役割:
//   - 新しいパスワードを2回入力。
//   - 一致すれば成功画面へ遷移。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var pwd1 by remember { mutableStateOf("") }
    var pwd2 by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("パスワード再設定（新規）") },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = pwd1,
                onValueChange = { pwd1 = it },
                label = { Text("新しいパスワード") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pwd2,
                onValueChange = { pwd2 = it },
                label = { Text("新しいパスワード（再入力）") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (pwd1.isBlank() || pwd2.isBlank()) {
                        errorMessage = "すべて入力してください。"
                    } else if (pwd1 != pwd2) {
                        errorMessage = "パスワードが一致しません。"
                    } else {
                        onSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("変更する")
            }
        }
    }
}
