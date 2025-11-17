// =========================================================
// File: AppIcon.kt
// 概要: アプリ共通のロゴ・アイコン表示コンポーネント。
//設計書ID: なし（部品）
//画面名: アプリアイコン（共通UI）
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

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
