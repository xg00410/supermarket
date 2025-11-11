package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.components.MainScaffold
import com.example.supermarket.ui.Routes
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.toProduct


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartItems = cartViewModel.cartItems

    MainScaffold(navController = navController, title = "カート") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (cartItems.isEmpty()) {
                Text("カートが空です。")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(cartItems) { item ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.productName)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("数量: ${item.quantity}")
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Button(onClick = { cartViewModel.decreaseItem(item.productId) }) { Text("-") }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = { cartViewModel.addToCart(item.toProduct()) }) { Text("+") }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { navController.navigate(Routes.ROUTE) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("最短ルートを確認する")
                }
            }
        }
    }
}
