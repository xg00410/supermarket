package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.AppIcon

/**
 * MainScreen
 * 🇯🇵 アプリ起動時のメイン画面（ログイン / 新規登録）
 * 🇨🇳 应用启动后的主画面（登录 / 新规注册）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("スーパー導購システム") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 放大的 LOGO
            AppIcon()

            Spacer(modifier = Modifier.height(24.dp))

            // ログインボタン / 登录按钮
            Button(
                onClick = { navController.navigate(Routes.LOGIN) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ログイン")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 新規登録ボタン / 新规注册按钮
            OutlinedButton(
                onClick = { navController.navigate(Routes.REGISTER) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("新規登録")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // フッター / 底部版权
            Text(
                text = "© 2025 BAROGAI チーム",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
