// =========================================================
// File: PasswordResetSuccessScreen.kt
// 概要: パスワード再設定の成功を知らせる画面。
// 設計書ID: Newpwdset_suc
// 画面名: パスワード再設定完了画面
// 更新者: 呉さん
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens.successscreen

import androidx.compose.runtime.Composable
import com.example.supermarket.ui.components.ResultTemplateScreen

@Composable
fun PasswordResetSuccessScreen(
    onBackToLogin: () -> Unit
) {
    ResultTemplateScreen(
        titleText = "パスワードを変更しました",
        buttonText = "ログイン画面へ",
        onButtonClick = onBackToLogin
    )
}