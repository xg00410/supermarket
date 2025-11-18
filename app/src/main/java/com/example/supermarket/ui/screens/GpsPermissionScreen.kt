// =========================================================
// File: GpsPermissionScreen.kt
// 設計書ID: gps_permission
// 画面名: 位置情報許可画面
// 役割:
//   - 位置情報利用の説明を表示。
//   - ユーザーに許可／拒否を選ばせる（ダミー処理）。
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpsPermissionScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("位置情報の許可") },
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

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("最短ルート案内のために、位置情報を利用します。")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("許可しない")
                    }
                    Button(onClick = { navController.popBackStack() }) {
                        Text("許可する")
                    }
                }
            }
        }
    }
}
