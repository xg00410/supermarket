// =========================================================
// ファイル名: LoginScreen.kt
// 設計書ID: Login
// 画面名: ログイン画面
// 役割: ユーザーIDとパスワードを入力し、認証を行う画面。
//       「ログイン」「戻る」「パスワード忘れ」ボタンを持つ。
// 更新者: 吴
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
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ログイン") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ユーザーID入力
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // パスワード入力
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // エラーメッセージ
            if (errorText != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorText!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ログインボタン（→ Login_suc）
            Button(
                onClick = {
                    if (userId.isNotBlank() && password.isNotBlank()) {
                        errorText = null
                        navController.navigate(Routes.LOGIN_SUCCESS)
                    } else {
                        errorText = "ユーザーIDとパスワードを入力してください"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ログイン")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // パスワードを忘れたボタン（→ Findpwd）
            TextButton(
                onClick = { navController.navigate(Routes.FIND_PASSWORD) }
            ) {
                Text("パスワードをお忘れの方はこちら")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 戻る（→ Main）
            TextButton(
                onClick = { navController.navigate(Routes.MAIN) }
            ) {
                Text("戻る")
            }
        }
    }
}
