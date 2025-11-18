// =========================================================
// File: GpsPermissionScreen.kt
// 設計書ID: gps_permission
// 画面名: 位置情報許可画面
// 役割:
//   - アプリが位置情報を利用する理由を説明する。
//   - 「許可する」ボタンで店舗地図画面へ遷移する想定。
//   - 「許可しない」ボタンで前の画面へ戻る。
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
fun GpsPermissionScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("位置情報の許可") }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("近くの店舗を地図上に表示するため、位置情報の利用を許可してください。")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("許可しない")
                    }
                    Button(
                        onClick = {
                            navController.navigate(Routes.STORE_MAP)
                        }
                    ) {
                        Text("許可する")
                    }
                }
            }
        }
    }
}
