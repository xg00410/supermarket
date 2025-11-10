package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supermarket.data.RouteRepository
import com.example.supermarket.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    onBack: () -> Unit,
    cartViewModel: CartViewModel = viewModel()
) {
    val cartItems = cartViewModel.cartItems
    var isNavigating by remember { mutableStateOf(false) }
    var routeList by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("最短ルート") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("戻る") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 🗺️ 地图区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isNavigating)
                        "🧭 最短ルート順に移動中..."
                    else
                        "ここに店舗マップを表示",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }

            // 商品リスト
            Text("買う商品リスト", style = MaterialTheme.typography.titleMedium)
            if (cartItems.isEmpty()) {
                Text("カートが空です")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { item ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            tonalElevation = 2.dp,
                            shadowElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(item.productName)
                                if (routeList.isNotEmpty()) {
                                    val order = routeList.indexOf(item.productName) + 1
                                    Text("順番: $order 番目", color = Color(0xFF1976D2))
                                }
                            }
                        }
                    }
                }
            }

            // 按钮行
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (!isNavigating) {
                            val route = RouteRepository.getShortestRoute(
                                cartItems.map { it.productId }
                            )
                            routeList = route.map { it.name }
                        }
                        isNavigating = !isNavigating
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isNavigating) "停止ナビ" else "最短ルート開始")
                }

                Button(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("戻る")
                }
            }

            if (routeList.isNotEmpty()) {
                Text(
                    text = "最短ルート順:\n" + routeList.joinToString(" → "),
                    color = Color(0xFF388E3C),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
