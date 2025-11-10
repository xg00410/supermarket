package com.example.supermarket.ui.screens
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.components.BottomNavBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    onBack: () -> Unit,
    onGoRoute: () -> Unit,
    cartViewModel: CartViewModel = viewModel()
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
        },bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("カートは空です")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { item ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = 2.dp,
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(item.productName)
                                    Text("数量: ${item.quantity}")
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(onClick = {
                                        cartViewModel.decreaseItem(item.productId)
                                    }) { Text("-") }

                                    Button(onClick = {
                                        cartViewModel.addToCart(
                                            product = com.example.supermarket.model.Product(
                                                id = item.productId,
                                                storeId = "",
                                                name = item.productName,
                                                category = "",
                                                priceYen = 0,
                                                stock = 0,
                                                imageUrl = null
                                            )
                                        )
                                    }) { Text("+") }

                                    IconButton(onClick = {
                                        cartViewModel.removeItem(item.productId)
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "削除"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 下部按钮行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { cartViewModel.clearCart() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("全削除")
                    }
                    Button(
                        onClick = onGoRoute,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("ルートへ")
                    }
                }
            }
        }
    }
}
