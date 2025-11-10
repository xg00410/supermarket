package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.supermarket.ui.components.AppIcon
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AppIcon(width = 240, height = 80)
            Spacer(modifier = Modifier.height(20.dp))
            Text("欢迎使用 BAROGAKI 系统", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("用户ID") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("密码") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val api = ApiClient.retrofit.create(ApiService::class.java)

                            // ✅ 关键改动：构建 JSON 请求体
                            val json = Gson().toJson(mapOf(
                                "user_id" to userId,
                                "password" to password
                            ))
                            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

                            val response = api.login(requestBody)

                            if (response.status == "success") {
                                message = "欢迎 ${response.name ?: ""}"
                                onLoginSuccess()
                            } else {
                                message = response.message ?: "登录失败"
                            }
                        } catch (e: Exception) {
                            message = "网络错误: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("登 录")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("还没有账号？点击这里注册")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(message, color = MaterialTheme.colorScheme.primary)
        }
    }
}
