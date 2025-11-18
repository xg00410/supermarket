// =========================================================
// File: StoreRegionScreen.kt
// 設計書ID: store_region
// 画面名: 都道府県別店舗一覧画面
// 役割:
//   - 選択された都道府県に属する店舗一覧を表示する。
//   - 店舗カードをタップすると「店舗拡大画面(StoreDetail)」へ遷移する。
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
fun StoreRegionScreen(
    navController: NavController,
    prefecture: String
) {
    val allStores = StoreDataRepository.getAllStores()
    val storesInPrefecture = allStores.filter {
        it.address.contains(prefecture)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$prefecture の店舗一覧") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        if (storesInPrefecture.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("この都道府県には登録された店舗がありません。")
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(storesInPrefecture, key = { it.storeId }) { store ->
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
                            .padding(12.dp)
                    ) {
                        Text(store.storeName, style = MaterialTheme.typography.titleMedium)
                        Text(store.address, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
