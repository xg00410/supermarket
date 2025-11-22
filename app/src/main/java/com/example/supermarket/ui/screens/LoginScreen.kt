// =========================================================
// File: LoginScreen.kt
// 設計書ID: login
// 画面名: ログイン画面
// 役割:
//   - ユーザーIDとパスワードを入力。
//   - PHP(login.php) と連携し、成功時はセッションを保持して遷移。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.UserSession
import com.example.supermarket.models.LoginBody
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import com.example.supermarket.ui.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val api = remember { ApiClient.retrofit.create(ApiService::class.java) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("ログイン") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    if (userId.isBlank() || password.isBlank()) {
                        errorMessage = "ユーザーIDとパスワードを入力してください。"
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null

                    scope.launch {
                        try {
                            val res = api.login(LoginBody(userId, password))
                            if (res.status == "ok") {
                                // ★ セッションに保存
                                UserSession.userId = res.user_id
                                UserSession.userCode = res.user_code ?: userId
                                UserSession.userName = res.name ?: userId
                                UserSession.phone = res.phone
                                UserSession.email = res.email

                                isLoading = false
                                navController.navigate(Routes.LOGIN_SUCCESS) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                            } else {
                                isLoading = false
                                errorMessage = res.message ?: "ログインに失敗しました。"
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            errorMessage = "通信エラーが発生しました。"
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("ログイン")
                }
            }

            TextButton(
                onClick = { navController.navigate(Routes.FIND_PASSWORD) }
            ) {
                Text("パスワードをお忘れの方はこちら")
            }

            TextButton(
                onClick = { navController.navigate(Routes.REGISTER) }
            ) {
                Text("新規登録")
            }
        }
    }
}
