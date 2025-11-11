package com.example.supermarket.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.supermarket.R

/**
 * ✅ ResultTemplateScreen.kt
 * -------------------------------------------------------------
 * 🇯🇵 成功画面の共通テンプレート
 * 各種成功画面（ログイン成功・登録成功・変更完了など）で共通使用。
 *
 * 🇨🇳 成功提示通用模板
 * 用于所有提示类界面（登录成功、注册成功、密码修改成功等）。
 * -------------------------------------------------------------
 */
@Composable
fun ResultTemplateScreen(
    titleText: String,        // 🏷️ タイトル / 标题
    buttonText: String,       // 🔘 ボタン文言 / 按钮文字
    onButtonClick: () -> Unit // 🎯 クリック処理 / 点击逻辑
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top   // 🔹 上方对齐
        ) {
            Spacer(modifier = Modifier.height(80.dp)) // 🔹 顶部间距稍大

            // 🖼️ ロゴ画像 / 应用Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(240.dp)   // 🚀 放大Logo尺寸
                    .padding(bottom = 60.dp),
                contentScale = ContentScale.Fit
            )

            // 🏷️ タイトルテキスト / 标题文字（更大更粗）
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(80.dp)) // 🔹 下方留白更宽

            // 🔘 メインボタン / 主按钮
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(buttonText, fontSize = 20.sp)
            }
        }
    }
}
