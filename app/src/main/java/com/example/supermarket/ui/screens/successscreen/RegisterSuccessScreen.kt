// =========================================================
// File: RegisterSuccessScreen.kt
// 概要: 新規登録成功後に表示される確認画面。
// 設計書ID: Register_suc
// 画面名: 登録完了画面
// 更新者: 呉さん
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens.successscreen


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.ResultTemplateScreen

@Composable
fun RegisterSuccessScreen(
    navController: NavController
) {
    ResultTemplateScreen(
        titleText = "登録完了",
        buttonText = "ログインへ",
        onButtonClick = { navController.navigate(Routes.LOGIN) }
    )
}
