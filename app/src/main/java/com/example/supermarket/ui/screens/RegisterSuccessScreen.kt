package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterSuccessScreen(onNext: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("登録成功") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("新規登録が完了しました！")
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = onNext) { Text("ログインへ進む") }
            }
        }
    }
}
