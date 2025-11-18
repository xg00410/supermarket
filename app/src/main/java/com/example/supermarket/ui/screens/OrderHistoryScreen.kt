// =========================================================
// File: OrderHistoryScreen.kt
// 設計書ID: order_history
// 画面名: 購入履歴
// 役割:
//   - 日付ごとに購入履歴をグループ化。
//   - 同じ日付内で店舗ごとに履歴を分類。
//   - 商品画像・名称・数量・価格・小計を表示。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.HistoryRepository
import com.example.supermarket.models.HistoryItem
import com.example.supermarket.models.HistoryProduct
import com.example.supermarket.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(navController: NavController) {

    val historyList = HistoryRepository.getAll()

    // 日付を yyyy/MM/dd 単位でグループ化
    val groupedByDate: Map<String, List<HistoryItem>> = historyList.groupBy { item ->
        item.dateTime.substring(0, 10)  // "yyyy/MM/dd"
    }

    // 日付降順に並べる
    val sortedDates = groupedByDate.keys.sortedDescending()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("購入履歴") },
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

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // 日付ごとのブロック
            items(sortedDates) { date ->

                val itemsInDate = groupedByDate[date] ?: emptyList()

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    // --------------------------------------------------------
                    // 日付ヘッダー
                    // --------------------------------------------------------
                    Text(
                        text = date,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    // 店舗ごとにグループ化
                    val groupedByStore = itemsInDate.groupBy { it.storeName }

                    groupedByStore.forEach { (storeName, storeHistoryList) ->

                        // --------------------------------------------------------
                        // 店舗名
                        // --------------------------------------------------------
                        Text(
                            text = "店舗：$storeName",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )

                        // 同じ店舗内の履歴は時間降順
                        val sortedStoreHistory =
                            storeHistoryList.sortedByDescending { it.dateTime }

                        sortedStoreHistory.forEach { historyItem ->

                            historyItem.items.forEach { product ->
                                HistoryItemRow(product)
                            }

                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// 商品1行（画像 + 商品名 + 数量 + 小計）
// =========================================================
@Composable
private fun HistoryItemRow(
    product: HistoryProduct
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // 画像
        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )

        // 商品情報
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text("数量：${product.quantity}")
            Text("単価：¥${product.price}")
        }

        // 小計
        Text(
            text = "¥${(product.quantity * product.price).toInt()}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
