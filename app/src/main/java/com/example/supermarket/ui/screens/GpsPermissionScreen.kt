// =========================================================
// File: GpsPermissionScreen.kt
// 画面名: 位置情報の許可確認画面
// 役割:
//   - ユーザーに位置情報利用の許可を求める。
//   - 「許可する」→ 店舗地図検索画面へ遷移。
//   - 「許可しない」→ 前の画面へ戻る。
// 更新者: 郭
// 更新日: 2025-11-19
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpsPermissionScreen(navController: NavController) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("位置情報の許可") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("アプリが位置情報を利用します。許可しますか？")
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 「許可する」
                Button(
                    onClick = { navController.navigate(Routes.STORE_MAP) }
                ) {
                    Text("許可する")
                }

                // 「許可しない」
                OutlinedButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Text("許可しない")
                }
            }
        }
    }
}
