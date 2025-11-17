// =========================================================
// File: StoreMapScreen.kt
// 設計書ID: store_Current
// 画面名: 店舗地図検索画面
// 役割:
//   - 店舗位置（緯度・経度）を地図上に表示。
//   - 店舗名ラベルをタップすると店舗拡大画面へ遷移。
//   - 下部には店舗一覧を表示し、一覧からも詳細へ遷移可能。
//   - GPSボタンは簡易実装（現在地機能の位置付け）。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    // GPS 簡易状態
    var gpsActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("店舗地図") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // =========================================================
            // 地図エリア（ダミー表示）
            // =========================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.TopStart
            ) {

                // 店舗ラベル（地図上の簡易表示）
                stores.forEach { store ->
                    Column(
                        modifier = Modifier
                            .padding(
                                start = (store.longitude % 1 * 200).dp,
                                top = (store.latitude % 1 * 200).dp
                            )
                            .clickable {
                                navController.navigate("${Routes.STORE_DETAIL}/${store.storeId}")
                            }
                    ) {
                        Surface(
                            color = Color.White,
                            shadowElevation = 4.dp,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = store.storeName,
                                modifier = Modifier.padding(6.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // GPS ボタン
                IconButton(
                    onClick = { gpsActive = !gpsActive },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                ) {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = "gps",
                        tint = if (gpsActive)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // =========================================================
            // 店舗一覧（リスト）
            // =========================================================
            Text(
                "店舗一覧",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stores) { store ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("${Routes.STORE_DETAIL}/${store.storeId}")
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(store.storeName, style = MaterialTheme.typography.titleMedium)
                            Text("住所：${store.address}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
