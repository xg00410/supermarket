// =========================================================
// File: LoginScreen.kt
// 設計書ID: login
// 画面名: ログイン画面
// 役割:
//   - ユーザーIDとパスワードを入力。
//   - 「ログイン」ボタンでログイン成功画面へ遷移。
//   - 「パスワードをお忘れの方」から FindPassword へ。
//   - 「新規登録」から RegisterScreen へ。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ログイン") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    if (userId.isBlank() || password.isBlank()) {
                        errorMessage = "すべて入力してください。"
                    } else {
                        // ※ 本来はAPI認証
                        navController.navigate(Routes.LOGIN_SUCCESS)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ログイン")
            }

            TextButton(
                onClick = { navController.navigate(Routes.FIND_PASSWORD) }
            ) {
                Text("パスワードをお忘れの方はこちら")
            }

            TextButton(
                onClick = { navController.navigate(Routes.REGISTER) }
            ) {
                Text("新規登録")
            }
        }
    }
}
