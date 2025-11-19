// =========================================================
// File: RouteScreen.kt
// 設計書ID: route
// 画面名: 最短ルート画面
// 役割:
//   - 店舗内の簡易マップ（擬似マップ）とルート表示。
//   - 画面上部に店舗名＋店舗ID を表示して、どの店舗か分かるようにする。
//   - 下部に「エリア（カテゴリ）スライダー」「商品スライダー」を横スクロールで表示。
//   - 各商品カードには 商品名・数量・チェックボックス を縦方向に配置し，
//     カード自体を横方向にスクロールして確認できるようにする。
// 備考:
//   - マップは Canvas を使ったシンプルな長方形＋ルート線の擬似図。
//   - 実際の店内画像に差し替える場合は、このファイル内の Canvas 部分を修正する。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    // 店舗情報（タイトル表示用）
    val store = remember(storeId) {
        StoreDataRepository.getStoreById(storeId)
    }

    // この店舗のカート内商品
    val cartItemsForStore = cartViewModel.cartItems.filter { it.storeId == storeId }

    // この店舗の商品マスタ（カテゴリを引くために使用）
    val productsForStore = remember(storeId) {
        StoreDataRepository.getProductsByStore(storeId)
    }

    // カート内に実際に存在するカテゴリ一覧（重複除去）
    val categoriesInCart: List<String> = remember(cartItemsForStore) {
        cartItemsForStore.mapNotNull { item ->
            productsForStore.find { it.productId == item.productId }?.category
        }.distinct()
    }

    var selectedCategory by remember(categoriesInCart) {
        mutableStateOf(categoriesInCart.firstOrNull())
    }

    // 選択中カテゴリに属する「カート内の商品＋商品マスタ」のペア
    val itemsForSelectedCategory = remember(selectedCategory, cartItemsForStore, productsForStore) {
        if (selectedCategory == null) emptyList()
        else {
            cartItemsForStore.mapNotNull { item ->
                val product = productsForStore.find { it.productId == item.productId }
                if (product != null && product.category == selectedCategory) {
                    item to product
                } else null
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = store?.let { "${it.storeName}（$storeId）" } ?: "ルート案内"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            // 上：店舗内マップ（擬似）
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // 店内の長方形（通路）
                        drawRect(
                            color = Color(0xFFE0E0E0),
                            topLeft = Offset(w * 0.05f, h * 0.05f),
                            size = androidx.compose.ui.geometry.Size(w * 0.9f, h * 0.9f)
                        )

                        // 簡易的な棚（縦長の細い長方形をいくつか）
                        for (i in 0..3) {
                            val left = w * (0.12f + i * 0.2f)
                            drawRect(
                                color = Color(0xFFBDBDBD),
                                topLeft = Offset(left, h * 0.1f),
                                size = androidx.compose.ui.geometry.Size(w * 0.04f, h * 0.8f)
                            )
                        }

                        // ルート線（スタート → ゴールのイメージ）
                        val start = Offset(w * 0.1f, h * 0.9f)
                        val mid = Offset(w * 0.5f, h * 0.5f)
                        val end = Offset(w * 0.85f, h * 0.15f)

                        drawLine(
                            color = Color(0xFF1976D2),
                            start = start,
                            end = mid,
                            strokeWidth = 8f
                        )
                        drawLine(
                            color = Color(0xFF1976D2),
                            start = mid,
                            end = end,
                            strokeWidth = 8f
                        )

                        // 現在地（青い丸）
                        drawCircle(
                            color = Color(0xFF0D47A1),
                            radius = 18f,
                            center = start
                        )

                        // ゴール（赤い丸）
                        drawCircle(
                            color = Color(0xFFD32F2F),
                            radius = 18f,
                            center = end
                        )
                    }
                }
            }

            // 下：エリア＆商品スライダー
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {

                // エリア（カテゴリ）スライダー：カートに存在するカテゴリだけ表示（A2）
                if (categoriesInCart.isNotEmpty()) {
                    Text(
                        text = "エリア（カテゴリ）",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categoriesInCart) { category ->
                            val selected = category == selectedCategory

                            FilterChip(
                                selected = selected,
                                onClick = { selectedCategory = category },
                                label = { Text(category) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 商品スライダー（横スクロール，立て長カード）
                Text(
                    text = "商品",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(4.dp))

                if (itemsForSelectedCategory.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("該当する商品がありません。")
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(itemsForSelectedCategory, key = { it.first.productId }) { pair ->
                            val cartItem = pair.first
                            val product = pair.second

                            // 各商品のチェック状態（画面内のみ保持）
                            var checked by remember(cartItem.productId) { mutableStateOf(false) }

                            Card(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(140.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = product.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 2
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "数量：${cartItem.quantity}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "取得済み",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Checkbox(
                                            checked = checked,
                                            onCheckedChange = { checked = it }
                                        )
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
