// =========================================================
// File: CartScreen.kt
// 設計書ID: cart
// 画面名: カート画面
// 役割:
//   - カート内の商品を一覧表示。
//   - 商品の数量調整（＋／－）。
//   - 合計金額を表示。
//   - 「最短ルートへ」で RouteScreen へ遷移。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.models.CartItem
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartItems = cartViewModel.cartItems

    // 合計金額
    val totalPrice by derivedStateOf {
        cartItems.sumOf { it.product.price * it.quantity }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カート") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ----------------------------------------------
            // カート一覧
            // ----------------------------------------------
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(cartItems) { item ->

                    CartItemRow(item, cartViewModel)
                    Divider()
                }
            }

            // ----------------------------------------------
            // 下部：合計 ＋ 最短ルート
            // ----------------------------------------------
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "合計：¥${totalPrice.toInt()}",
                    style = MaterialTheme.typography.titleMedium
                )

                // 最短ルートへ（storeName から storeId を逆引きしない）
                // → storeId は RouteScreen の仕様通り、MenuScreen から渡された storeId を保持している
                //   カートの全商品が同じ店のものなので、1つ目の商品から storeName を取得できる
                val storeId: String? =
                    if (cartItems.isNotEmpty()) {
                        // StoreDataRepository で名前→ID の逆引きが必要
                        com.example.supermarket.data.StoreDataRepository
                            .getAllStores()
                            .find { it.storeName == cartItems.first().product.storeName }
                            ?.storeId
                    } else null

                Button(
                    onClick = {
                        if (storeId != null) {
                            navController.navigate("${Routes.ROUTE}/${storeId}")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("最短ルートへ")
                }
            }
        }
    }
}

// ------------------------------------------------------------
// 商品1行（数量調整）
// ------------------------------------------------------------
@Composable
private fun CartItemRow(
    item: CartItem,
    cartViewModel: CartViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // 商品画像
        Image(
            painter = painterResource(id = item.product.imageRes),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )

        // 商品情報
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(item.product.name, style = MaterialTheme.typography.titleMedium)
            Text("価格：¥${item.product.price}")
            Text("在庫：${item.product.stock}")
        }

        // 数量調整
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { cartViewModel.decrease(item.product) }) {
                Text("－")
            }

            Text("${item.quantity}")

            Button(onClick = { cartViewModel.increase(item.product) }) {
                Text("＋")
            }
        }
    }
}
