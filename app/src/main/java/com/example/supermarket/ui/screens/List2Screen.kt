// =========================================================
// File: List2Screen.kt
// 設計書ID: list2
// 画面名: リスト画面②（カート管理）
// 役割:
//   - 店舗単位でカート内の商品をグループ化して表示
//   - 店舗チェック → 店舗内の全商品チェック
//   - 商品チェック → 店舗チェックの自動ON/OFF
//   - すべて選択チェック
//   - 選択された商品の一括削除
//   - 商品数量の増減（＋／－）
//   - 店舗内の商品が0件になると店舗ブロックを非表示
//
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.models.CartItem
import com.example.supermarket.ui.components.BottomNavBar
import com.example.supermarket.viewmodel.CartViewModel

// 設計書ID: list2
// 画面名: カート管理画面（リスト②）
// 機能:
// - カート内の商品を1行ごとに表示
// - 行ごとに「削除」ボタンを配置して商品をカートから削除
// - 下部には合計金額などを表示（簡易的に実装）
// - 画面下部に共通のボトムナビゲーション（店舗選択 / 店舗 / カート / マイページ）
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun List2Screen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    // ViewModel のカートリストをローカルで編集 / 在本地列表上编辑
    val cartItems = remember { mutableStateListOf<CartItem>().apply { addAll(cartViewModel.cartItems) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カート管理") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る / 返回")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // カート商品一覧 / 购物车商品列表
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems, key = { it.productId }
                ) { item ->
                    ProductManageCard(
                        cartItem = item,
                        onDelete = {
                            cartItems.remove(item)
                            cartViewModel.removeItem(item.productId)

                        }
                    )
                }
            }

            // 合計金額など（簡易表示）/ 合计金额（简易显示）
            val total = cartItems.sumOf { (it.price * it.quantity).toInt() }

            Surface(
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "合計: ${total} 円",
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = "点数: ${cartItems.sumOf { it.quantity }}")
                }
            }
        }
    }
}

@Composable
private fun ProductManageCard(
    cartItem: CartItem,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo),

                contentDescription = cartItem.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = cartItem.name)
                Text(text = "数量: ${cartItem.quantity}")
                Text(text = "小計: ${(cartItem.price * cartItem.quantity).toInt()} 円")
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "削除 / 删除")
            }
        }
    }
}
