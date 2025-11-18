// =========================================================
// File: CartScreen.kt
// 設計書ID: list
// 画面名: リスト画面①（カート）
// 役割:
//   - カート内商品の数量変更（＋／－）。
//   - 削除ボタン（ゴミ箱）。
//   - 合計金額の表示。
//   - 「最短ルートへ」ボタンから route 画面へ遷移。
//   - 戻るボタンで店舗商品一覧へ戻る。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes
import com.example.supermarket.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartItems = cartViewModel.cartItems
    val totalPrice by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(0.0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カート") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    // 管理画面（list2）への遷移ボタン
                    TextButton(onClick = { navController.navigate(Routes.CART_MANAGE) }) {
                        Text("管理")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("カートは空です。", style = MaterialTheme.typography.bodyLarge)
                }
            } else {

                // 商品一覧
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems, key = { it.productId }) { item ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {

                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text("数量：${item.quantity}")
                                Text("価格：${item.price} 円")

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                cartViewModel.increaseQuantity(item.productId)
                                            }
                                        ) {
                                            Text("+")
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                cartViewModel.decreaseQuantity(item.productId)
                                            }
                                        ) {
                                            Text("-")
                                        }
                                    }

                                    IconButton(
                                        onClick = {
                                            cartViewModel.removeItem(item.productId)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "削除"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 合計金額
            val sum = cartViewModel.totalPrice()
            Text(
                text = "合計：${sum.toInt()} 円",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 最短ルートへ
            val canNavigateRoute = cartItems.isNotEmpty()
            Button(
                onClick = {
                    if (canNavigateRoute) {
                        val storeId = cartItems.first().storeId
                        navController.navigate("${Routes.ROUTE}/$storeId")
                    }
                },
                enabled = canNavigateRoute,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("最短ルートへ")
            }
        }
    }
}
