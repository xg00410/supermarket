// =========================================================
// File: RegisterSuccessScreen.kt
// 画面名: 新規登録 成功画面
// 役割:
//   - ResultTemplateScreen を使用して成功を表示
//   - 5秒後に自動でログイン画面へ遷移する
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.ResultTemplateScreen

@Composable
fun RegisterSuccessScreen(navController: NavController) {

    ResultTemplateScreen(
        titleText = "登録完了",
        buttonText = "ログインへ",   // UIには表示されないが、パラメータは保持
        onButtonClick = {
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.MAIN) { inclusive = false }
            }
        }
    )
}
