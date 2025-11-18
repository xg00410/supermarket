// =========================================================
// File: MenuScreen.kt
// 設計書ID: menu
// 画面名: 店舗画面（商品一覧）
// 役割:
//   - 左側にカテゴリ一覧（8カテゴリ）を表示。
//   - 右側にカテゴリ別の商品一覧を表示。
//   - 商品画像／名前／価格／在庫／数量ボタン（＋／－）。
//   - 画面下部に「カートを見る」ボタンを表示。
//   - 店舗ごとのカート合計金額を下部に表示。
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
    // 8カテゴリ（設計書に合わせて固定）
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

    // 現在の店舗に紐づくカート内商品の合計金額
    val storeCartTotal by remember(cartViewModel.cartItems, storeId) {
        mutableStateOf(
            cartViewModel.cartItems
                .filter { it.storeId == storeId }
                .sumOf { it.price * it.quantity }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("商品一覧") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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

            // 商品一覧（上部：スクロール領域）
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

            // 現在の店舗のカート合計（参考表示）
            Text(
                text = "この店舗のカート合計：${storeCartTotal.toInt()} 円",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // カートへ
            Button(
                onClick = { navController.navigate(Routes.CART) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("カートを見る")
            }
        }
    }
}
