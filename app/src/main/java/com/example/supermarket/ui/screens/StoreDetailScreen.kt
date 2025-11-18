// =========================================================
// File: StoreDetailScreen.kt
// 設計書ID: store_detail
// 画面名: 店舗詳細画面
// 役割:
//   - 店舗の写真・名称・住所を表示する。
//   - 「店内マップを見る」で StoreMapScreen へ遷移。
//   - 「商品一覧を見る」で MenuScreen へ遷移。
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes
import com.example.supermarket.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    navController: NavController,
    storeId: String
) {
    val store = StoreDataRepository.getStore(storeId)

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
    ) { padding ->

        if (store == null) {
            Box(modifier = Modifier.padding(padding)) {
                Text("店舗情報が見つかりません。")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 店舗外観画像（占位画像）
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "店舗外観",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Text(text = "店舗名：${store.storeName}", style = MaterialTheme.typography.titleMedium)
            Text(text = "住所：${store.address}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("${Routes.STORE_MAP_EXPANDED}/${store.storeId}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("店内マップを見る")
            }

            Button(
                onClick = {
                    navController.navigate("${Routes.MENU}/${store.storeId}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("商品一覧を見る")
            }
        }
    }
}
