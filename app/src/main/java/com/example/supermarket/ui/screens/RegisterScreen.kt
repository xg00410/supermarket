// =========================================================
// ファイル名: RegisterScreen.kt
// 設計書ID: Register
// 画面名: 新規登録画面
// 役割: 新規ユーザーの情報を入力し、登録を行う画面。
//       「登録」「キャンセル（Mainへ戻る）」ボタンを持つ。
//       氏名／性別／電話番号／メールアドレスなど必須項目を入力。
// 更新者: 呉
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var agree by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("新規登録") },
                navigationIcon = {
                    IconButton(onClick = {onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
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
                label = { Text("ユーザーID（必須）") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード（必須）") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("氏名（必須）") },
                modifier = Modifier.fillMaxWidth()
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("性別", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = gender == "男",
                        onClick = { gender = "男" }
                    )
                    Text("男", modifier = Modifier.padding(end = 8.dp))

                    RadioButton(
                        selected = gender == "女",
                        onClick = { gender = "女" }
                    )
                    Text("女", modifier = Modifier.padding(end = 8.dp))

                    RadioButton(
                        selected = gender == "その他",
                        onClick = { gender = "その他" }
                    )
                    Text("その他")
                }
            }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス（必須）") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = agree,
                        onValueChange = { agree = it }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = agree, onCheckedChange = { agree = it })
                Text("利用規約に同意します")
            }

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 登録処理
            Button(
                onClick = {
                    if (userId.isBlank() || password.isBlank() || name.isBlank() || email.isBlank()) {
                        error = "必須項目を入力してください"
                    } else if (gender.isBlank()) {
                        error = "性別を選択してください"
                    }
                    else if (!agree) {
                        error = "利用規約に同意してください"
                    } else {
                        error = ""
                        onRegisterSuccess()   // 成功 → 成功画面へ
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("登録")
            }

        }
    }
}