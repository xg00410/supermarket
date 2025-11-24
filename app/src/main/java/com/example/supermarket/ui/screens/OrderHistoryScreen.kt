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
import com.example.supermarket.data.UserSession
import com.example.supermarket.models.OrderHistory
import com.example.supermarket.models.OrderHistoryItem
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // 各日付の展開状態
    val expandedStates = remember { mutableStateMapOf<LocalDate, Boolean>() }

    // ★ DB から取得した履歴リスト
    var history by remember { mutableStateOf<List<OrderHistory>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // ★ 初回表示時に get_orders.php から取得
    LaunchedEffect(Unit) {
        val userId = UserSession.userId
        if (userId == null) {
            errorMessage = "ログイン情報が見つかりません。先にログインしてください。"
            isLoading = false
            return@LaunchedEffect
        }

        try {
            val api = ApiClient.retrofit.create(ApiService::class.java)
            val res = api.getOrders(userId)
            if (res.status == "ok" && res.data != null) {
                history = res.data.map { dto ->
                    OrderHistory(
                        orderId = dto.order_id,
                        storeId = dto.store_id,
                        storeName = dto.store_name,
                        orderedAt = try {
                            // "YYYY-MM-DD HH:MM:SS" -> LocalDateTime
                            LocalDateTime.parse(dto.ordered_at.replace(" ", "T"))
                        } catch (e: Exception) {
                            LocalDateTime.now()
                        },
                        items = dto.items.map { itemDto ->
                            OrderHistoryItem(
                                productId = itemDto.product_id,
                                name = itemDto.name,
                                price = itemDto.price.toDouble(),
                                quantity = itemDto.quantity
                            )
                        }
                    )
                }
                errorMessage = null
            } else {
                errorMessage = res.message ?: "履歴の取得に失敗しました。"
            }
        } catch (e: Exception) {
            errorMessage = "通信エラーが発生しました。"
        } finally {
            isLoading = false
        }
    }


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

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(errorMessage!!)
            }
            return@Scaffold
        }

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
