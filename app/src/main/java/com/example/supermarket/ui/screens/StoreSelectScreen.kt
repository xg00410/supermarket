// =========================================================
// File: StoreSelectScreen.kt
// 設計書ID: store_select
// 画面名: 店舗選択画面
// 役割:
//   - 店舗を都道府県（地域）ごとに分類して一覧表示する。
//   - 店舗カードをタップすると「店舗拡大画面（詳細画面）」へ遷移。
//   - 旧検索バーは使用しない（ユーザーの要望による削除）。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes
import com.example.supermarket.models.Store

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSelectScreen(navController: NavController) {

    val stores = StoreDataRepository.getAllStores()
    val grouped = stores.groupBy { extractPrefecture(it.address) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("店舗選択") }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // -----------------------------------------
            // ★ GPS 検索（現在地から検索）
            // -----------------------------------------
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Routes.GPS_PERMISSION)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("現在地から検索", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            // -----------------------------------------
            // ★ 都道府県で探す
            // -----------------------------------------
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Routes.STORE_REGION)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("都道府県で探す", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            // -----------------------------------------
            // ★ stores の都道府県別 店舗カード一覧
            // -----------------------------------------
            grouped.forEach { (prefecture, list) ->

                item {
                    Text(prefecture, style = MaterialTheme.typography.titleMedium)
                }

                items(list) { store ->
                    StoreCardItem(store) {
                        navController.navigate("${Routes.STORE_DETAIL}/${store.storeId}")
                    }
                }
            }
        }
    }
}

@Composable
fun StoreCardItem(store: Store, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            store.imageRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = store.storeName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(store.storeName, style = MaterialTheme.typography.titleMedium)
            Text(store.address, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// ★ address 先頭3文字から都道府県を抽出
fun extractPrefecture(address: String): String {
    return address.take(3)
}