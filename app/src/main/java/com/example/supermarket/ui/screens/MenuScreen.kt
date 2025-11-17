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


import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.models.Product
import com.example.supermarket.ui.Routes
import com.example.supermarket.viewmodel.CartViewModel

// 設計書ID: menu
// 画面名: 店舗画面（商品一覧）
// 機能:
// - 左側に商品カテゴリ一覧を表示（8種類を想定）
// - 右側に選択されたカテゴリの商品カードを一覧表示
// - 各商品カードには画像・名称・単価・在庫数・選択数(+/-ボタン)・「カートに追加」ボタンを配置
// - 画面下部「カートを見る」ボタンからカート画面(list)へ遷移
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    val store = StoreDataRepository.getStoreById(storeId)

    // 仮のカテゴリと商品データ / 临时分类和商品数据
    val categories = listOf("飲料", "食品", "お菓子", "冷凍", "日用品", "調味料", "惣菜", "その他")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    // 実際はカテゴリと紐付いた商品リストを取得する / 实际会按分类取得商品列表
    val products = remember(selectedCategory) {
        StoreDataRepository.getProducts().filter {
            it.category == selectedCategory
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(store?.storeName ?: "店舗画面")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る / 返回")
                    }
                }
            )
        },
        bottomBar = {
            // 下部の「カートを見る」ボタン / 底部“查看购物车”按钮
            Surface(
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "カート内商品数: ${cartViewModel.cartItems.size}",
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { navController.navigate(Routes.CART) }
                    ) {
                        Text("カートを見る")
                    }
                }
            }
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 左側: カテゴリ一覧 / 左侧：分类列表
            Column(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                verticalArrangement = Arrangement.Top
            ) {
                categories.forEach { category ->
                    val selected = category == selectedCategory
                    val bgColor =
                        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else Color.Transparent
                    val textColor =
                        if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(bgColor)
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = category,
                            color = textColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // 右側: 商品一覧 / 右侧：商品列表
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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

// 商品カードコンポーネント / 商品卡片组件
@Composable
private fun ProductCard(
    product: Product,
    cartViewModel: CartViewModel
) {
    var quantity by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 商品画像 / 商品图片
            Image(
                painter = painterResource(android.R.drawable.ic_menu_report_image),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "単価: ${product.price} 円")
                Text("在庫数: 不明")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 個数 +/- / 数量 +/- 按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (quantity > 0) quantity-- },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("-")
                    }
                    Text(text = quantity.toString())
                    OutlinedButton(
                        onClick = { quantity++ },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("+")
                    }
                }

                // カートに追加ボタン / 加入购物车按钮
                Button(
                    onClick = {
                        if (quantity > 0) {
                            cartViewModel.addToCart(product, quantity)
                            quantity = 0
                        }
                    }
                ) {
                    Text("カートに追加")
                }
            }
        }
    }
}
