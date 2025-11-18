// =========================================================
// File: StoreMapScreen.kt
// 設計書ID: store_Current
// 画面名: 店舗地図画面
// 役割:
//   - 現在地周辺の店舗を地図風に一覧表示する簡易画面。
//   - 実際の地図APIの代わりに、リスト表示＋将来地図差し替え用コメントを記載。
//   - 店舗をタップすると店舗拡大画面へ遷移する。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapScreen(
    navController: NavController
) {
    val stores = StoreDataRepository.getAllStores()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店舗地図") },
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
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "※ 将来的に地図APIを埋め込むエリア（現在はリスト表示で代用）",
                style = MaterialTheme.typography.bodySmall
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stores, key = { it.storeId }) { store ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("${Routes.STORE_DETAIL}/${store.storeId}")
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(store.storeName, style = MaterialTheme.typography.titleMedium)
                            Text(store.address, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
