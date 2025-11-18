// =========================================================
// File: RegisterScreen.kt
// 設計書ID: register
// 画面名: 新規登録画面
// 役割:
//   - 必要情報の入力
//   - 成功時：RegisterSuccessScreen へ遷移
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    var userId by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("新規登録") }) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = passwd,
                onValueChange = { passwd = it },
                label = { Text("パスワード") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("氏名") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    navController.navigate(Routes.REGISTER_SUCCESS)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("登録する")
            }
        }
    }
}
