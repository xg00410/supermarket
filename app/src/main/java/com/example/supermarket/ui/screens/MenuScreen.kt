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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.Routes
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    storeId: String,
    cartViewModel: CartViewModel
) {
    val store = StoreDataRepository.getStoreById(storeId)
    val allProducts = StoreDataRepository.getProductsByStore(storeId)

    // 設計書に従いカテゴリ8種
    val categories = listOf(
        "飲料", "食品", "調味料", "菓子",
        "日用品", "冷蔵", "冷凍", "その他"
    )

    var selectedCategory by remember { mutableStateOf(categories[0]) }

    // 選択カテゴリの商品だけ表示
    val filteredProducts = allProducts.filter { it.category == selectedCategory }

    // 個数管理（商品ID → 数量）
    val quantities = remember { mutableStateMapOf<String, Int>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(store?.name ?: "商品一覧") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.STORE_DETAIL) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { navController.navigate(Routes.LIST) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text("カートを見る")
            }
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

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ======================================================
            // 左側カテゴリ一覧（幅固定）
            // ======================================================
            Column(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                verticalArrangement = Arrangement.Top
            ) {
                categories.forEach { category ->
                    val selected = (category == selectedCategory)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else Color.Transparent
                            )
                            .clickable { selectedCategory = category }
                            .padding(8.dp)
                    ) {
                        Text(
                            text = category,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // ======================================================
            // 右側商品一覧
            // ======================================================
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredProducts) { item ->

                    // 数量初期化
                    val qty = quantities[item.id] ?: 0

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            // 商品画像
                            item.imageRes?.let { img ->
                                Image(
                                    painter = painterResource(id = img),
                                    contentDescription = item.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            // 商品名
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            // 価格
                            Text("価格：${item.price} 円")

                            // 在庫
                            Text("在庫：${item.stock}")

                            // ======================================================
                            // 数量選択（－ ボタン、数量表示、＋ ボタン）
                            // ======================================================
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                // －ボタン
                                OutlinedButton(
                                    onClick = {
                                        if (qty > 0) quantities[item.id] = qty - 1
                                    }
                                ) { Text("－") }

                                Text("$qty 個")

                                // ＋ボタン（在庫以上は不可）
                                OutlinedButton(
                                    onClick = {
                                        if (qty < item.stock) quantities[item.id] = qty + 1
                                    }
                                ) { Text("＋") }
                            }

                            // ======================================================
                            // カートに追加
                            // ======================================================
                            Button(
                                onClick = {
                                    if (qty > 0) {
                                        cartViewModel.addToCart(item, qty)
                                        quantities[item.id] = 0  // 追加後数量リセット
                                    }
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
}
