// =========================================================
// File: List2Screen.kt
// 設計書ID: list2
// 画面名: カート（管理モード）
// 役割:
//   - カート内容を閲覧のみ可能。
//   - 数量変更、削除は不可。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun List2Screen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartItems = cartViewModel.cartItems

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("カート（管理モード）") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(cartItems) { item ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Image(
                        painter = painterResource(id = item.product.imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.product.name, style = MaterialTheme.typography.titleMedium)
                        Text("価格：¥${item.product.price}")
                        Text("数量：${item.quantity}")
                        Text("在庫：${item.product.stock}")
                    }
                }

                Divider()
            }
        }
    }
}
