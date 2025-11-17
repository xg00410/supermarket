// =========================================================
// File: StoreRegionScreen.kt
// 概要: 日本の地域区分を表示し、ユーザーがエリアを選択する画面。
//設計書ID: なし（追加機能）
//画面名: 地域選択画面
// 更新者: 小林さん
// 更新日: 2025-11-17
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.models.Store
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegionScreen(navController: NavController) {

    // ★ stores 表のすべての店舗
    val stores = StoreDataRepository.getAllStores()

    // ★ 都道府県一覧（住所の先頭3文字で抽出して distinct）
    val prefectures = stores
        .map { it.address.take(3) }
        .distinct()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("都道府県を選択") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(prefectures) { prefecture ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("${Routes.STORE_REGION}/$prefecture")
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(prefecture, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

/**
 * --------------------------------------------------------------------
 * 店舗一覧（都道府県を選んだ後の画面）
 * Route: STORE_REGION/{prefecture}
 * --------------------------------------------------------------------
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegionDetailScreen(
    navController: NavController,
    prefecture: String
) {

    val stores = StoreDataRepository.getAllStores()
        .filter { it.address.startsWith(prefecture) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("${prefecture} の店舗") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
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

            items(stores) { store ->
                PrefectureStoreItem(store) {
                    navController.navigate("${Routes.STORE_DETAIL}/${store.storeId}")
                }
            }
        }
    }
}

@Composable
fun PrefectureStoreItem(store: Store, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(store.storeName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(store.address, style = MaterialTheme.typography.bodySmall)
        }
    }
}