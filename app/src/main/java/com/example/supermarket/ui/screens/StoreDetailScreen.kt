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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    storeId: String,
    onBack: () -> Unit,
    onViewStoreMap: () -> Unit,
    onViewMenu: () -> Unit
)
{
    val store = StoreDataRepository.getStoreById(storeId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(store?.storeName ?: "店舗詳細") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->

        if (store == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("店舗情報が見つかりません。")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 店舗外観写真（なければプレースホルダ）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                if (store.imageRes != null) {
                    Image(
                        painter = painterResource(id = store.imageRes),
                        contentDescription = store.storeName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // 画像未設定の場合の簡易表示
                    Text("店舗画像は未設定です")
                }
            }

            // 店舗名
            Text(
                text = store.storeName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // 住所
            Text(
                text = "住所：${store.address}",
                style = MaterialTheme.typography.bodyMedium
            )

            // ※ 必要なら営業時間・電話番号などを追加可能
            // Text("営業時間：10:00〜21:00")
            // Text("電話番号：03-xxxx-xxxx")

            Spacer(modifier = Modifier.height(8.dp))

            // 店舗内マップ・商品一覧ボタン
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // 店舗内マップ（平面図）へ
                Button(
                    onClick = {
                        // storeId を一緒に渡して店内マップ画面で平面図を表示
                        onViewStoreMap()

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("店舗内マップを見る")
                }

                // 商品一覧（menu）へ
                Button(
                    onClick = {
                        // menu 画面側で storeId を受け取り、その店舗の商品一覧を表示
                        onViewMenu()

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("商品一覧を見る")
                }
            }
        }
    }
}
