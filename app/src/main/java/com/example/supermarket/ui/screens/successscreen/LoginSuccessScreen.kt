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
