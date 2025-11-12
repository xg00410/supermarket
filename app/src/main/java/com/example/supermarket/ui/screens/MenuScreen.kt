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
import com.example.supermarket.data.FakeRepository
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.models.Product
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.MainScaffold
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    storeId: String,
    onGoCart: () -> Unit = { navController.navigate(Routes.CART) },
    onBack: () -> Unit = { navController.popBackStack() },
    cartViewModel: CartViewModel
) {
    val products = remember { FakeRepository.getProductsByStore(storeId) }

    MainScaffold(navController = navController, title = "商品一覧") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (products.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("この店舗には商品がありません。")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(products) { product ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(product.name, style = MaterialTheme.typography.titleMedium)
                                Text("価格: ${product.price}円")
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { cartViewModel.addToCart(product) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("カートに追加")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onGoCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("カートを見る")
            }
        }
    }
}
