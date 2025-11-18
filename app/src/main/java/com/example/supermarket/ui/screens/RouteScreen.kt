// =========================================================
// File: RouteScreen.kt
// 設計書ID: route
// 画面名: 最短ルート画面
// 役割:
//   - カート内の商品をもとに、店舗内の巡回順（最短ルート）を確認する画面。
//   - 画面上部：店舗内地図（簡易表示）＋「GPS開始／停止」ボタン。
//   - 画面下部：エリア＋商品一覧（写真／商品名／数量／チェックボックス）。
//   - 戻るボタンでカート画面（list）へ戻る。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// 設計書ID: route
// 画面名: ルート案内画面
// 機能:
// - 上部に店内マップ（イメージ）を表示
// - 下部に「エリア」「商品名」「数量」「チェックボックス」を表示する2行構成のリスト
// - 「GPS開始」「GPS停止」ボタンでルート案内の開始/停止を制御
// - 戻るボタンでカート画面（list）へ戻る
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(navController: NavController) {

    // 仮のルートデータ / 临时路线数据
    val routeItems = listOf(
        RouteItem(area = "飲料コーナー", productName = "お〜いお茶 500ml", quantity = 2),
        RouteItem(area = "カップ麺コーナー", productName = "カップラーメン 醤油", quantity = 1),
        RouteItem(area = "飲料コーナー", productName = "コカ・コーラ 1.5L", quantity = 1)
    )

    val isGpsActive = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ルート案内") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .padding(16.dp)
        ) {
            // 上部: 店内マップ（ダミー）/ 上部: 店内地图（占位）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("店内マップ（ダミー）")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GPS開始・停止ボタン / GPS 开始・停止按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { isGpsActive.value = true },
                    enabled = !isGpsActive.value,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("GPS開始")
                }
                Button(
                    onClick = { isGpsActive.value = false },
                    enabled = isGpsActive.value,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("GPS停止")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 下部: エリア・商品情報リスト / 下部: 区域 + 商品信息列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(routeItems) { item ->
                    RouteItemRow(item = item)
                }
            }
        }
    }
}

// ルート表示用データクラス / 路线显示用数据类
data class RouteItem(
    val area: String,
    val productName: String,
    val quantity: Int
)

// 1行分の表示 / 单行显示
@Composable
private fun RouteItemRow(item: RouteItem) {
    val checked = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "エリア: ${item.area}")
                Text(text = "商品: ${item.productName}")
                Text(text = "数量: ${item.quantity}")
            }
            Checkbox(
                checked = checked.value,
                onCheckedChange = { checked.value = it }
            )
        }
    }
}
