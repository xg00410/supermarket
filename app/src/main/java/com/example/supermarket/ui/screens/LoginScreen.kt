package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.ui.Routes

/**
 * 🔐 LoginScreen.kt
 * --------------------------------------------
 * 🇯🇵 ログイン画面：ユーザーIDとパスワードを入力し、認証成功後ログイン成功画面へ。
 * 🇨🇳 登录界面：输入用户ID与密码，认证成功后跳转登录成功画面。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text("ログイン", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (userId.isNotBlank() && password.isNotBlank()) {
                        navController.navigate(Routes.LOGIN_SUCCESS)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ログイン")
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate(Routes.REGISTER) }) {
                Text("新規登録はこちら")
            }
            TextButton(onClick = { navController.navigate(Routes.PASSWORD_RESET) }) {
                Text("パスワードを忘れた方はこちら")
            }
        }
    }
}
