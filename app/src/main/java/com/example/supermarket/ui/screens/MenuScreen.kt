// =========================================================
// File: MenuScreen.kt
// 商品一覧画面（最終修正版 / 下部固定ボタン対応）
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    storeId: String,
    cartViewModel: CartViewModel
) {
    val allProducts = remember { StoreDataRepository.getProductsByStore(storeId) }

    // カテゴリ一覧
    val categories = listOf("飲料", "食品", "菓子", "日用品", "冷凍", "惣菜", "調味料", "その他")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    // 数量管理
    var quantityMap by remember { mutableStateOf(mutableMapOf<Int, Int>()) }

    // 合計金額
    val totalPrice by derivedStateOf {
        quantityMap.entries.sumOf { entry ->
            val p = allProducts.find { it.productId == entry.key }
            val qty = entry.value
            if (p != null) p.price * qty else 0.0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("商品一覧") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        // ★ Box で全体を包む（align が合法化される）
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // 上部（カテゴリ + 商品一覧）
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                // 左側カテゴリ
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFEFEFEF)),
                ) {
                    categories.forEach { category ->
                        val isSelected = category == selectedCategory
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .background(
                                    if (isSelected) Color(0xFFCCFF99)
                                    else Color.White
                                )
                                .clickable { selectedCategory = category }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(category)
                        }
                    }
                }

                // 右側商品一覧
                val filtered = allProducts.filter { it.category == selectedCategory }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtered) { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // 商品画像（仮）
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color.LightGray)
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(product.name, style = MaterialTheme.typography.titleMedium)
                                Text("価格：¥${product.price}")
                                Text("在庫：${product.stock}")
                            }

                            // 数量調整
                            val qty = quantityMap[product.productId] ?: 0
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(onClick = {
                                    if (qty > 0) {
                                        quantityMap = quantityMap.toMutableMap().apply {
                                            this[product.productId] = qty - 1
                                        }
                                    }
                                }) { Text("－") }

                                Text(qty.toString())

                                Button(onClick = {
                                    if (qty < product.stock) {
                                        quantityMap = quantityMap.toMutableMap().apply {
                                            this[product.productId] = qty + 1
                                        }
                                    }
                                }) { Text("＋") }
                            }
                        }
                        Divider()
                    }
                }
            }

            // ★ 下部固定（align がここなら合法）
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "合計：¥${totalPrice.toInt()}",
                    style = MaterialTheme.typography.titleMedium
                )

                // カートに入れる
                Button(
                    onClick = {
                        quantityMap.forEach { (productId, qty) ->
                            val p = allProducts.find { it.productId == productId }
                            if (p != null && qty > 0) {
                                cartViewModel.addToCart(p, qty)
                            }
                        }
                        quantityMap = mutableMapOf()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("カートに入れる")
                }

                // 最短ルートへ
                Button(
                    onClick = {
                        quantityMap.forEach { (productId, qty) ->
                            val p = allProducts.find { it.productId == productId }
                            if (p != null && qty > 0) {
                                cartViewModel.addToCart(p, qty)
                            }
                        }
                        quantityMap = mutableMapOf()
                        navController.navigate("route/$storeId")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("最短ルートへ")
                }
            }
        }
    }
}
