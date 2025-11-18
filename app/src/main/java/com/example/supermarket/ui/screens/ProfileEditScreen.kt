// =========================================================
// File: ProfileEditScreen.kt
// 設計書ID: profile_edit
// 画面名: プロフィール編集
// 役割:
//   - ユーザーの基本情報を編集する（ダミー実装）。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(navController: NavController) {

    var name by remember { mutableStateOf("山田 太郎") }
    var email by remember { mutableStateOf("sample@example.com") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("プロフィール編集") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("氏名") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    // 実際のアプリではここでサーバー更新などを行う
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("保存する")
            }
        }
    }
}
