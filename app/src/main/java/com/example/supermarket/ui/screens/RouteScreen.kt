// =========================================================
// File: RouteScreen.kt
// 画面名: 最短ルート画面（DSL修正版）
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.AreaNode
import com.example.supermarket.data.HistoryRepository
import com.example.supermarket.data.RouteRepository
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.HistoryItem
import com.example.supermarket.models.HistoryProduct
import com.example.supermarket.viewmodel.CartViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    storeId: String
) {
    val cartItems = cartViewModel.cartItems
    val store = StoreDataRepository.getStore(storeId)

    val areaIds: Set<String> = cartItems.map { it.product.category }.toSet()
    var areaOrder by remember { mutableStateOf(RouteRepository.buildInitialAreaOrder(areaIds)) }

    var checkedMap by remember { mutableStateOf(mutableMapOf<Int, Boolean>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("最短ルート") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val obtained = cartItems.filter {
                        checkedMap[it.product.productId] == true
                    }

                    obtained.forEach { cartViewModel.remove(it.product) }

                    if (obtained.isNotEmpty()) {
                        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
                        val now = sdf.format(Date())

                        val history = HistoryItem(
                            historyId = System.currentTimeMillis().toString(),
                            dateTime = now,
                            storeName = obtained.first().product.storeName,
                            items = obtained.map {
                                HistoryProduct(
                                    productId = it.product.productId,
                                    name = it.product.name,
                                    price = it.product.price,
                                    quantity = it.quantity,
                                    imageRes = it.product.imageRes
                                )
                            }
                        )
                        HistoryRepository.addHistory(history)
                    }

                    navController.navigate("cart")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("ナビを終了する")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 上：店内マップ（ダミー）
            store?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    // floorMapRes が画像リソースならここで表示
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = it.floorMapRes),
                        contentDescription = "店内マップ",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // エリアスライダー
            Text(
                text = "エリア順序（左右で並べ替え）",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(areaOrder) { area ->
                    AreaChipWithArrows(
                        area = area,
                        areaOrder = areaOrder,
                        onMoveLeft = { moved -> areaOrder = moveArea(areaOrder, moved, -1) },
                        onMoveRight = { moved -> areaOrder = moveArea(areaOrder, moved, +1) }
                    )
                }
            }

            Divider()

            // 商品一覧（ここが DSL 修正版）
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(areaOrder) { area ->
                    val itemsInArea: List<CartItem> =
                        cartItems.filter { it.product.category == area.id }

                    if (itemsInArea.isEmpty()) return@items

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "エリア：${area.id}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        itemsInArea.forEach { cartItem ->
                            RouteItemRow(
                                cartItem = cartItem,
                                checked = checkedMap[cartItem.product.productId] ?: false,
                                onCheckedChange = { newValue ->
                                    checkedMap = checkedMap.toMutableMap().apply {
                                        this[cartItem.product.productId] = newValue
                                    }
                                }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AreaChipWithArrows(
    area: AreaNode,
    areaOrder: List<AreaNode>,
    onMoveLeft: (AreaNode) -> Unit,
    onMoveRight: (AreaNode) -> Unit
) {
    val index = areaOrder.indexOf(area)
    val isFirst = index == 0
    val isLast = index == areaOrder.lastIndex

    Surface(tonalElevation = 3.dp, shape = MaterialTheme.shapes.medium) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = { if (!isFirst) onMoveLeft(area) }, enabled = !isFirst) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "左へ")
            }
            AssistChip(onClick = {}, label = { Text(area.id) })
            IconButton(onClick = { if (!isLast) onMoveRight(area) }, enabled = !isLast) {
                Icon(Icons.Filled.ArrowForwardIos, contentDescription = "右へ")
            }
        }
    }
}

private fun moveArea(
    current: List<AreaNode>,
    target: AreaNode,
    direction: Int
): List<AreaNode> {
    val list = current.toMutableList()
    val index = list.indexOf(target)
    val newIndex = (index + direction).coerceIn(0, list.lastIndex)
    list.removeAt(index)
    list.add(newIndex, target)
    return list
}

@Composable
private fun RouteItemRow(
    cartItem: CartItem,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(cartItem.product.name)
            Text("数量：${cartItem.quantity}")
        }
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }
}
