package com.example.supermarket.ui.screens.successscreen

import androidx.compose.runtime.Composable
import com.example.supermarket.ui.components.ResultTemplateScreen

@Composable
fun LoginSuccessScreen(onContinue: () -> Unit) {
    ResultTemplateScreen(
        title = "ログイン成功",
        message = "ようこそ！",
        buttonText = "店舗選択へ",
        onClick = onContinue   // ★ ここ onClick
    )
}
