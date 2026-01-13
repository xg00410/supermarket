// =========================================================
// File: RegisterScreen.kt
// 設計書ID: register
// 画面名: 新規登録画面
// 役割:
//   - 必須項目（ユーザーID・パスワード）
//   - 氏名、電話番号、メールアドレスの任意入力
//   - PHP(register.php) と連携し、DB にユーザーを登録
//   - 利用規約の確認表示および同意チェック必須
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.models.RegisterBody
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import com.example.supermarket.ui.Routes
import kotlinx.coroutines.launch
import com.example.supermarket.data.PhoneFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    // 性別（任意）: 「男性」または「女性」を格納
    var gender by remember { mutableStateOf<String?>(null) }
    var agreed by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val api = remember { ApiClient.retrofit.create(ApiService::class.java) }

    // ------------------- 利用規約（アプリ内表示用） -------------------
    val termsText = """
【利用規約】

本アプリをご利用いただく前に、必ず以下の利用規約をお読みください。

1. 本アプリは、店舗内の商品検索・経路案内を目的としたサービスです。
2. 利用者はアカウント情報（ユーザーID・パスワード）を自己責任で管理するものとします。
3. 不正アクセス、他者になりすました登録行為を禁止します。
4. 商品情報・在庫情報は店舗データに基づきますが、完全な正確性を保証するものではありません。
5. 本アプリの利用に関連して発生した損害について、開発者は一切の責任を負いません。
6. 予告なくサービス内容を変更、停止する場合があります。
7. 本規約に同意いただけない場合、本アプリをご利用いただくことはできません。

（最終更新：2025年1月）
""".trimIndent()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新規登録") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ------------------- 入力項目 -------------------

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
                label = { Text("ユーザーID（必須）") },
                modifier = Modifier.fillMaxWidth()
            )


            // パスワード
            OutlinedTextField(
                value = password,
                onValueChange = { input ->
                    // Tab と改行(\n)を除外してパスワードに反映
                    password = input.filter { it != '\t' && it != '\n' }
                },
                label = { Text("パスワード（必須）") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            // パスワード（確認）
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { input ->
                    confirmPassword = input.filter { it != '\t' && it != '\n' }
                },
                label = { Text("パスワード（確認）") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            // 性別（任意）ラジオボタン
            Text("性別（任意）")
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = gender == "男性",
                    onClick = { gender = "男性" }
                )
                Text("男性")

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = gender == "女性",
                    onClick = { gender = "女性" }
                )
                Text("女性")
            }

            /// 氏名：記号の混入を抑止（入力禁止）
//  - 日本語氏名でよく使う「・」「ー」「-」とスペースは許可
            val nameMaxLen = 100
            val nameRegex = Regex("^[\\p{L}\\p{M}0-9\\s・ー-]*$")

            OutlinedTextField(
                value = name,
                onValueChange = { input ->
                    if (input.length <= nameMaxLen && nameRegex.matches(input)) {
                        name = input
                    }
                },
                label = { Text("氏名（必須）") },
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
                label = { Text("電話番号（半角数字・ハイフン不要）") },
                modifier = Modifier.fillMaxWidth()
            )


            // メールアドレス（任意）
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                modifier = Modifier.fillMaxWidth()
            )

            // ------------------- 利用規約チェック -------------------
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreed,
                    onCheckedChange = { agreed = it }
                )
                Text("利用規約に同意します")
            }

            // ------------------- 利用規約テキストボックス -------------------
            Surface(
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = termsText,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // ------------------- エラー表示 -------------------
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // ------------------- 登録ボタン -------------------
            Button(
                onClick = {
                    // 必須チェック（テスト仕様書に合わせて必須項目を拡張）
                    if (userId.isBlank() || password.isBlank() || name.isBlank() || phone.isBlank() || email.isBlank()) {
                        errorMessage = "ユーザーID、パスワード、氏名、電話番号、メールアドレスは必須です。"
                        return@Button
                    }

// ユーザーID文字数チェック（6文字以内はNG）
                    if (userId.length <= 5) {
                        errorMessage = "ユーザーIDは6文字以上で入力してください。"
                        return@Button
                    }


                    // パスワード強度チェック：8文字以上 & 英字 + 数字 を含む
                    val hasLetter = password.any { it.isLetter() }
                    val hasDigit = password.any { it.isDigit() }
                    if (password.length < 8 || !hasLetter || !hasDigit) {
                        errorMessage = "パスワードは8文字以上、英字と数字を含めてください。"
                        return@Button
                    }

                    // パスワード一致チェック
                    if (password != confirmPassword) {
                        errorMessage = "パスワードが一致しません。"
                        return@Button
                    }

                    // メールアドレス形式チェック（必須）
                    run {
                        val emailPattern = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex()
                        if (!emailPattern.matches(email)) {
                            errorMessage = "メールアドレスの形式が正しくありません。"
                            return@Button
                        }
                    }


                    // 利用規約同意チェック
                    if (!agreed) {
                        errorMessage = "利用規約に同意してください。"
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null


                    scope.launch {
                        try {
                            val body = RegisterBody(
                                user_code = userId,
                                password = password,
                                email = email,
                                name = name,
                                gender = gender,
                                phone = PhoneFormatter.format(phone)
                            )

                            val res = api.register(body)

                            if (res.status == "ok") {
                                isLoading = false
                                navController.navigate(Routes.REGISTER_SUCCESS) {
                                    popUpTo(Routes.REGISTER) { inclusive = true }
                                }
                            } else {
                                isLoading = false
                                errorMessage = res.message ?: "登録に失敗しました。"
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
                    Text("登録する")
                }
            }
        }
    }
}
