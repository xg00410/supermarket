// =========================================================
// File: RegisterScreen.kt
// 設計書ID: register
// 画面名: 新規登録画面
// 役割:
//   - ユーザーID, パスワード, 氏名, 電話番号, メールを入力。
//   - 「ID重複チェック」ボタンで簡易チェック（ダミー）。
//   - 「登録」ボタンで登録成功画面へ遷移。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var idCheckMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var agreed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新規登録") },
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
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    idCheckMessage =
                        if (userId.trim().lowercase() == "test") "このIDは既に使用されています。"
                        else "使用可能なIDです。"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ID重複チェック")
            }

            if (idCheckMessage.isNotEmpty()) {
                Text(idCheckMessage)
            }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("名前") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("電話番号") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreed,
                    onCheckedChange = { agreed = it }
                )
                Text("利用規約に同意します")
            }

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (
                        userId.isBlank() ||
                        password.isBlank() ||
                        name.isBlank() ||
                        phone.isBlank() ||
                        email.isBlank()
                    ) {
                        errorMessage = "必要項目を入力してください。"
                    } else if (!agreed) {
                        errorMessage = "利用規約に同意してください。"
                    } else {
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("登録する")
            }
        }
    }
}
