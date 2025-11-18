package com.example.supermarket.ui.screens.successscreen

import androidx.compose.runtime.Composable
import com.example.supermarket.ui.components.ResultTemplateScreen

@Composable
fun RegisterSuccessScreen(onContinue: () -> Unit) {
    ResultTemplateScreen(
        title = "登録が完了しました",
        message = "ログイン画面へ進んでください",
        buttonText = "ログインへ",
        onClick = onContinue
    )
}
