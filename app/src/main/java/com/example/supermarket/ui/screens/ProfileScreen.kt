// =========================================================
// File: ProfileScreen.kt
// 設計書ID: profile
// 画面名: マイページ
// 役割:
//   - ユーザーの簡易プロフィール情報を表示する。
//   - プロフィール編集画面、購入履歴画面、設定画面への入口。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("マイページ") },
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
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text("ユーザーID：sample_user")
            Text("氏名：山田 太郎")
            Text("メール：sample@example.com")

            Button(
                onClick = { navController.navigate(Routes.PROFILE_EDIT) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("プロフィールを編集する")
            }

            Button(
                onClick = { navController.navigate(Routes.ORDER_HISTORY) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("購入履歴を見る")
            }

            Button(
                onClick = { navController.navigate(Routes.SETTINGS) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("設定")
            }
        }
    }
}
