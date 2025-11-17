// =========================================================
// File: ResultTemplateScreen.kt
// 概要: 成功・失敗などの結果表示用テンプレート画面。
//設計書ID: なし（部品）
//画面名: 成功メッセージ共通画面
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.components



/**
 * ✅ ResultTemplateScreen.kt
 * -----------------------------------------------------------
 * 🇯🇵 成功・結果表示画面の共通テンプレート
 * 🇨🇳 注册成功、登录成功、密码修改成功等通用结果页模板
 * -----------------------------------------------------------
 */


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.supermarket.R   // ★ 自分の R を使う！ここが重要

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultTemplateScreen(
    titleText: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // ★ あなたの logo を使えるようになった！
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Result Icon",
                    modifier = Modifier.size(100.dp)
                )

                Text(
                    text = titleText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Button(
                    onClick = onButtonClick,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp)
                ) {
                    Text(buttonText, fontSize = 16.sp)
                }
            }
        }
    }
}
