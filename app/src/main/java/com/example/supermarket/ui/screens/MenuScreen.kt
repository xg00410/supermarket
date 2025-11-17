package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.viewmodel.CartViewModel
import androidx.compose.ui.Alignment

/**
 * MenuScreen
 * 🇯🇵 商品一覧（店舗内の商品選択画面）
 * 🇨🇳 店铺商品菜单（商品选择）
 *
 * - 店名を表示する
 * - 店舗IDに紐づく商品を取得する
 * - 追加ボタン → カートへ追加
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    storeId: String,
    onGoCart: () -> Unit,
    onBack: () -> Unit,
    cartViewModel: CartViewModel
) {
    val store = StoreDataRepository.getStoreById(storeId)
    val products = StoreDataRepository.getProductsByStore(storeId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(store?.name ?: "商品一覧") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    IconButton(onClick = onGoCart) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "cart")
                    }
                }
            )
        }
    ) { padding ->

        if (store == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("店舗情報が見つかりません。")
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(products.size) { index ->
                val item = products[index]

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("カテゴリー：${item.category ?: "不明"}")
                        Text("価格：${item.price} 円")
                        Text("在庫：${item.stock}")

                        Button(
                            onClick = {
                                cartViewModel.addToCart(item)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("カートに追加")
                        }
                    }
                }
            }
        }
    }
}
