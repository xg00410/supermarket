// =========================================================
// File: ProfileEditScreen.kt
// 設計書ID: profile_edit
// 画面名: プロフィール編集画面
// 役割:
//   - ユーザー情報（名前・電話番号・メール）を編集し、PHP(update_profile.php) へ保存。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.PhoneFormatter
import com.example.supermarket.data.UserSession
import com.example.supermarket.models.UpdateProfileBody
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val currentName = UserSession.userName ?: ""
    val currentPhone = UserSession.phone ?: ""
    val currentEmail = UserSession.email ?: ""

    var name by remember { mutableStateOf(currentName) }
    var phone by remember { mutableStateOf(currentPhone) }
    var email by remember { mutableStateOf(currentEmail) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val api = remember { ApiClient.retrofit.create(ApiService::class.java) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("プロフィール編集") },
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

            // 名前：数字・記号を禁止（漢字/ひらがな/カタカナ/英字/空白/・/ーのみ許可）
            OutlinedTextField(
                value = name,
                onValueChange = { input ->
                    val filtered = input.filter { ch ->
                        ch == ' ' || ch == '・' || ch == 'ー' ||
                                ch.isLetter() || // 英字 +（環境によっては）一部文字
                                Character.UnicodeScript.of(ch.code) in setOf(
                            Character.UnicodeScript.HAN,       // 漢字
                            Character.UnicodeScript.HIRAGANA,  // ひらがな
                            Character.UnicodeScript.KATAKANA   // カタカナ
                        )
                    }
                    // 入力禁止（不正文字は反映しない）
                    name = filtered
                },
                label = { Text("名前") },
                modifier = Modifier.fillMaxWidth()
            )


            // 電話番号：半角数字のみ・最大11桁（入力禁止）
            val phoneMaxLen = 11

            OutlinedTextField(
                value = phone,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    if (digits.length <= phoneMaxLen) {
                        phone = digits
                    }
                },
                label = { Text("電話番号") },
                modifier = Modifier.fillMaxWidth()
            )



            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val uid = UserSession.userId
                    if (uid == null) {
                        errorMessage = "ログイン情報が見つかりません。再ログインしてください。"
                        return@Button
                    }

                    // 電話番号チェック（表示上は「-」を許可するが、判定は数字のみで行う）
                    val phoneDigits = phone.filter { it.isDigit() }

                    if (phone.isNotBlank() && !phoneDigits.matches(Regex("^\\d{11}$"))) {
                        errorMessage = "電話番号は半角数字11桁で入力してください。"
                        return@Button
                    }



// メールアドレスチェック（必須・形式のみ）
                    if (email.isBlank()) {
                        errorMessage = "メールアドレスを入力してください。"
                        return@Button
                    }

                    val emailPattern = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex()
                    if (!emailPattern.matches(email)) {
                        errorMessage = "メールアドレスの形式が正しくありません。"
                        return@Button
                    }


                    isLoading = true
                    errorMessage = null

                    scope.launch {
                        try {
                            val res = api.updateProfile(
                                UpdateProfileBody(
                                    user_id = uid,
                                    name = if (name.isBlank()) null else name,
                                    phone = if (phone.isBlank()) null else PhoneFormatter.format(phoneDigits),
                                    email = if (email.isBlank()) null else email
                                )
                            )

                            if (res.status == "ok") {
                                // ★ セッションも更新
                                UserSession.userName = name
                                UserSession.phone = if (phone.isBlank()) "" else PhoneFormatter.format(phoneDigits)
                                UserSession.email = email

                                isLoading = false
                                onSaved()
                            } else {
                                isLoading = false
                                errorMessage = res.message ?: "保存に失敗しました。"
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
                    Text("保存する")
                }
            }
        }
    }
}
