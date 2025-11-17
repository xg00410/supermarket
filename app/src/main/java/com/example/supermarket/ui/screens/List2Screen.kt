// =========================================================
// File: List2Screen.kt
// 設計書ID: list2
// 画面名: リスト画面②（カート管理）
// 役割:
//   - 店舗単位でカート内の商品をグループ化して表示
//   - 店舗チェック → 店舗内の全商品チェック
//   - 商品チェック → 店舗チェックの自動ON/OFF
//   - すべて選択チェック
//   - 選択された商品の一括削除
//   - 商品数量の増減（＋／－）
//   - 店舗内の商品が0件になると店舗ブロックを非表示
//
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.ui.Routes
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun List2Screen(
    navController: NavController,
    cartViewModel: CartViewModel
) {

    // カート全体
    val cartItems = cartViewModel.cartItems

    // 店舗ごとにグループ化
    val grouped = cartItems.groupBy { it.storeId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("カート管理") }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.STORE_SELECT) },
                    label = { Text("店舗選択") },
                    icon = { /*icon省略*/ }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.STORE) },
                    label = { Text("店舗") },
                    icon = { }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate(Routes.LIST2) },
                    label = { Text("カート") },
                    icon = { }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.PROFILE) },
                    label = { Text("マイページ") },
                    icon = { }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            // ---------------------------------------------------------
            // すべて選択チェック（設計書ID: 全選択）
            // ---------------------------------------------------------
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Checkbox(
                    checked = cartViewModel.isSelectAll,
                    onCheckedChange = { cartViewModel.selectAll() }
                )
                Text("すべて選択（全店舗・全商品）")
            }

            // ---------------------------------------------------------
            // 店舗ごとの商品一覧表示
            // ---------------------------------------------------------
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.weight(1f)
            ) {

                items(grouped.keys.toList()) { storeId ->

                    val storeItems = grouped[storeId] ?: emptyList()
                    if (storeItems.isEmpty()) return@items

                    val storeName = storeItems.first().storeName
                    val storeChecked = cartViewModel.selectedStoreIds[storeId] ?: false

                    // -----------------------------
                    // 店舗ブロック
                    // -----------------------------
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        // 店舗ヘッダー（チェック + 店舗名）
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = storeChecked,
                                onCheckedChange = { cartViewModel.toggleStoreChecked(storeId) }
                            )
                            Text(storeName, style = MaterialTheme.typography.titleMedium)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // -----------------------------
                        // 店舗内の商品一覧
                        // -----------------------------
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            storeItems.forEach { item ->
                                ProductManageCard(
                                    item = item,
                                    cartViewModel = cartViewModel
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ---------------------------------------------------------
            // 削除ボタン（選択された商品を一括削除）
            // ---------------------------------------------------------
            Button(
                onClick = { cartViewModel.deleteSelectedItems() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("選択した商品を削除する")
            }
        }
    }
}

@Composable
private fun ProductManageCard(
    item: CartItem,
    cartViewModel: CartViewModel
) {
    val checked = cartViewModel.selectedItemIds[item.productId] ?: false

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            // 商品行1
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { cartViewModel.toggleItemChecked(item.productId) }
                )

                Text(item.name, style = MaterialTheme.typography.titleMedium)
            }

            // 商品画像
            item.imageRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text("価格：${item.price} 円")

            // 数量 + / -
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        cartViewModel.decreaseQuantity(item.productId)

                    }
                ) { Text("－") }

                Text("${item.quantity} 個")

                OutlinedButton(
                    onClick = {
                        cartViewModel.increaseQuantity(item.productId)

                    }
                ) { Text("＋") }
            }

            // 削除
            IconButton(
                onClick = { cartViewModel.removeItem(item.productId) }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "delete")
            }
        }
    }
}
