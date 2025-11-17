// =========================================================
// File: ProfileEditScreen.kt
// 概要: プロフィール情報（名前・電話番号・メール等）を編集する画面。
//設計書ID: なし（追加機能）
//画面名: プロファイル編集画面
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@Composable
fun ProfileEditScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit
)

 {

    // -----------------------------------------------
    // ★ 本来は DB / API から取得した値をセット
    //   今は仮データ（後で PHP 連動可能）
    // -----------------------------------------------
    var userName by remember { mutableStateOf("山田 太郎") }
    var email by remember { mutableStateOf("taro@example.com") }
    var phone by remember { mutableStateOf("080-1234-5678") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("情報編集") },
                navigationIcon = {
                    IconButton(onClick = {onBack()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // -----------------------------
            // 入力項目
            // -----------------------------
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("氏名") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("メールアドレス") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("電話番号") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // -----------------------------
            // 保存ボタン
            // -----------------------------
            Button(
                onClick = {
                    // ★ 後で PHP API と連動して DB 更新
                    onSaved()

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("保存する")
            }
        }
    }
}
