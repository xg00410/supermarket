// =========================================================
// File: LoginScreen.kt
// 設計書ID: login
// 画面名: ログイン画面
// 役割:
//   - ユーザーID と パスワードの入力
//   - 成功時：LoginSuccessScreen へ遷移
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ログイン") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    navController.navigate(Routes.LOGIN_SUCCESS)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ログイン")
            }

            TextButton(
                onClick = { navController.navigate(Routes.FIND_PASSWORD) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("パスワードを忘れた方はこちら")
            }
        }
    }
}
