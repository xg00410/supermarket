// =========================================================
// File: CartScreen.kt
// 設計書ID: list
// 画面名: リスト画面①（カート）
// 役割:
//   - カート内商品の数量変更（＋／－）。
//   - 削除ボタン（ゴミ箱）。
//   - 合計金額の表示。
//   - 店舗ごとに商品をグループ化して表示。
//   - 各商品の在庫数を表示（ゴミ箱ボタンの左側）。
//   - 各店舗ブロックの下に「最短ルートへ」ボタンを配置し，
//     その店舗のルート画面へ直接遷移する。
//   - 画面下部の「どの店舗か分からない」全体用ルートボタンは削除。
//   - ★在庫数を超えている場合，「最短ルートへ」押下時に確認ダイアログを表示。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes
import com.example.supermarket.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {

    val cartItems = cartViewModel.cartItems

    // ★在庫超過チェック用の状態（ダイアログ）
    val overStockMessageState = remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    val pendingStoreIdState = remember { androidx.compose.runtime.mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カート") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // 戻る → 店舗選択画面へ
                            navController.navigate(Routes.STORE_SELECT) {
                                popUpTo(Routes.STORE_SELECT) { inclusive = false }
                            }
                        }
                    ) {
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

                // 店舗ごとにグループ化
                val groupedByStore = cartItems.groupBy { it.storeId to it.storeName }

                // 商品一覧
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    groupedByStore.forEach { (storeKey, itemsInStore) ->
                        val storeId = storeKey.first
                        val storeName = storeKey.second

                        // 店舗名ヘッダー
                        item(key = "header_${storeId}") {
                            Text(
                                text = storeName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        // 店舗ごとの商品行
                        items(itemsInStore, key = { it.productId }) { item ->

                            // 在庫数を Product から取得
                            val stock = remember(item.storeId, item.productId) {
                                StoreDataRepository
                                    .getProductsByStore(item.storeId)
                                    .find { it.productId == item.productId }
                                    ?.stock ?: 0
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 左：画像（商品に画像があればそれを使用，なければロゴ）
                                    Image(
                                        painter = painterResource(
                                            id = item.imageRes ?: R.drawable.logo
                                        ),
                                        contentDescription = "商品画像",
                                        modifier = Modifier
                                            .size(64.dp)
                                            .padding(end = 8.dp),
                                        contentScale = ContentScale.Crop
                                    )

                                    // 中央：商品情報
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 8.dp),
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(
                                            text = item.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text("単価：${item.price.toInt()} 円")
                                        Text("小計：${(item.price * item.quantity).toInt()} 円")
                                    }

                                    // 右：数量コントロール ＋ 在庫 ＋ 削除ボタン
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // 数量（－ 数量 ＋）
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = {
                                                    cartViewModel.decreaseQuantity(item.productId)
                                                },
                                                contentPadding = PaddingValues(horizontal = 8.dp)
                                            ) {
                                                Text("－")
                                            }

                                            Text(
                                                text = item.quantity.toString(),
                                                style = MaterialTheme.typography.bodyLarge
                                            )

                                            OutlinedButton(
                                                onClick = {
                                                    cartViewModel.increaseQuantity(item.productId)
                                                },
                                                contentPadding = PaddingValues(horizontal = 8.dp)
                                            ) {
                                                Text("＋")
                                            }
                                        }

                                        // 在庫表示＋削除ボタン（在庫が左、ゴミ箱が右）
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text("在庫：$stock")

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

                        // 店舗ごとの「最短ルートへ」ボタン
                        item(key = "route_${storeId}") {
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(
                                onClick = {
                                    // ★この店舗内で在庫超過している商品があるかどうかチェック
                                    val over = itemsInStore.mapNotNull { cartItem ->
                                        val product = StoreDataRepository
                                            .getProductsByStore(cartItem.storeId)
                                            .find { it.productId == cartItem.productId }
                                        if (product != null && cartItem.quantity > product.stock) {
                                            product to cartItem.quantity
                                        } else {
                                            null
                                        }
                                    }

                                    if (over.isNotEmpty()) {
                                        val (product, qty) = over.first()
                                        overStockMessageState.value =
                                            "選択された数量が在庫数を超えている商品があります。\n" +
                                                    "例：${product.name} 在庫：${product.stock} / カート数量：$qty\n\n" +
                                                    "このまま続行しますか？"
                                        pendingStoreIdState.value = storeId
                                    } else {
                                        // 在庫超過なし → そのままルート画面へ
                                        navController.navigate("${Routes.ROUTE}/$storeId")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("最短ルートへ")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 合計金額（全店舗合計）
            val sum = cartViewModel.totalPrice()
            Text(
                text = "合計：${sum.toInt()} 円",
                style = MaterialTheme.typography.titleMedium
            )

            // ★在庫超過確認ダイアログ
            overStockMessageState.value?.let { msg ->
                AlertDialog(
                    onDismissRequest = {
                        overStockMessageState.value = null
                        pendingStoreIdState.value = null
                    },
                    title = { Text("在庫数を超えています") },
                    text = { Text(msg) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val target = pendingStoreIdState.value
                                if (target != null) {
                                    navController.navigate("${Routes.ROUTE}/$target")
                                }
                                overStockMessageState.value = null
                                pendingStoreIdState.value = null
                            }
                        ) {
                            Text("はい")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                overStockMessageState.value = null
                                pendingStoreIdState.value = null
                            }
                        ) {
                            Text("いいえ")
                        }
                    }
                )
            }
        }
    }
}
