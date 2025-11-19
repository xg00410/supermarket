// =========================================================
// File: MenuScreen.kt
// 設計書ID: menu
// 画面名: 店舗画面（商品一覧）
// 役割:
//   - 左側にカテゴリ一覧（8カテゴリ）を表示。
//   - 右側にカテゴリ別の商品一覧を表示。
//   - 商品画像／名前／価格／在庫／数量ボタン（＋／－）。
//   - 画面下部に「カートに入れる」「最短ルートへ」ボタンを表示。
//   - 店舗ごとのカート合計金額を下部に表示。
//   - 店舗名を画面上部に表示して、どの店舗か分かるようにする。
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

private enum class MenuCommitAction {
    ADD_TO_CART,
    GO_ROUTE
}

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

    // 店舗情報
    val store = remember(storeId) {
        StoreDataRepository.getStoreById(storeId)
    }

    // この店舗に属する全商品
    val allProductsForStore = remember(storeId) {
        StoreDataRepository.getProductsByStore(storeId)
    }

    // productId -> Product のマップ（合計金額計算用）
    val productMap = remember(storeId) {
        allProductsForStore.associateBy { it.productId }
    }

    // 画面内の一時的な「選択数量」
    // key: productId, value: 選択数量
    val tempQuantities = remember(storeId) {
        mutableStateMapOf<Int, Int>()
    }

    // 現在表示中カテゴリの商品一覧
    val productsOfCategory = remember(selectedCategory, allProductsForStore) {
        allProductsForStore.filter { it.category == selectedCategory }
    }

    // すでにカートに入っている「この店舗の合計金額」
    val storeCartTotal = cartViewModel.cartItems
        .filter { it.storeId == storeId }
        .sumOf { it.price * it.quantity }

    // 画面内で一時選択している商品群の合計金額
    val tempTotal = tempQuantities.entries.sumOf { (productId, qty) ->
        val p = productMap[productId]
        if (p != null && qty > 0) p.price * qty else 0.0
    }

    // ルートに進んでよいか？
    val hasTempSelection = tempQuantities.values.any { it > 0 }
    val hasCartForStore = cartViewModel.cartItems.any { it.storeId == storeId }
    val canGoRoute = hasTempSelection || hasCartForStore

    // 「カートに入れる／最短ルートへ」を押したときの共通処理
    fun handleCommit(action: MenuCommitAction) {
        // 一時選択をカートへ反映
        tempQuantities.forEach { (productId, qty) ->
            if (qty > 0) {
                val p = productMap[productId]
                if (p != null) {
                    cartViewModel.addToCart(p, qty)
                }
            }
        }
        // 画面内の数量をリセット
        tempQuantities.clear()

        if (action == MenuCommitAction.GO_ROUTE) {
            // この店舗の最短ルート画面へ
            navController.navigate("${Routes.ROUTE}/$storeId")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // 上部タイトルに「店舗名」を表示
                    Text(text = store?.storeName ?: "商品一覧")
                },
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

            Spacer(modifier = Modifier.height(8.dp))

            // 商品一覧（上部：スクロール領域）
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(productsOfCategory, key = { it.productId }) { product ->
                    val q = tempQuantities[product.productId] ?: 0
                    ProductCard(
                        product = product,
                        quantity = q,
                        onQuantityChange = { newQty ->
                            // 0以下は削除扱い
                            if (newQty <= 0) {
                                tempQuantities.remove(product.productId)
                            } else {
                                tempQuantities[product.productId] = newQty
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 画面下部情報
            Text(
                text = "この店舗のカート合計（反映済み）：${storeCartTotal.toInt()} 円",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "今回の選択（未反映）：${tempTotal.toInt()} 円",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 下部ボタン：カートに入れる ＋ 最短ルートへ
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { handleCommit(MenuCommitAction.ADD_TO_CART) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("カートに入れる")
                }

                Button(
                    onClick = { handleCommit(MenuCommitAction.GO_ROUTE) },
                    modifier = Modifier.weight(1f),
                    enabled = canGoRoute
                ) {
                    Text("最短ルートへ")
                }
            }
        }
    }
}
