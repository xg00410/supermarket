package com.example.supermarket.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.supermarket.R

/**
 * AppIcon
 * 🇯🇵 アプリロゴ
 * 🇨🇳 主页面 Logo
 */
@Composable
fun AppIcon() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "App Logo",
        modifier = Modifier.size(200.dp) // 放大后的 Logo
    )
}
