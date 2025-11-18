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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes

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
                title = { Text("店舗内マップ") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentAlignment = Alignment.Center
            ) {
                val mapRes = store.floorMapRes ?: R.drawable.logo
                Image(
                    painter = painterResource(id = mapRes),
                    contentDescription = "floor map",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Text(store.storeName, style = MaterialTheme.typography.titleMedium)
            Text(store.address, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("${Routes.MENU}/$storeId")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("商品一覧へ")
            }
        }
    }
}