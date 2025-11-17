// =========================================================
// File: StoreSelectScreen.kt
// 概要: 地域・都道府県を選択し、店舗一覧へ進むための画面。
//設計書ID: なし（追加機能）
//画面名: 店舗一覧選択画面
// 更新者: 小林さん
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes

/**
 * StoreSelectScreen
 * 🇯🇵 店舗選択画面（統合版）
 * 🇨🇳 店铺选择界面（整合现在地搜索 + 区域按钮）
 *
 * - 搜索栏
 * - 現在地から検索（GPS）
 * - 地域から選択（北海道 / 東北 / 関東...）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSelectScreen(
    navController: NavController
) {
    val regions = StoreDataRepository.regionMap.keys.toList()       // ["hokkaido", "tohoku", ...]
    val regionDisplayNames = mapOf(
        "hokkaido" to "北海道",
        "tohoku" to "東北",
        "kanto" to "関東",
        "chubu" to "中部",
        "kinki" to "近畿",
        "chugoku" to "中国",
        "shikoku" to "四国",
        "kyushu" to "九州"
    )

    // ★ 彩色背景（淡色）
    val regionColors = mapOf(
        "hokkaido" to Color(0xFF90CAF9),  // 蓝
        "tohoku" to Color(0xFFA5D6A7),    // 绿
        "kanto" to Color(0xFFFFCC80),     // 橙
        "chubu" to Color(0xFFEF9A9A),     // 红
        "kinki" to Color(0xFFCE93D8),     // 紫
        "chugoku" to Color(0xFF80CBC4),   // 青
        "shikoku" to Color(0xFFFFF59D),   // 黄
        "kyushu" to Color(0xFFF48FB1)     // 粉
    )

    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("店舗選択") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // -----------------------------
            // 🔍 搜索栏
            // -----------------------------
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("店舗名・住所で検索") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    navController.navigate("${Routes.STORE_LIST}/search?query=${searchText}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("検索")
            }

            Divider()

            // -----------------------------
            // 📍 現在地から検索（GPS）
            // -----------------------------
            Button(
                onClick = {
                    navController.navigate(Routes.GPS_PERMISSION)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = "nearby")
                Spacer(Modifier.width(8.dp))
                Text("現在地から検索")
            }

            Divider()

            // -----------------------------
            // 🗾 地域から選択（新版 A2 彩色按钮）
            // -----------------------------
            Text("地域から選択", style = MaterialTheme.typography.titleMedium)

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),   // ★ 2 列网格
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(regions) { regionKey ->
                    val name = regionDisplayNames[regionKey] ?: regionKey
                    val bg = regionColors[regionKey] ?: Color(0xFFE0E0E0)

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = bg
                        ),
                        onClick = {
                            navController.navigate("${Routes.STORE_REGION}/$regionKey")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
