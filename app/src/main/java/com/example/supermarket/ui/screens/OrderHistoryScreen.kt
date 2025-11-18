// =========================================================
// File: OrderHistoryScreen.kt
// 設計書ID: order_history
// 画面名: 履歴一覧画面
// 役割:
//   - 過去の購入履歴を、日付 → 店舗 → 商品の順に一覧表示する。
//   - 日付単位でセクション見出しを表示（新しい日付から順に並べる）。
//   - 同じ日の中では、注文時刻の新しい順に店舗ブロックを並べる。
//   - 日付行をタップして、その日の履歴を折りたたみ／展開できるようにする。
//   - 戻るボタンで前の画面に戻る（マイページまたはボトムナビの呼び出し元）。
// 更新者: 郭
// 更新日: 2025-11-19
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.viewmodel.CartViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val history = cartViewModel.orderHistory

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // 日付ごとの折りたたみ状態（デフォルト = 展開）
    val expandedStates = remember { mutableStateMapOf<LocalDate, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("履歴一覧") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("注文履歴はありません。")
            }
            return@Scaffold
        }

        // 日付（LocalDate）ごとにグルーピングし、新しい日付順に並べる
        val groupedByDate = history.groupBy { it.orderedAt.toLocalDate() }
            .toSortedMap(compareByDescending { it })

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            groupedByDate.forEach { (date, ordersInDate) ->

                val expanded = expandedStates[date] ?: true

                // ---- 日付セクションヘッダ（タップで開閉） ----
                item(key = "date_${date}") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedStates[date] = !(expandedStates[date] ?: true)
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = date.format(dateFormatter),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "閉じる" else "開く"
                        )
                    }
                    Divider()
                }

                if (expanded) {
                    // ---- 同一日付内の店舗ごとの注文 ----
                    val sortedOrders = ordersInDate.sortedByDescending { it.orderedAt }
                    items(sortedOrders, key = { it.orderId }) { order ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // 店舗名＋時刻
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = order.storeName,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = order.orderedAt.format(timeFormatter),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                Divider()

                                // 商品一覧
                                order.items.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(item.name)
                                            Text("数量：${item.quantity}")
                                        }
                                        Text("¥${(item.price * item.quantity).toInt()}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
