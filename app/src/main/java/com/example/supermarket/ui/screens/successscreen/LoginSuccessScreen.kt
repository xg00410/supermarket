// =========================================================
// File: LoginSuccessScreen.kt
// 画面名: ログイン成功画面
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.ResultTemplateScreen

@Composable
fun LoginSuccessScreen(navController: NavController) {

    ResultTemplateScreen(
        titleText = "ログイン成功",
        buttonText = "店舗選択へ",   // 保持するだけ
        onButtonClick = {
            navController.navigate(Routes.STORE_SELECT) {
                popUpTo(Routes.MAIN) { inclusive = false }
            }
        }
    )
}
