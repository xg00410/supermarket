// =========================================================
// File: StoreSelectScreen.kt
// 設計書ID: store_select
// 画面名: 店舗選択画面
// 役割:
//   - 店舗を都道府県（地域）ごとに分類して一覧表示する。
//   - 店舗カードをタップすると「店舗拡大画面（詳細画面）」へ遷移。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.models.Store
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.MainScaffold

@Composable
fun StoreSelectScreen(navController: NavController) {

    val stores = StoreDataRepository.getAllStores()
    val grouped = stores.groupBy { extractPrefecture(it.address) }

    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    MainScaffold(navController = navController, title = "店舗選択") { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 検索バー（今はUIだけ）
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("店舗名または住所で検索") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 現在地から検索
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
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("現在地から検索", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            // 店舗一覧（都道府県ごと）
            grouped.forEach { (pref, storeList) ->
                item {
                    Text(
                        text = pref,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                items(storeList) { store ->
                    StoreCardItem(
                        store = store,
                        onClick = {
                            navController.navigate("${Routes.STORE_DETAIL}/${store.storeId}")
                        }
                    )
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
            val imageRes = store.imageRes ?: R.drawable.logo
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = store.storeName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(store.storeName, style = MaterialTheme.typography.titleMedium)
            Text(store.address, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// 住所先頭3文字を都道府県として扱う
fun extractPrefecture(address: String): String = address.take(3)