// =========================================================
// File: ResultTemplateScreen.kt
// 共通結果画面テンプレート
// =========================================================

package com.example.supermarket.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultTemplateScreen(
    title: String,
    message: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(title, style = MaterialTheme.typography.headlineSmall)
                Text(message)
                Button(onClick = onClick) {
                    Text(buttonText)
                }
            }
        }
    }
}
