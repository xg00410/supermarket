package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.FakeRepository
import com.example.supermarket.models.Product   // ✅ 确认是 models（有 s）
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.components.MainScaffold
import com.example.supermarket.ui.Routes

/**
 * 🍱 MenuScreen.kt
 * -------------------------------------------------------------
 * 📘 商品一覧画面 / 商品菜单界面
 * -------------------------------------------------------------
 * 🇯🇵 店舗ごとの商品一覧を表示し、カートに追加できる画面。
 * 🇨🇳 显示各店铺商品列表，可将商品加入购物车。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    storeId: String,
    cartViewModel: CartViewModel
) {
    // 🛍️ 获取该店铺的商品
    val products = remember { FakeRepository.getProductsByStore(storeId) }

    MainScaffold(navController = navController, title = "商品一覧") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (products.isEmpty()) {
                // ⚠️ 没有商品时的提示
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("この店舗には商品が登録されていません。")
                }
            } else {
                // ✅ 商品列表
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = { cartViewModel.addToCart(product) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🛒 跳转购物车
            Button(
                onClick = { navController.navigate(Routes.CART) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("カートを見る")
            }
        }
    }
}

/**
 * 🏷️ 商品卡片组件 / 商品カードコンポーネント
 */
@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text("価格: ${product.price}円", style = MaterialTheme.typography.bodyMedium)
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("カートに追加")
            }
        }
    }
}
