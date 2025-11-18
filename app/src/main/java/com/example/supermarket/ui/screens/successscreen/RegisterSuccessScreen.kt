// =========================================================
// File: RegisterSuccessScreen.kt
// 設計書ID: result_register
// 画面名: 新規登録成功画面
// 役割:
//   - 登録成功を知らせる。
//   - 5秒後にログイン画面へ自動遷移。
// =========================================================

package com.example.supermarket.ui.screens.successscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun RegisterSuccessScreen(
    onBackToLogin: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(5000)
        onBackToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("登録が完了しました！", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))
            Text("5秒後にログイン画面へ戻ります。")
        }
    }
}
