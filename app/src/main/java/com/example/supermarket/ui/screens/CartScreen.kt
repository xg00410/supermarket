// =========================================================
// ファイル名: CartScreen.kt
// 設計書ID: list
// 画面名: リスト画面①（カート）
// 役割:
//   - カート内商品の数量変更（＋／－）
//   - 削除ボタン（ゴミ箱）
//   - 合計金額の表示
//   - 「最短ルートへ」遷移ボタン
//   - 「戻る」ボタンで店舗画面へ戻る
//
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.models.toProduct
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {

    val cartItems = cartViewModel.cartItems

    // 合計金額
    val totalPrice = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("カート") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(Routes.MENU) }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
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
                Spacer(modifier = Modifier.height(20.dp))
                Text("カートは空です。", style = MaterialTheme.typography.bodyLarge)
            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems.size) { index ->
                        val item = cartItems[index]

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                                Text("価格：${item.price} 円")
                                Text("数量：${item.quantity}")

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // +（数量増）
                                    Button(
                                        onClick = {
                                            cartViewModel.addToCart(item.toProduct())
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("+")
                                    }

                                    // -（数量減）
                                    OutlinedButton(
                                        onClick = {
                                            cartViewModel.decreaseItem(item.productId)
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("-")
                                    }

                                    // 削除
                                    IconButton(
                                        onClick = {
                                            cartViewModel.removeItem(item.productId)
                                        }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "delete")
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ------------------------------
                // 合計金額表示（設計書で必須）
                // ------------------------------
                Text(
                    text = "合計：${totalPrice} 円",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ------------------------------
                // 最短ルートへ（→ route）
                // ------------------------------
                Button(
                    onClick = { navController.navigate(Routes.ROUTE) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("最短ルートへ")
                }
            }
        }
    }
}
