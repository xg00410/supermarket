// =========================================================
// File: StoreRegionScreen.kt
// 画面名: 店舗地域選択 → 選択した都道府県の店舗一覧表示
// 役割:
//   - 選択した都道府県(prefecture)に属する店舗一覧を API から取得する。
//   - UI レイアウトは既存コードに合わせて必要最小限のみ修正。
//   - 偽データ(StoreDataRepository.getAllStores) は一切使用しない。
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
fun StoreRegionScreen(
    navController: NavController,
    prefecture: String   // 例: "東京都"
) {
    // ------------------------------
    // 画面状態
    // ------------------------------
    var storeList by remember { mutableStateOf<List<Store>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // ------------------------------
    // 起動時に API から店舗一覧を取得
    // ------------------------------
    LaunchedEffect(prefecture) {
        isLoading = true
        errorMessage = null

        try {
            val api = ApiClient.retrofit.create(ApiService::class.java)
            val res = api.getStores()

            if (res.status == "ok" && res.data != null) {
                // DTO → Store モデルへの変換
                val all = res.data.map { dto ->
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

                // 都道府県名文字列に住所が含まれる店舗のみ抽出
                storeList = all.filter { it.address.contains(prefecture) }
            } else {
                errorMessage = res.message ?: "店舗情報の取得に失敗しました。"
            }
        } catch (e: Exception) {
            errorMessage = "店舗情報の取得中にエラーが発生しました。"
        } finally {
            isLoading = false
        }
    }

    // ------------------------------
    // UI レイアウト
    // ------------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店舗一覧：$prefecture") },
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
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                }
            }

            storeList.isEmpty() -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("この都道府県には登録店舗がありません。")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(storeList) { store ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // 店舗選択 → 店舗詳細画面へ
                                    navController.navigate("store_detail/${store.storeId}")
                                }
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(text = store.storeName, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = store.address, style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Divider()
                    }
                }
            }
        }
    }
}
