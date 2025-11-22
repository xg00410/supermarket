// =========================================================
// File: PasswordResetScreen.kt
// 設計書ID: newpwdset
// 画面名: パスワード再設定（新パスワード入力）
// 役割:
//   - 新しいパスワードを2回入力し、PHP(reset_password.php) で更新する。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.PasswordResetState
import com.example.supermarket.models.ResetPasswordBody
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var pwd1 by remember { mutableStateOf("") }
    var pwd2 by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val api = remember { ApiClient.retrofit.create(ApiService::class.java) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("パスワード再設定") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = pwd1,
                onValueChange = { pwd1 = it },
                label = { Text("新しいパスワード") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            OutlinedTextField(
                value = pwd2,
                onValueChange = { pwd2 = it },
                label = { Text("新しいパスワード（確認）") },
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

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val userCode = PasswordResetState.userCode
                    val email = PasswordResetState.email

                    if (userCode.isNullOrBlank() || email.isNullOrBlank()) {
                        errorMessage = "最初の画面からやり直してください。"
                        return@Button
                    }
                    if (pwd1.isBlank() || pwd2.isBlank()) {
                        errorMessage = "すべて入力してください。"
                        return@Button
                    }
                    if (pwd1 != pwd2) {
                        errorMessage = "パスワードが一致しません。"
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null

                    scope.launch {
                        try {
                            val res = api.resetPassword(
                                ResetPasswordBody(
                                    user_code = userCode,
                                    email = email,
                                    new_password = pwd1
                                )
                            )
                            if (res.status == "ok") {
                                isLoading = false
                                PasswordResetState.clear()
                                onSuccess()
                            } else {
                                isLoading = false
                                errorMessage = res.message ?: "変更に失敗しました。"
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            errorMessage = "通信エラーが発生しました。"
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("変更する")
                }
            }
        }
    }
}
