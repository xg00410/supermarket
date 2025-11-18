// =========================================================
// File: StoreMapExpandedScreen.kt
// 設計書ID: store_map_expanded
// 画面名: 店内マップ拡大画面
// 役割:
//   - 店舗の平面図を拡大表示する。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapExpandedScreen(
    navController: NavController,
    storeId: String
) {
    val store = StoreDataRepository.getStore(storeId)
    val scroll = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店内マップ（拡大）") },
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
                .verticalScroll(scroll)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = store.floorMapRes),
                contentDescription = "店内マップ拡大",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
