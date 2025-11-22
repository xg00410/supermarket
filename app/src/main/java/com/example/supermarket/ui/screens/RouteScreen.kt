// =========================================================
// File: RouteScreen.kt
// 設計書ID: route
// 画面名: 最短ルート画面
// 役割:
//   - カート内商品のカテゴリを元にエリア順序（自動ルート）を算出。
//   - ユーザーがエリア順序を < / > ボタンで手動変更できる。
//   - 下部に対象商品の簡易一覧（商品名 + 数量）を表示。
//   - 「終了」ボタンでチェック済み商品の注文を DB に登録し、
//     ローカル履歴追加＋カートから削除する。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.supermarket.R
import com.example.supermarket.data.RouteRepository
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.data.UserSession
import com.example.supermarket.models.InsertOrderBody
import com.example.supermarket.models.OrderItem
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import com.example.supermarket.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    // ---------------- 店舗情報 ----------------
    val store = remember(storeId) { StoreDataRepository.getStoreById(storeId) }

    // ---------------- 対象店舗のカート商品 ----------------
    val cartItemsInStore by remember {
        derivedStateOf {
            cartViewModel.cartItems.filter { it.storeId == storeId }
        }
    }

    // ---------------- 商品ごとのチェック状態（購入するかどうか） ----------------
    val checkedMap = remember {
        mutableStateMapOf<Int, Boolean>()
    }
    // カート内容変化時に初期値をリセット（全て true）
    LaunchedEffect(cartItemsInStore) {
        checkedMap.clear()
        cartItemsInStore.forEach { item ->
            checkedMap[item.productId] = true
        }
    }

    // ---------------- エリア順序（カテゴリ名をエリアとみなす） ----------------
    // カート内のカテゴリ一覧
    val areaIds by remember(cartItemsInStore) {
        mutableStateOf(
            cartItemsInStore.map { it.category }.distinct()
        )
    }

    // 自動最短ルート（RouteRepository を利用）
    val autoAreaOrder by remember(areaIds) {
        mutableStateOf(
            RouteRepository.calcShortestAreaOrder(areaIds)
        )
    }

    // ユーザーが手動調整するエリア順序
    val userAreaOrder = remember(autoAreaOrder) {
        mutableStateListOf<String>().apply {
            clear()
            addAll(autoAreaOrder)
        }
    }

    // 実際に画面に表示するエリア順序（手動が優先）
    val finalAreaOrder by remember(userAreaOrder) {
        derivedStateOf { userAreaOrder.toList() }
    }

    // ---------------- 注文登録用 ----------------
    val scope = rememberCoroutineScope()
    val api = remember { ApiClient.retrofit.create(ApiService::class.java) }

    var isSending by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("最短ルート (${store?.storeName ?: ""})")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        },
        bottomBar = {
            // 画面下部中央の「終了」ボタン
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val userId = UserSession.userId
                        if (userId == null) {
                            dialogMessage = "ログイン情報が見つかりません。先にログインしてください。"
                            return@Button
                        }

                        val checkedItems = cartItemsInStore.filter { checkedMap[it.productId] == true }
                        if (checkedItems.isEmpty()) {
                            dialogMessage = "購入する商品が選択されていません。"
                            return@Button
                        }

                        isSending = true

                        scope.launch {
                            try {
                                // DB へ注文登録
                                val orderItems = checkedItems.map {
                                    OrderItem(
                                        product_id = it.productId,
                                        quantity = it.quantity,
                                        price = it.price
                                    )
                                }

                                val body = InsertOrderBody(
                                    user_id = userId,
                                    store_code = storeId, // storeId をそのまま店舗コードとして送信
                                    items = orderItems
                                )

                                val res = api.insertOrder(body)
                                if (res.status == "ok") {
                                    // ローカル履歴に追加
                                    cartViewModel.addHistoryEntry(
                                        storeId = storeId,
                                        storeName = store?.storeName ?: "",
                                        items = checkedItems,
                                        orderedAt = LocalDateTime.now()
                                    )
                                    // カートから削除
                                    val checkedIds = checkedItems.map { it.productId }.toSet()
                                    cartViewModel.removeCheckedItems(checkedIds)

                                    isSending = false
                                    dialogMessage = "注文を登録しました。（注文ID: ${res.order_id ?: "-"}）"
                                } else {
                                    isSending = false
                                    dialogMessage = res.message ?: "注文登録に失敗しました。"
                                }
                            } catch (e: Exception) {
                                isSending = false
                                dialogMessage = "通信エラーが発生しました。"
                            }
                        }
                    },
                    enabled = !isSending,
                    modifier = Modifier
                        .widthIn(min = 160.dp)
                        .height(46.dp) // 少し小さめの高さ
                ) {
                    Text(if (isSending) "送信中..." else "終了")
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ---------------- 店内マップ（大きめに表示） ----------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f), // 全体のうち 3/5 程度を地図
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEAEAEA)),
                    contentAlignment = Alignment.Center
                ) {
                    val floorMapRes = store?.floorMapRes ?: R.drawable.store_floor_map
                    AsyncImage(
                        model = floorMapRes,
                        contentDescription = "store map",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // ---------------- エリア順序（スライダー + < / > ボタン） ----------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "エリア順序",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    finalAreaOrder.forEachIndexed { index, id ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            tonalElevation = 2.dp,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 左矢印（先頭以外）
                                if (index > 0) {
                                    IconButton(
                                        onClick = {
                                            val currentIndex = userAreaOrder.indexOf(id)
                                            if (currentIndex > 0) {
                                                val prev = userAreaOrder[currentIndex - 1]
                                                userAreaOrder[currentIndex - 1] = id
                                                userAreaOrder[currentIndex] = prev
                                            }
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowLeft,
                                            contentDescription = "左へ"
                                        )
                                    }
                                }

                                Text(text = id)

                                // 右矢印（最後以外）
                                if (index < finalAreaOrder.size - 1) {
                                    IconButton(
                                        onClick = {
                                            val currentIndex = userAreaOrder.indexOf(id)
                                            if (currentIndex >= 0 && currentIndex < userAreaOrder.size - 1) {
                                                val next = userAreaOrder[currentIndex + 1]
                                                userAreaOrder[currentIndex + 1] = id
                                                userAreaOrder[currentIndex] = next
                                            }
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowRight,
                                            contentDescription = "右へ"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ---------------- 対象商品（横スクロール / 小さめカード） ----------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f) // 地図より小さめ
            ) {
                Text(
                    text = "対象商品",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cartItemsInStore.forEach { item ->
                        val checked = checkedMap[item.productId] ?: true

                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .height(90.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // 商品名（1〜2行程度）
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2
                                )

                                // 数量のみ表示
                                Text(
                                    text = "数量：${item.quantity}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                // チェックボックス（右下）
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "購入",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = { newChecked ->
                                            checkedMap[item.productId] = newChecked
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ---------------- 結果ダイアログ ----------------
    if (dialogMessage != null) {
        AlertDialog(
            onDismissRequest = {
                dialogMessage = null
            },
            title = { Text("情報") },
            text = { Text(dialogMessage!!) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val msg = dialogMessage
                        dialogMessage = null
                        // 成功メッセージだった場合だけ前の画面へ戻る
                        if (msg != null && msg.startsWith("注文を登録しました")) {
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
