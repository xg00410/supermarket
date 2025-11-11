package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

/**
 * 📝 RegisterScreen.kt
 * --------------------------------------------
 * 🇯🇵 新規登録画面：ユーザー情報を入力し、登録完了後登録成功画面へ。
 * 🇨🇳 新规注册界面：输入用户信息，注册成功后跳转注册成功画面。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text("新規登録", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(value = userId, onValueChange = { userId = it }, label = { Text("ユーザーID") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("パスワード") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("メールアドレス") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (userId.isNotBlank() && password.isNotBlank()) {
                        navController.navigate(Routes.REGISTER_SUCCESS)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("登録する")
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.popBackStack() }) {
                Text("戻る")
            }
        }
    }
}
