package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.viewmodel.CartViewModel

/**
 * RouteScreen（最短ルート）
 * 🇯🇵 最短ルート画面（簡易版）
 * 🇨🇳 最短路径画面（简化版）
 *
 * - 上半部分：地図エリア（ダミー）
 * - 下半部分：商品一覧 + チェックボックス
 * - GPS Start / Stop ボタン
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    onBack: () -> Unit
) {
    // ✔ cartItems 是普通 List<CartItem>，不需要 collectAsState()
    val cartItems = cartViewModel.cartItems

    // ✔ 用于 Checkbox 状态
    val checkedState = remember { mutableStateMapOf<Int, Boolean>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("最短ルート") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ---------------------------
            // 上半部分：地图占位区域
            // ---------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("地図（ダミー）")
            }

            Divider()

            // ---------------------------
            // 下半部分：购物车商品列表
            // ---------------------------
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems.size) { index ->
                    val item = cartItems[index]
                    val checked = checkedState[item.productId] ?: false

                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                                Text("数量：${item.quantity}")
                            }

                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    checkedState[item.productId] = it
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // ---------------------------
            // GPS Start / Stop
            // ---------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) { Text("GPS Start") }

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) { Text("GPS Stop") }
            }
        }
    }
}
