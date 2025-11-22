// =========================================================
// File: ResultTemplateScreen.kt
// 画面名: 成功表示テンプレート
// 役割:
//   - titleText / buttonText / onButtonClick など
//     元の引数を全て保持しつつ、5 秒後に自動遷移する。
//   - 画面上にはボタンを表示しない。
// =========================================================

package com.example.supermarket.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultTemplateScreen(
    titleText: String,
    buttonText: String,             // ← 保留だけど画面に表示しない
    onButtonClick: () -> Unit       // ← 5 秒後にこれを呼ぶ
) {
    // ★ 5 秒後に自動遷移
    LaunchedEffect(Unit) {
        delay(5000)
        onButtonClick()
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // タイトル
            Text(
                text = titleText,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(20.dp))

            // 案内
            Text(
                text = "5秒後に自動的に次の画面へ移動します…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // 注意：ここにはもうボタンを置かない
            // （buttonText は保持するが UI には表示しない）
        }
    }
}
