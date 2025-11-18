// =========================================================
// File: RouteScreen.kt
// 設計書ID: route
// 画面名: 最短ルート画面
// 役割:
//   - カート内の商品をもとに、店舗内の巡回順（ルート）を確認する画面。
//   - 上部: 店舗内マップ（簡易図）＋エリアのハイライト（A〜Fなど）
//   - 中部: エリアスライダー（エリア順の並べ替え）
//   - 下部: 商品一覧（エリアごとにグルーピング）＋「取得」チェックボックス
//   - 戻る時: チェックONの商品を「取得済み」とみなし、カートから削除し履歴へ登録。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.RouteRepository
import com.example.supermarket.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    // 対象店舗のカート内商品
    val cartItems = cartViewModel.cartItems.filter { it.storeId == storeId }

    // 使用されているエリアID一覧（A, B, C, ...）
    val areaIds = remember(cartItems) {
        cartItems.map { it.category } // ※カテゴリーではなく本来は area だが、簡易実装として category を流用する場合はここを調整
    }

    // ルート用エリア設定
    val initialAreas = remember(areaIds) {
        RouteRepository.buildInitialAreaOrder(areaIds.toSet())
    }

    var areaOrder by remember { mutableStateOf(initialAreas) }

    // 商品取得チェック状態（productId 単位）
    val obtainedMap = remember { mutableStateMapOf<Int, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ルート案内") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // 戻るボタンでもナビ終了扱いとする
                            finishRouteAndBack(navController, cartViewModel, obtainedMap)
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        if (cartItems.isEmpty()) {
            // 対象店舗のカートが空の場合
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("この店舗のカートに商品がありません。")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // -------------------------------------------------
            // ① 店内マップ（簡易キャンバス）
            // -------------------------------------------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // エリアを順番に線で結ぶ
                    for (i in 0 until areaOrder.size - 1) {
                        val a = areaOrder[i]
                        val b = areaOrder[i + 1]
                        val start = Offset(a.centerX * w, a.centerY * h)
                        val end = Offset(b.centerX * w, b.centerY * h)
                        drawLine(
                            color = androidx.compose.ui.graphics.Color.DarkGray,
                            start = start,
                            end = end,
                            strokeWidth = 6f
                        )
                    }

                    // 各エリアを丸＋ラベルで表示
                    areaOrder.forEach { area ->
                        val center = Offset(area.centerX * w, area.centerY * h)
                        drawCircle(
                            color = androidx.compose.ui.graphics.Color.Black,
                            radius = 14f,
                            center = center
                        )
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                area.id,
                                center.x,
                                center.y - 20f,
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.BLACK
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    textSize = 32f
                                }
                            )
                        }
                    }
                }
            }

            // -------------------------------------------------
            // ② エリアスライダー（エリア順の並べ替え）
            // -------------------------------------------------
            Text(
                text = "エリア順（上のマップと連動）",
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(areaOrder.size) { index ->
                    val area = areaOrder[index]
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (index > 0) {
                                    val list = areaOrder.toMutableList()
                                    list.removeAt(index)
                                    list.add(index - 1, area)
                                    areaOrder = list
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.KeyboardArrowLeft,
                                contentDescription = "左へ"
                            )
                        }

                        AssistChip(
                            onClick = { /* クリックでは何もしない */ },
                            label = { Text(area.id) }
                        )

                        IconButton(
                            onClick = {
                                if (index < areaOrder.size - 1) {
                                    val list = areaOrder.toMutableList()
                                    list.removeAt(index)
                                    list.add(index + 1, area)
                                    areaOrder = list
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                contentDescription = "右へ"
                            )
                        }
                    }
                }
            }

            // -------------------------------------------------
            // ③ 商品一覧（エリア → 商品）
            // -------------------------------------------------
            Text(
                text = "商品一覧（取得したものにチェック）",
                style = MaterialTheme.typography.titleMedium
            )

            // 簡易的にカテゴリ単位でグループ化（本来は area でグループ化）
            val grouped = cartItems.groupBy { it.category }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                grouped.forEach { (groupKey, itemsInGroup) ->
                    item(key = "header_$groupKey") {
                        Text(
                            text = "エリア：$groupKey",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    items(itemsInGroup, key = { it.productId }) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = obtainedMap[item.productId] ?: false,
                                onCheckedChange = { checked ->
                                    obtainedMap[item.productId] = checked
                                }
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(item.name, style = MaterialTheme.typography.bodyLarge)
                                Text("数量：${item.quantity}")
                                Text("価格：${item.price} 円")
                            }
                        }
                    }
                }
            }

            // -------------------------------------------------
            // ④ ナビ終了ボタン
            // -------------------------------------------------
            Button(
                onClick = {
                    finishRouteAndBack(navController, cartViewModel, obtainedMap)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ナビ終了（取得済みを履歴に登録）")
            }
        }
    }
}

/**
 * ルート終了時の処理:
 *  - チェックONの商品IDを取得
 *  - カートから削除（consumeItems）
 *  - 取得済み商品で履歴を1件登録（registerOrder）
 *  - カート画面に戻る
 */
private fun finishRouteAndBack(
    navController: NavController,
    cartViewModel: CartViewModel,
    obtainedMap: Map<Int, Boolean>
) {
    val obtainedIds = obtainedMap.filterValues { it }.keys.toList()
    val obtainedItems = cartViewModel.consumeItems(obtainedIds)
    cartViewModel.registerOrder(obtainedItems)

    // カート画面へ戻る
    navController.popBackStack()
}
