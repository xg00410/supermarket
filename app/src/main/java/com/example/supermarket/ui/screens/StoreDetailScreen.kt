// =========================================================
// File: StoreDetailScreen.kt
// 設計書ID: store_拡大
// 画面名: 店舗拡大画面
// 役割:
//   - 店舗の外観画像・名称・住所を表示する。
//   - 「店舗内マップを見る」ボタンから店内平面図画面へ遷移。
//   - 「商品一覧を見る」ボタンから menu 画面へ遷移。
//   - 戻るボタンで一つ前の画面へ戻る。
// 備考:
//   - 店舗情報は SQL + PHP の API(get_stores.php) から取得する。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
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
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    navController: NavController,
    storeId: String
) {
    // 店舗情報状態
    var store by remember { mutableStateOf<Store?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // API から該当店舗情報を取得
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
                if (store == null) {
                    errorMessage = "店舗情報が見つかりません。"
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
                title = { Text("店舗詳細") },
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

            store == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("店舗情報がありません。")
                }
            }

            else -> {
                val s = store!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // TODO: 店舗外観画像を配置する場合はここに Image を追加する

                    Text(
                        text = s.storeName,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "住所：${s.address}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 店内マップへ
                    Button(
                        onClick = {
                            navController.navigate("store_map_expanded/${s.storeId}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("店舗内マップを見る")
                    }

                    // 商品一覧へ
                    Button(
                        onClick = {
                            // Routes.MENU の定義に合わせてパラメータ付きで遷移
                            navController.navigate("${Routes.MENU}/${s.storeId}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("商品一覧を見る")
                    }
                }
            }
        }
    }
}
