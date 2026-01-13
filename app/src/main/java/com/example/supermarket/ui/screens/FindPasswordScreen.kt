// =========================================================
// File: FindPasswordScreen.kt
// 設計書ID: findpwd
// 画面名: パスワード再設定（ID + メール入力）
// 役割:
//   - ユーザーIDとメールを入力し、次の画面へ進む。
//   - この画面ではDB更新は行わず、入力値を保持するだけ。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.PasswordResetState
import com.example.supermarket.models.VerifyUserEmailBody
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindPasswordScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var userId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val api = remember { ApiClient.retrofit.create(ApiService::class.java) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("パスワード再設定（確認）") },
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

            // ユーザーID：英数字のみ・最大文字数制限（入力禁止）
            val userIdMaxLen = 50
            val alnumRegex = Regex("^[a-zA-Z0-9]*$")

            OutlinedTextField(
                value = userId,
                onValueChange = { input ->
                    if (input.length <= userIdMaxLen && alnumRegex.matches(input)) {
                        userId = input
                    }
                },
                label = { Text("ユーザーID") },
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("登録メールアドレス") },
                modifier = Modifier.fillMaxWidth()
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
                    // 入力値を正規化（前後空白を除去）
                    val uid = userId.trim()
                    val mail = email.trim()

                    // 必須チェック
                    if (uid.isBlank() || mail.isBlank()) {
                        errorMessage = "すべて入力してください。"
                        return@Button
                    }

                    // メール形式チェック
                    val emailPattern = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex()
                    if (!emailPattern.matches(mail)) {
                        errorMessage = "メールアドレスの形式が正しくありません。"
                        return@Button
                    }

                    // ★ 第1画面でユーザーID + メールの照合を行う（不一致なら遷移しない）
                    isLoading = true
                    errorMessage = null

                    scope.launch {
                        try {
                            val res = api.verifyUserEmail(
                                VerifyUserEmailBody(
                                    user_code = uid,
                                    email = mail
                                )
                            )

                            if (res.status == "ok") {
                                // 次画面用に入力値を保持して遷移
                                PasswordResetState.userCode = uid
                                PasswordResetState.email = mail

                                isLoading = false
                                onNext()
                            } else {
                                isLoading = false
                                errorMessage = res.message ?: "ユーザーIDまたはメールアドレスが一致しません。"
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
                Text("次へ")
            }
        }
    }
}
