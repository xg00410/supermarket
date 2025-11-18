// =========================================================
// File: TermsScreen.kt
// 設計書ID: terms
// 画面名: 利用規約
// 役割:
//   - アプリの簡易的な利用規約を表示。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(navController: NavController) {

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("利用規約") },
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
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {

            Text(
                text = "BAROGAKI 利用規約",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text("本アプリは、店舗内での商品探し・ルート案内を支援することを目的としています。")
            Spacer(modifier = Modifier.height(8.dp))
            Text("・表示される在庫情報や価格は、実際の店舗状況と異なる場合があります。")
            Spacer(modifier = Modifier.height(8.dp))
            Text("・アプリの利用により発生したトラブルについて、開発者は一切の責任を負いません。")
            Spacer(modifier = Modifier.height(8.dp))
            Text("・本アプリ内のデータは卒業制作の一環として作成されたサンプルです。")
        }
    }
}
