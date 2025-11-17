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
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.ResultTemplateScreen

@Composable
fun LoginSuccessScreen(
    navController: NavController
) {
    ResultTemplateScreen(
        titleText = "ログイン成功",
        buttonText = "店舗選択へ",
        onButtonClick = {
            navController.navigate(Routes.STORE_SELECT) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }

    )
}