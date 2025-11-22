// =========================================================
// File: MenuScreen.kt
// 設計書ID: menu
// 画面名: 店舗画面（商品一覧）
// 役割:
//   - 上部に「店舗名」を表示。
//   - 左側にカテゴリタブ（8カテゴリ固定）。
//   - 右側にカテゴリ別の商品一覧を表示。
//   - 商品ごとに「一時選択数量」を保持（tempQuantities）。
//   - 下部に「カートに入れる」「最短ルートへ」ボタン。
//   - 在庫を超える数量が選択されている場合はダイアログで確認。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.models.Product
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.ProductCard
import com.example.supermarket.viewmodel.CartViewModel

// 商品確定時の動作種別
private enum class MenuCommitAction {
    ADD_TO_CART,     // カートに入れる
    GO_ROUTE         // 最短ルートへ
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    // カートの状態（この店舗の商品のみ抽出）
    val cartItemsInThisStore = cartViewModel.cartItems.filter { item -> item.storeId == storeId }

    // 店舗情報（店名表示用）
    val store = remember(storeId) {
        StoreDataRepository.getStoreById(storeId)
    }

    // この店舗の全商品（ダミー or DB から取得）
    var allProducts by remember(storeId) {
        mutableStateOf<List<Product>>(emptyList())
    }

    // 実際に「DB結果を使っているかどうか」を示すフラグ
    var isDbResult by remember(storeId) {
        mutableStateOf(false)
    }

    // DBモード時のエラー内容を画面下に表示するためのメッセージ
    var dbErrorMessage by remember(storeId) {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(storeId, StoreDataRepository.useDatabaseMode) {
        if (StoreDataRepository.useDatabaseMode) {
            // DBモード：まずは API を試す
            try {
                val api = ApiClient.retrofit.create(ApiService::class.java)
                val res = api.getProductsByStore(storeId)

                if (res.status == "ok" && res.data != null) {
                    // DB からのデータを使用
                    allProducts = res.data.map { dto ->
                        Product(
                            productId = dto.product_id,
                            storeId = storeId,
                            storeName = store?.storeName ?: "",
                            name = dto.name,
                            category = dto.category ?: "その他",
                            price = dto.price,
                            stock = dto.stock ?: 0,
                            imageRes = store?.imageRes
                                ?: com.example.supermarket.R.drawable.logo
                        )
                    }
                    isDbResult = true
                    dbErrorMessage = null
                } else {
                    // ステータス異常 → ダミーデータへフォールバック
                    allProducts = StoreDataRepository.getProductsByStore(storeId)
                    isDbResult = false
                    dbErrorMessage = "APIステータス異常: status=${res.status}, message=${res.message ?: "不明"}"
                }
            } catch (e: Exception) {
                // 通信エラー → ダミーデータへフォールバック
                allProducts = StoreDataRepository.getProductsByStore(storeId)
                isDbResult = false
                dbErrorMessage = "通信エラー: ${e.localizedMessage}"
            }
        } else {
            // ダミーモード：従来通りリポジトリから取得
            allProducts = StoreDataRepository.getProductsByStore(storeId)
            isDbResult = false
            dbErrorMessage = null
        }
    }

    // 8カテゴリ（設計書固定）
    val categories = listOf(
        "飲料", "食品", "菓子", "調味料",
        "日用品", "冷蔵", "冷凍", "その他"
    )

    var selectedCategory by remember { mutableStateOf(categories.first()) }

    // 一時選択数量: productId -> quantity
    val tempQuantities = cartViewModel.tempSelectedItems

    // 一時選択の合計金額（全カテゴリ合計）
    val tempTotal by remember(storeId) {
        derivedStateOf {
            allProducts.sumOf { product ->
                val q = tempQuantities[product.productId] ?: 0
                product.price * q
            }
        }
    }

    // 在庫超過確認ダイアログ用 state
    var overStockMessage by remember { mutableStateOf<String?>(null) }
    var pendingAction by remember { mutableStateOf<MenuCommitAction?>(null) }

    // 実際にカートへ反映＆最短ルートへ遷移する処理
    fun doCommit(action: MenuCommitAction) {
        val selected = allProducts.filter { (tempQuantities[it.productId] ?: 0) > 0 }
        if (selected.isEmpty()) return

        // カートに追加
        selected.forEach { product ->
            val qty = tempQuantities[product.productId] ?: 0
            if (qty > 0) {
                cartViewModel.addToCart(product, qty)
            }
        }

        // 一時選択をリセット
        tempQuantities.clear()

        // 最短ルートへ遷移する場合
        if (action == MenuCommitAction.GO_ROUTE) {
            navController.navigate("${Routes.ROUTE}/$storeId")
        }
    }

    // ボタン押下時の共通チェック（在庫超過確認）
    fun handleCommitRequest(action: MenuCommitAction) {
        val selected = allProducts.filter { (tempQuantities[it.productId] ?: 0) > 0 }
        if (selected.isEmpty()) {
            // 何も選んでいなければ何もしない（必要ならメッセージ表示も可）
            return
        }

        val over = selected.filter { product ->
            val qty = tempQuantities[product.productId] ?: 0
            qty > product.stock
        }

        if (over.isNotEmpty()) {
            val first = over.first()
            val qty = tempQuantities[first.productId] ?: 0
            overStockMessage =
                "選択された数量が在庫数を超えている商品があります。\n" +
                        "例：${first.name} 在庫：${first.stock} / 選択数：$qty\n\n" +
                        "このまま続行しますか？"
            pendingAction = action
        } else {
            doCommit(action)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // 店舗名をタイトルに表示
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
            // ★ 追加：現在のデータ取得状態を表示
            Text(
                text = when {
                    StoreDataRepository.useDatabaseMode && isDbResult ->
                        "現在：DB（PHP / MySQL）から商品データを取得しています。"
                    StoreDataRepository.useDatabaseMode && !isDbResult ->
                        "現在：DBモードですが、取得に失敗したためダミーデータを表示しています。"
                    else ->
                        "現在：ダミーデータモードです。"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // ★ 追加：エラー詳細（あれば表示）
            dbErrorMessage?.let { msg ->
                Text(
                    text = "エラー詳細: $msg",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

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

            // 選択中カテゴリの商品一覧
            val productsInCategory = remember(selectedCategory, allProducts) {
                allProducts.filter { it.category == selectedCategory }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(productsInCategory) { product ->
                    val q = tempQuantities[product.productId] ?: 0

                    ProductCard(
                        product = product,
                        quantity = q,
                        onIncrease = {
                            val current = tempQuantities[product.productId] ?: 0
                            tempQuantities[product.productId] = current + 1
                        },
                        onDecrease = {
                            val current = tempQuantities[product.productId] ?: 0
                            if (current > 0) {
                                tempQuantities[product.productId] = current - 1
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 一時選択の合計金額
            Text(
                text = "一時選択の合計：${tempTotal.toInt()} 円",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 下部ボタン：カートに入れる ＋ 最短ルートへ
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { handleCommitRequest(MenuCommitAction.ADD_TO_CART) },
                    modifier = Modifier.weight(1f),
                    enabled = tempTotal > 0.0
                ) {
                    Text("カートに入れる")
                }

                Button(
                    onClick = {
                        if (tempTotal > 0.0) {
                            // まだ一時選択中の商品がある → 在庫チェックしてからカート追加＋ルートへ
                            handleCommitRequest(MenuCommitAction.GO_ROUTE)
                        } else if (cartItemsInThisStore.isNotEmpty()) {
                            // 一時選択は空だが、この店舗のカート商品は存在する → そのままルート画面へ
                            navController.navigate("${Routes.ROUTE}/$storeId")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = (tempTotal > 0.0) || cartItemsInThisStore.isNotEmpty()
                ) {
                    Text("最短ルートへ")
                }
            }
        }
    }

    // 在庫超過確認ダイアログ
    if (overStockMessage != null) {
        AlertDialog(
            onDismissRequest = {
                overStockMessage = null
                pendingAction = null
            },
            title = { Text("在庫数超過の確認") },
            text = { Text(overStockMessage!!) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val action = pendingAction
                        overStockMessage = null
                        pendingAction = null
                        if (action != null) {
                            doCommit(action)
                        }
                    }
                ) {
                    Text("はい")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        overStockMessage = null
                        pendingAction = null
                    }
                ) {
                    Text("キャンセル")
                }
            }
        )
    }
}
