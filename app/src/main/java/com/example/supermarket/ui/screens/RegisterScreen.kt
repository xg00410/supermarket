// =========================================================
// ファイル名: RegisterScreen.kt
// 設計書ID: Register
// 画面名: 新規登録画面
// 役割: 新規ユーザーの情報を入力し、登録を行う画面。
//       「登録」「キャンセル（Mainへ戻る）」ボタンを持つ。
//       氏名／性別／電話番号／メールアドレスなど必須項目を入力。
// 更新者: 呉
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("男") }   // ←★ 性別追加
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var agreed by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("新規登録") }
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

            // ユーザーID
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("ユーザーID（必須）") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // パスワード
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("パスワード（必須）") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // 氏名
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("氏名（必須）") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // ★ 性別（設計書必須）
            Text("性別（必須）")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == "男",
                    onClick = { gender = "男" }
                )
                Text("男")
                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = gender == "女",
                    onClick = { gender = "女" }
                )
                Text("女")
            }

            // 電話番号
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("電話番号") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // メール
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス（必須）") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // 規約同意
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = agreed,
                    onCheckedChange = { agreed = it }
                )
                Text("利用規約に同意します")
            }

            if (errorText != null) {
                Text(
                    text = errorText!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // ボタン（キャンセル + 登録）
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // ★ キャンセル → Main へ戻る
                OutlinedButton(
                    onClick = { navController.navigate(Routes.MAIN) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("キャンセル")
                }

                // ★ 登録 → Register_suc
                Button(
                    onClick = {
                        if (userId.isBlank() || password.isBlank() || name.isBlank() || email.isBlank()) {
                            errorText = "必須項目を入力してください"
                        } else if (!agreed) {
                            errorText = "利用規約に同意してください"
                        } else {
                            errorText = null
                            navController.navigate(Routes.REGISTER_SUCCESS)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("登録")
                }
            }
        }
    }
}
