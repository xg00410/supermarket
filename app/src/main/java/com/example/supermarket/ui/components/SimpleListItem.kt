// =========================================================
// File: SimpleListItem.kt
// カスタム ListItem（Material3 互換 / Compose どのVersionでも動作）
// =========================================================

package com.example.supermarket.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SimpleListItem(
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 16.dp)
    ) {
        Text(text)
    }
    Divider()
}
