package com.example.supermarket.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 BAROGAKI 系统的主题配色
private val SupermarketColors = lightColorScheme(
    primary = Color(0xFF2196F3),      // 主色调（蓝色按钮）
    onPrimary = Color.White,          // 按钮文字白色
    background = Color.White,         // 背景白色
    onBackground = Color.Black,       // 字体黑色
    surface = Color.White,            // 卡片、输入框背景
    onSurface = Color.Black,          // 卡片文字
    secondary = Color(0xFF1976D2),    // 次级蓝（强调）
)

@Composable
fun SupermarketTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SupermarketColors,
        typography = Typography,   // 使用系统默认排版风格
        content = content
    )
}
