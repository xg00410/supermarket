package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.models.toProduct

/**
 * CartScreen
 * 🇯🇵 カート画面
 * 🇨🇳 购物车画面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onGoRoute: () -> Unit
) {
    // ❗ 正确：cartItems 是普通 List<CartItem>
    val cartItems = cartViewModel.cartItems

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("カート") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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

            // カート空检查 / 购物车为空
            if (cartItems.isEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("カートは空です。", style = MaterialTheme.typography.bodyLarge)
            } else {

                // 商品列表
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

                                // 商品名・価格・数量
                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                                Text("価格：${item.price} 円")
                                Text("数量：${item.quantity}")

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // 数量 +1
                                    Button(
                                        onClick = { cartViewModel.addToCart(item.toProduct()) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("+")
                                    }

                                    // 数量 -1
                                    OutlinedButton(
                                        onClick = { cartViewModel.decreaseItem(item.productId) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("-")
                                    }

                                    // 删除该商品
                                    IconButton(
                                        onClick = { cartViewModel.removeItem(item.productId) }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "delete")
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onGoRoute,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("最短ルートへ")
                }
            }
        }
    }
}
