// =========================================================
// File: StoreMapExpandedScreen.kt
// 設計書ID: store_拡大_map
// 画面名: 店内マップ拡大画面
// 役割:
//   - 店舗の店内平面図（floorMapRes）を大きく表示する。
//   - 将来的にエリア別のゾーン（A〜F）を重ねて表示する土台となる画面。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.R
import com.example.supermarket.data.StoreDataRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapExpandedScreen(
    navController: NavController,
    storeId: String
) {
    val store = StoreDataRepository.getStoreById(storeId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店内マップ") },
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
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = store?.storeName ?: "店舗名不明",
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = store?.floorMapRes ?: R.drawable.logo),
                    contentDescription = "店内マップ",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
