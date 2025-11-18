// =========================================================
// File: ProfileEditScreen.kt
// 設計書ID: profile_edit
// 画面名: プロフィール編集画面
// 役割:
//   - ユーザー情報（名前・電話番号・メール）を編集する画面。
//   - 実際の保存処理はダミー。UIフローを構築。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    var name by remember { mutableStateOf("テストユーザー") }
    var phone by remember { mutableStateOf("090-xxxx-xxxx") }
    var email by remember { mutableStateOf("test@example.com") }

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("名前") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("電話番号") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSaved,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("保存する")
            }
        }
    }
}
