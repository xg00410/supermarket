package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.toProduct   // ✅ 确保导入扩展函数

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: androidx.navigation.NavController,
    onBack: () -> Unit,
    onGoRoute: () -> Unit,
    cartViewModel: CartViewModel
) {
    val cartItems = cartViewModel.cartItems

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カート") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("戻る") }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onGoRoute,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("最短ルートへ")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            items(cartItems) { item ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("数量: ${item.quantity}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick = { cartViewModel.decreaseItem(item.productId) }) {
                                Text("-")
                            }
                            Button(onClick = { cartViewModel.addToCart(item.toProduct()) }) {
                                Text("+")
                            }
                            OutlinedButton(onClick = { cartViewModel.removeItem(item.productId) }) {
                                Text("削除")
                            }
                        }
                    }
                }
            }
        }
    }
}
