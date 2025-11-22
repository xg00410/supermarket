// =========================================================
// File: PasswordResetSuccessScreen.kt
// 画面名: パスワード再設定 成功画面
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.ResultTemplateScreen

@Composable
fun PasswordResetSuccessScreen(navController: NavController) {

    ResultTemplateScreen(
        titleText = "パスワードを変更しました",
        buttonText = "ログインへ戻る",   // UI表示なし、呼び元互換のため残す
        onButtonClick = {
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.MAIN) { inclusive = false }
            }
        }
    )
}
