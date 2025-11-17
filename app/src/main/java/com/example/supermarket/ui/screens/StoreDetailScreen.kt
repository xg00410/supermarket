// =========================================================
// File: StoreDetailScreen.kt
// 設計書ID: store_拡大
// 画面名: 店舗拡大画面
// 役割:
//   - 店舗の外観写真・名称・住所などの詳細情報を表示する。
//   - 「店舗内マップを見る」ボタンから店内平面図画面へ遷移。
//   - 「商品一覧を見る」ボタンから menu 画面へ遷移。
//   - 戻るボタンで一つ前の画面に戻る。
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.supermarket.R
import com.example.supermarket.data.StoreDataRepository

// 設計書ID: store_拡大
// 画面名: 店舗詳細画面
// 機能: 店舗の詳細情報（名称・住所・営業時間・店舗画像など）を表示し、
//       「店舗内マップを見る」「商品一覧を見る」ボタンからそれぞれの画面へ遷移する。
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    storeId: String,
    onBack: () -> Unit,
    onViewStoreMap: () -> Unit, // 店舗内マップ画面へ / 前往店内平面图画面
    onViewMenu: () -> Unit      // 商品一覧画面へ / 前往商品列表画面
) {
    val store = StoreDataRepository.getStoreById(storeId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(store?.storeName ?: "店舗詳細") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る / 返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 店舗画像 / 店铺图片
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                // 実際のアプリでは store.storeImageUrl などを利用して表示 / 实际应用中用 URL
                Image(
                    painter = painterResource(//android.R.drawable.ic_menu_gallery),
                        R.drawable.logo),
                    contentDescription = "店舗画像 / 店铺图片",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 店舗情報 / 店铺信息
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = store?.storeName ?: "店舗名不明",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(text = store?.address ?: "住所未設定 / 地址未设置")

            }

            Spacer(modifier = Modifier.height(16.dp))

            // 「店舗内マップを見る」「商品一覧を見る」ボタン
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onViewStoreMap,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("店舗内マップを見る")
                }

                Button(
                    onClick = onViewMenu,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("商品一覧を見る")
                }
            }
        }
    }
}
