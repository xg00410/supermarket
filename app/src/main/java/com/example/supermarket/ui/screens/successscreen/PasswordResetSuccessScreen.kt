package com.example.supermarket.ui.screens.successscreen

import androidx.compose.runtime.Composable
import com.example.supermarket.ui.components.ResultTemplateScreen

@Composable
fun PasswordResetSuccessScreen(onContinue: () -> Unit) {
    ResultTemplateScreen(
        title = "パスワード変更完了",
        message = "新しいパスワードでログインできます",
        buttonText = "ログインへ",
        onClick = onContinue
    )
}
