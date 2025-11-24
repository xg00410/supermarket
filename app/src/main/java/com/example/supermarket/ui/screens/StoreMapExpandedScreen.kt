// =========================================================
// File: StoreMapExpandedScreen.kt
// 設計書ID: store_拡大_map
// 画面名: 店内マップ拡大画面
// 役割:
//   - 店舗の店内平面図を大きく表示する。
//   - 将来的にエリア別ゾーン(A〜F)などを重ねて表示する土台画面。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.models.Store
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapExpandedScreen(
    navController: NavController,
    storeId: String
) {
    var store by remember { mutableStateOf<Store?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 店舗情報取得（タイトル表示用）
    LaunchedEffect(storeId) {
        isLoading = true
        errorMessage = null
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
                store = allStores.firstOrNull { it.storeId == storeId }
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
                title = { Text(store?.storeName ?: "店内マップ") },
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

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 今はダミーの店内マップ画像を表示
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.store_floor_map),
                            contentDescription = "店内マップ",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    // 将来的なエリア情報などの説明欄（プレースホルダー）
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "※ 今後ここに「Aゾーン：飲料」「Bゾーン：菓子」などの情報を表示予定。",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
