// =========================================================
// File: LoginSuccessScreen.kt
// 設計書ID: result_login
// 画面名: ログイン成功画面
// 役割:
//   - ログイン完了を知らせる。
//   - 5秒後に「店舗選択画面(StoreSelect)」へ自動遷移。
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
fun LoginSuccessScreen(
    onGoStoreSelect: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(5000)
        onGoStoreSelect()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("ログインに成功しました！", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))
            Text("5秒後に店舗選択画面へ移動します。")
        }
    }
}
