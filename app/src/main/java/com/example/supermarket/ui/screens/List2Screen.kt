// =========================================================
// File: List2Screen.kt
// 設計書ID: list2
// 画面名: リスト画面②（カート管理）
// 役割:
//   - 店舗単位でカート内の商品をグループ化して表示。
//   - 店舗チェック → 店舗内の全商品チェック。
//   - 商品チェック → 店舗チェック状態の自動更新。
//   - 「すべて選択」チェック。
//   - 選択された商品の一括削除。
//   - 各商品の数量増減（＋／－）。
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
import com.example.supermarket.models.CartItem
import com.example.supermarket.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun List2Screen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartItems = cartViewModel.cartItems
    val selectedItemIds = cartViewModel.selectedItemIds
    val selectedStoreIds = cartViewModel.selectedStoreIds
    val isSelectAll = cartViewModel.isSelectAll

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カート管理") },
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
                    Text("カートに商品がありません。")
                }
                return@Column
            }

            // 上部: すべて選択 ＋ 一括削除ボタン
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isSelectAll,
                        onCheckedChange = { cartViewModel.selectAll() }
                    )
                    Text("すべて選択")
                }

                val hasSelected = selectedItemIds.values.any { it }
                OutlinedButton(
                    onClick = { cartViewModel.deleteSelectedItems() },
                    enabled = hasSelected
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "選択削除")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("選択削除")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 店舗ごとにグルーピング
            val groupedByStore = cartItems.groupBy { it.storeId }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                groupedByStore.forEach { (storeId, itemsInStore) ->

                    item(key = "store_$storeId") {

                        val storeName = itemsInStore.firstOrNull()?.storeName ?: "店舗"

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                // 店舗ヘッダ（店舗チェック）
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val storeChecked by androidx.compose.runtime.remember {
                                            androidx.compose.runtime.mutableStateOf(
                                                selectedStoreIds[storeId] ?: false
                                            )
                                        }
                                        Checkbox(
                                            checked = selectedStoreIds[storeId] == true,
                                            onCheckedChange = {
                                                cartViewModel.toggleStoreChecked(storeId)
                                            }
                                        )
                                        Text(storeName)
                                    }
                                }

                                Divider()

                                // 店舗内の商品一覧
                                itemsInStore.forEach { item ->
                                    ProductManageRow(
                                        cartItem = item,
                                        isChecked = selectedItemIds[item.productId] == true,
                                        onToggleChecked = {
                                            cartViewModel.toggleItemChecked(item.productId)
                                        },
                                        onIncrease = {
                                            cartViewModel.increaseQuantity(item.productId)
                                        },
                                        onDecrease = {
                                            cartViewModel.decreaseQuantity(item.productId)
                                        },
                                        onDelete = {
                                            cartViewModel.removeItem(item.productId)
                                        }
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

/**
 * カート管理用の商品1行表示。
 */
@Composable
private fun ProductManageRow(
    cartItem: CartItem,
    isChecked: Boolean,
    onToggleChecked: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = isChecked,
            onCheckedChange = { onToggleChecked() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = cartItem.name)
            Text(text = "数量：${cartItem.quantity}")
            Text(text = "小計：${(cartItem.price * cartItem.quantity).toInt()} 円")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onIncrease) {
                Text("+")
            }
            OutlinedButton(onClick = onDecrease) {
                Text("-")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "削除")
            }
        }
    }
}
