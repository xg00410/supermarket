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
//   - 画像は全店舗共通のダミー画像（logo）を使用。
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
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    navController: NavController,
    storeId: String
) {
    val store = StoreDataRepository.getStoreById(storeId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(store?.storeName ?: "店舗詳細") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
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

            // 店舗外観画像（全店舗共通のダミー画像）
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = store?.imageRes ?: R.drawable.logo),
                    contentDescription = "店舗画像",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // 店舗情報
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = store?.storeName ?: "店舗名不明",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(text = store?.address ?: "住所未設定")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ボタン群
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // 店内マップへ
                Button(
                    onClick = {
                        navController.navigate("${Routes.STORE_MAP_EXPANDED}/$storeId")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("店舗内マップを見る")
                }

                // 商品一覧へ
                Button(
                    onClick = {
                        navController.navigate("${Routes.MENU}/$storeId")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("商品一覧を見る")
                }
            }
        }
    }
}
