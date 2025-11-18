// =========================================================
// File: FindPasswordScreen.kt
// 画面名: パスワード検索
// 役割:
//   - ユーザーID を入力し、PasswordReset へ遷移
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
fun FindPasswordScreen(navController: NavController) {

    var userId by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("パスワード検索") }) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { navController.navigate(Routes.PASSWORD_RESET) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("次へ")
            }
        }
    }
}
