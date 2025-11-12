package com.example.supermarket.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.R

/**
 * 🧩 AppIcon.kt
 * -----------------------------------------------------------
 * 🇯🇵 アプリロゴ表示用の共通コンポーネント
 * 🇨🇳 用于显示应用程序图标的通用组件
 * -----------------------------------------------------------
 */
@Composable
fun AppIcon() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.sym_def_app_icon),
            contentDescription = "App Logo",
            modifier = Modifier.size(96.dp)
        )
    }
}
