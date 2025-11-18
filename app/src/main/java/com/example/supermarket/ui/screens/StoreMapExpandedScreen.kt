// =========================================================
// File: StoreMapExpandedScreen.kt
// 設計書ID: store_map_expanded
// 画面名: 店舗内マップ（拡大表示）
// 役割:
//   - 店舗内の平面図（剖面図）を拡大表示する画面。
//   - 戻るボタンで前画面（店舗拡大画面）に戻る。
//   - 「商品一覧へ」ボタンで menu 画面へ遷移。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import com.example.supermarket.data.StoreDataRepository
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapExpandedScreen(
    navController: NavController,
    storeId: String
) {
    val store = StoreDataRepository.getStoreById(storeId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("店内マップ") },
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
                .padding(padding)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            store?.let { s ->
                s.floorMapRes?.let { mapRes ->
                    Image(
                        painter = painterResource(id = mapRes),
                        contentDescription = "map",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("店舗名：${s.storeName}")
                Text("住所：${s.address}")
            } ?: run {
                Text("店舗情報が見つかりません。")
            }
        }
    }}