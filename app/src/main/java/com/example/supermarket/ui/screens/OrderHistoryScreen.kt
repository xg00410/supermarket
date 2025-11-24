// =========================================================
// File: OrderHistoryScreen.kt
// 画面名: 注文履歴画面
// 役割:
//   - CartViewModel 内の orderHistory（ローカルセッション内の履歴）を表示。
//   - 日付ごとにグルーピングして、開閉可能なリストとして表示する。
//   - ★将来対応予定:
//       DB側に get_orders.php を用意し、ユーザーID単位の全履歴を取得して
//       表示する形に拡張する（bパート）。
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

    // 各日付の展開状態
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

        val groupedByDate =
            history.groupBy { it.orderedAt.toLocalDate() }
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

                // ------------ 日付ヘッダ ------------
                item("date_$date") {
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
                            contentDescription = null
                        )
                    }
                    Divider()
                }

                if (expanded) {
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

                                // 店铺名＋时刻
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(order.storeName, style = MaterialTheme.typography.titleMedium)
                                    Text(order.orderedAt.format(timeFormatter))
                                }

                                Divider()

                                // 商品列表
                                order.items.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
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
