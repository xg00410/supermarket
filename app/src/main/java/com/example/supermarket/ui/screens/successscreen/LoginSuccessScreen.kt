// =========================================================
// File: LoginSuccessScreen.kt
// 概要: ログイン成功時に表示される確認画面。
// 設計書ID: Login_suc
// 画面名: ログイン成功画面
// 更新者: 呉さん
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens.successscreen

import androidx.compose.runtime.Composable
import com.example.supermarket.ui.components.ResultTemplateScreen

/**
 * ログイン成功画面 / 登录成功画面
 */
@Composable
fun LoginSuccessScreen(onNext: () -> Unit) {
    ResultTemplateScreen(
        titleText = "ログイン成功！",
        buttonText = "次へ",
        onButtonClick = onNext
    )
}
