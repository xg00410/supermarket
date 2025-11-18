// =========================================================
// File: StoreDetailScreen.kt
// 店舗詳細画面（store_拡大）
// 更新者: 郭（修正版）
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
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.data.StoreDataRepository

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
                            contentDescription = "back"
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

            // 店舗外観（统一使用 logo 占位）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
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

            // --- ボタン群 ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // 店内マップへ
                Button(
                    onClick = {
                        navController.navigate("store_map_expanded/$storeId")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("店内マップを見る")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 商品一覧へ
                Button(
                    onClick = {
                        navController.navigate("menu/$storeId")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("商品一覧を見る")
                }
            }
        }
    }
}
