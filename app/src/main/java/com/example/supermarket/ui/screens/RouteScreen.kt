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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.Routes
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.models.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartItems = cartViewModel.cartItems

    // 店舗ごとにグループ化（1店舗のみを選択している想定だが、複数店舗にも対応）
    val groupedByStore = cartItems.groupBy { it.storeId }

    // チェック状態：productId → Boolean（通路上で「回収済み」のイメージ）
    val checkedMap = remember { mutableStateMapOf<Int, Boolean>() }

    // GPS開始・停止の簡易状態
    var gpsRunning by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("最短ルート") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.LIST) }) {
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
                .padding(12.dp)
        ) {

            // ============================================
            // 上部：地図エリア ＋ GPS ボタン
            // ============================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFE0E0E0)),   // 地図領域の簡易背景
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "店舗内ルート地図（ダミー表示）",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { gpsRunning = true }
                        ) {
                            Text("GPS開始")
                        }
                        OutlinedButton(
                            onClick = { gpsRunning = false }
                        ) {
                            Text("GPS停止")
                        }
                    }

                    if (gpsRunning) {
                        Text(
                            text = "現在地を追跡中…",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ============================================
            // 下部：エリア＋商品一覧
            // 設計書では「エリア／商品」2行構成イメージだが、
            // ここでは店舗名 → 商品というブロックで表現する。
            // ============================================
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // 店舗ごとにセクション表示
                items(groupedByStore.keys.toList()) { storeId ->
                    val itemsInStore = groupedByStore[storeId] ?: emptyList()
                    if (itemsInStore.isEmpty()) return@items

                    val store = StoreDataRepository.getStoreById(storeId)
                    val storeName = store?.storeName ?: itemsInStore.first().storeName

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(8.dp)
                    ) {
                        // 店舗名（エリア相当）
                        Text(
                            text = "店舗：$storeName",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        itemsInStore.forEach { item ->
                            RouteItemRow(
                                item = item,
                                checked = checkedMap[item.productId] ?: false,
                                onCheckedChange = { newChecked ->
                                    checkedMap[item.productId] = newChecked
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 最短ルート画面下部の 1 商品行
 * - 写真
 * - 商品名
 * - 数量
 * - チェックボックス（回収済みフラグ）
 */
@Composable
private fun RouteItemRow(
    item: CartItem,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 写真
            item.imageRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(64.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "数量：${item.quantity} 個",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
