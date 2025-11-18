// =========================================================
// File: StoreSelectScreen.kt
// 設計書ID: store_select
// 画面名: 店舗選択（第1階層）
// 役割:
//   - 日本の7地域（関東・東北など）を表示する。
//   - 地域を選択すると AreaLevel1Screen（都道府県選択）へ遷移。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

// 7地域
private val regions = listOf(
    "北海道", "東北", "関東", "中部", "近畿", "中国・四国", "九州・沖縄"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSelectScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("店舗選択（地域）") })
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(regions) { region ->

                Button(
                    onClick = {
                        navController.navigate("${Routes.AREA_LEVEL1}/$region")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text(region)
                }
            }
        }
    }
}
