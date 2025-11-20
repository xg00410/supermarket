// =========================================================
// File: RouteScreen.kt
// 設計書ID: route
// 画面名: 最短ルート画面
// 役割:
//   - 商品の配置順に応じた簡易的なルートを表示。
//   - 下部の横スクロールで「対象商品」を確認し、チェック済み商品を管理。
//   - 右下「終了」ボタンでチェック済みの注文を履歴へ登録。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.viewmodel.CartViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    val store = StoreDataRepository.getStoreById(storeId)
    val cartItems = cartViewModel.cartItems.filter { it.storeId == storeId }

    // checkbox 管理
    val checkedMap = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            cartItems.forEach { put(it.productId, false) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("最短ルート  (${store?.storeName ?: ""})") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        },
        floatingActionButton = {
            // ----------- 右下の 終了 ボタン -----------
            ExtendedFloatingActionButton(
                onClick = {
                    val checkedItems = cartItems.filter { checkedMap[it.productId] == true }
                    if (checkedItems.isNotEmpty()) {
                        // 履歴へ保存
                        cartViewModel.addHistoryEntry(
                            storeId = storeId,
                            storeName = store?.storeName ?: "",
                            items = checkedItems,
                            orderedAt = LocalDateTime.now()
                        )

                        // チェック済み商品をカートから削除
                        cartViewModel.removeCheckedItems(
                            checkedItems.map { it.productId }.toSet()
                        )
                    }

                    // 店舗選択画面へ戻る
                    navController.navigate("store_select") {
                        popUpTo("menu") { inclusive = false }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("終了")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ------------------- 簡易マップ（仮） -------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFEAEAEA), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("ここに簡易マップを表示（後で本番用に差し替え）")
            }

            // ------------------- 下部 横スクロールの商品一覧 -------------------
            Text("チェックする商品", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                cartItems.forEach { item ->

                    val isChecked = checkedMap[item.productId] ?: false

                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isChecked) Color(0xFFBBDEFB) else Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.name, maxLines = 1)

                            Text("数量：${item.quantity}")

                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    checkedMap[item.productId] = checked
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
