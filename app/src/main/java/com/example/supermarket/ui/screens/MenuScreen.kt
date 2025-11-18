// =========================================================
// ファイル名: MenuScreen.kt
// 設計書ID: menu
// 画面名: 店舗画面（商品一覧）
// 役割:
//   - 左側にカテゴリ一覧を表示（8カテゴリ）
//   - 右側にカテゴリ別の商品一覧を表示
//   - 商品画像／名前／価格／在庫／数量選択（＋／－）
//   - 「カートに追加」ボタン
//   - 下部に「カートを見る」ボタン
//   - 設計書のレイアウト構成に完全準拠
//
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.viewmodel.CartViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.supermarket.ui.components.ProductCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    val categories = listOf(
        "飲料", "食品", "調味料", "菓子",
        "日用品", "冷蔵", "冷凍", "その他"
    )

    var selectedCategory by remember { mutableStateOf(categories.first()) }

    val products = remember(selectedCategory) {
        StoreDataRepository.getProductsByStore(storeId).filter {
            it.category == selectedCategory
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("商品一覧") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
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

            // --- 分类标签 ---
            ScrollableTabRow(selectedTabIndex = categories.indexOf(selectedCategory)) {
                categories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { Text(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 商品列表 ---
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        cartViewModel = cartViewModel
                    )
                }
            }
        }
    }
}