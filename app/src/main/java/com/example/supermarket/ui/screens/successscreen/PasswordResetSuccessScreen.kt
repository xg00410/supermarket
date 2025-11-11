package com.example.supermarket.ui.screens.successscreen

import androidx.compose.runtime.Composable
import com.example.supermarket.ui.components.ResultTemplateScreen

/**
 * パスワード変更完了画面 / 密码修改成功画面
 */
@Composable
fun PasswordResetSuccessScreen(onNext: () -> Unit) {
    ResultTemplateScreen(
        titleText = "パスワード変更完了",
        buttonText = "ログインへ戻る",
        onButtonClick = onNext
    )
}
