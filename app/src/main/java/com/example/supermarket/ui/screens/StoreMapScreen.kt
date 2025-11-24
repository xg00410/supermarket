// =========================================================
// File: StoreMapScreen.kt
// 設計書ID: store_Current
// 画面名: 店舗地図画面
// 役割:
//   - 現在地周辺の店舗を地図風に一覧表示する簡易画面。
//   - 実際の地図APIの代わりに、リスト表示で店舗を表示する。
//   - 店舗をタップすると店舗拡大画面(StoreDetailScreen)へ遷移する。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.models.Store
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapScreen(
    navController: NavController
) {
    // 店舗一覧状態
    var storeList by remember { mutableStateOf<List<Store>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 画面初期表示時に店舗一覧を取得
    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null

        try {
            val api = ApiClient.retrofit.create(ApiService::class.java)
            val res = api.getStores()

            if (res.status == "ok" && res.data != null) {
                storeList = res.data.map { dto ->
                    Store(
                        storeId = dto.store_id,
                        storeName = dto.name,
                        address = dto.address,
                        latitude = dto.latitude ?: 0.0,
                        longitude = dto.longitude ?: 0.0,
                        imageRes = null,
                        floorMapRes = null
                    )
                }
            } else {
                errorMessage = res.message ?: "店舗情報の取得に失敗しました。"
            }
        } catch (e: Exception) {
            errorMessage = "店舗情報の取得中にエラーが発生しました。"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店舗地図（一覧表示）") },
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
    ) { paddingValues ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                }
            }

            storeList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("表示できる店舗がありません。")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(storeList) { store ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable {
                                    // 店舗タップ → 店舗拡大画面へ
                                    navController.navigate("store_detail/${store.storeId}")
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = store.storeName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = store.address,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
