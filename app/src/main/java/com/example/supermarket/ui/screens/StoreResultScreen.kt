// =========================================================
// File: StoreResultScreen.kt
// 画面名: 店舗検索結果表示
// 役割:
//   - 検索キーワード(keyword) に一致する店舗一覧を API から取得して表示。
//   - 住所・店舗名の部分一致でフィルタリング。
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
fun StoreResultScreen(
    navController: NavController,
    keyword: String
) {

    var storeList by remember { mutableStateOf<List<Store>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(keyword) {
        isLoading = true
        try {
            val api = ApiClient.retrofit.create(ApiService::class.java)
            val res = api.getStores()

            if (res.status == "ok" && res.data != null) {
                val allStores = res.data.map { dto ->
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

                storeList = allStores.filter {
                    it.storeName.contains(keyword) ||
                            it.address.contains(keyword)
                }
            } else {
                errorMessage = res.message ?: "検索結果の取得に失敗しました。"
            }
        } catch (e: Exception) {
            errorMessage = "通信エラーが発生しました。"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("検索結果：$keyword") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                }
            }

            storeList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("該当する店舗が見つかりませんでした。")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(storeList) { store ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("store_detail/${store.storeId}")
                                }
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(store.storeName, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(store.address, style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Divider()
                    }
                }
            }
        }
    }
}
