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
import androidx.compose.foundation.border
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
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.supermarket.R


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

            // ------------------- 店内マップ表示 -------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (store != null) {
                    Image(
                        painter = painterResource(id = store?.floorMapRes ?: R.drawable.store_floor_map),
                        contentDescription = "店内マップ",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFEAEAEA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("店舗情報が見つかりません")
                    }
                }
            }

            // ------------------- エリア順序（手動並び替え） -------------------
            val usedAreas = remember(cartItems) {
                cartItems.map { it.category }.distinct()
            }
            val areasOrder = remember(usedAreas) {
                mutableStateListOf<String>().apply {
                    clear()
                    addAll(usedAreas)
                }
            }

            if (areasOrder.isNotEmpty()) {
                Text("エリア順序", style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    areasOrder.forEachIndexed { index, area ->

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {

                            // 左矢印：前の位置へ移動（小さめ）
                            IconButton(
                                onClick = {
                                    if (index > 0) {
                                        val tmp = areasOrder[index]
                                        areasOrder[index] = areasOrder[index - 1]
                                        areasOrder[index - 1] = tmp
                                    }
                                },
                                enabled = index > 0,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "左へ移動"
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = area,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            // 右矢印：後ろの位置へ移動（小さめ）
                            IconButton(
                                onClick = {
                                    if (index < areasOrder.lastIndex) {
                                        val tmp = areasOrder[index]
                                        areasOrder[index] = areasOrder[index + 1]
                                        areasOrder[index + 1] = tmp
                                    }
                                },
                                enabled = index < areasOrder.lastIndex,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "右へ移動"
                                )
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))
            }

// エリア順に並べ替えた商品リスト
            val sortedItems = if (areasOrder.isEmpty()) {
                cartItems
            } else {
                cartItems.sortedWith(
                    compareBy(
                        { item ->
                            val idx = areasOrder.indexOf(item.category)
                            if (idx == -1) Int.MAX_VALUE else idx
                        },
                        { it.productId }
                    )
                )
            }

            // ------------------- 下部 横スクロールの商品一覧 -------------------
            Text("チェックする商品", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                sortedItems.forEach { item ->

                    val isChecked = checkedMap[item.productId] ?: false

                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(100.dp)
                            .background(
                                color = if (isChecked) Color(0xFFBBDEFB) else Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFFDDDDDD),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )

                            Text(
                                text = "数量：${item.quantity}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        checkedMap[item.productId] = checked
                                    },
                                    modifier = Modifier.size(18.dp) // 小さめのチェックボックス
                                )
                            }
                        }
                    }


                }
            }
        }
    }
}
