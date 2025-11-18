// =========================================================
// File: StoreMapScreen.kt
// 設計書ID: store_map
// 画面名: 店内マップ画面
// 役割:
//   - 店舗の平面図を表示。
//   - 「拡大表示」で StoreMapExpandedScreen に遷移。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes
import com.example.supermarket.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapScreen(
    navController: NavController,
    storeId: String
) {
    val store = StoreDataRepository.getStore(storeId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店内マップ") },
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Image(
                painter = painterResource(id = store.floorMapRes),
                contentDescription = "店内マップ",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Button(
                onClick = {
                    navController.navigate("${Routes.STORE_MAP_EXPANDED}/${store.storeId}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("拡大表示")
            }
        }
    }
}
