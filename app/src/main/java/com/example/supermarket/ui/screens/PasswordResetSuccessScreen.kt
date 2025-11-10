package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetSuccessScreen(onBackToLogin: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("パスワード変更完了") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("パスワードが正常に変更されました。")
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = onBackToLogin) { Text("ログイン画面へ戻る") }
            }
        }
    }
}
