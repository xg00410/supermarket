// =========================================================
// File: MenuScreen.kt
// 画面名: 店舗画面（商品一覧）
// 役割:
//   - 店舗名を画面上部に表示
//   - 左側にカテゴリ一覧、右側にカテゴリ別商品一覧
//   - 「カートを見る」ボタン
//   - 店舗ごとのカート合計を表示
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.data.SelectedStoreState
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.ProductCard
import com.example.supermarket.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    // ★ 表示中の店舗を記録（BottomNav 用）
    LaunchedEffect(storeId) {
        SelectedStoreState.currentStoreId = storeId
    }

    // ★ 店舗名を取得（StoreDataRepository から）
    val store = remember(storeId) {
        StoreDataRepository.getStoreById(storeId)
    }
    val storeName = store?.storeName ?: "店舗"

    // 8カテゴリ
    val categories = listOf(
        "飲料", "食品", "菓子", "調味料",
        "日用品", "冷蔵", "冷凍", "その他"
    )
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    // 選択中カテゴリの商品一覧
    val products = remember(selectedCategory, storeId) {
        StoreDataRepository.getProductsByStore(storeId).filter {
            it.category == selectedCategory
        }
    }

    // ★ カート合計（毎回計算 → リアルタイム更新のため）
    val storeCartTotal = cartViewModel.cartItems
        .filter { it.storeId == storeId }
        .sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(storeName) }, // ★ 店舗名を表示
                navigationIcon = {
                    IconButton(
                        onClick = {
                            val prev = navController.previousBackStackEntry?.destination?.route
                            if (prev == Routes.MAIN) {
                                navController.navigate(Routes.STORE_SELECT) {
                                    popUpTo(Routes.STORE_SELECT) { inclusive = false }
                                }
                            } else {
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
                .fillMaxSize()
        ) {

            // カテゴリタブ
            ScrollableTabRow(selectedTabIndex = categories.indexOf(selectedCategory)) {
                categories.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { Text(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 商品一覧
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        cartViewModel = cartViewModel
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 店舗カート合計
            Text(
                text = "この店舗のカート合計：${storeCartTotal.toInt()} 円",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // カートへ
            Button(
                onClick = {
                    navController.navigate(Routes.CART)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("カートを見る")
            }
        }
    }
}
