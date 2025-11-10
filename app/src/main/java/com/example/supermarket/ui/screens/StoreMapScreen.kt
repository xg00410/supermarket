package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.supermarket.data.FakeRepository

/**
 * 🗾 地图区域选择画面
 * 日本地図から地域を選ぶ画面（例：北海道・関東など）
 * -------------------------------------------------------
 * 功能说明（中日对照）：
 * ・显示六大区域按钮（北海道、東北、関東、中部、近畿、九州）
 * ・上方搜索栏可输入“东京 / 東京 / イオン / 超市名”进行模糊搜索
 * ・系统会自动判断对应区域并仅显示匹配结果
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapScreen(
    onRegionClick: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    // 🌏 区域对应表（日文→英文Key）
    val regionList = listOf(
        "北海道" to "hokkaido",
        "東北" to "tohoku",
        "関東" to "kanto",
        "中部" to "chubu",
        "近畿" to "kinki",
        "九州" to "kyushu"
    )

    // 🔍 搜索逻辑（模糊匹配都道府县或店铺名）
    val filteredRegions = remember(searchText) {
        if (searchText.isBlank()) {
            regionList
        } else {
            val keyword = searchText.trim()
            val matchedRegions = mutableSetOf<String>()

            // 1️⃣ 根据都道府县名判断归属区域
            FakeRepository.regionMap.forEach { (regionKey, prefectures) ->
                if (prefectures.any { it.contains(keyword, ignoreCase = true) }) {
                    matchedRegions.add(regionKey)
                }
            }

            // 2️⃣ 根据店铺名或地址匹配
            FakeRepository.getStores().forEach { store ->
                if (store.name.contains(keyword, ignoreCase = true) ||
                    store.address.contains(keyword, ignoreCase = true)
                ) {
                    // 地址中包含都道府县名 → 查归属区域
                    FakeRepository.regionMap.forEach { (regionKey, prefectures) ->
                        if (prefectures.any { store.address.contains(it) }) {
                            matchedRegions.add(regionKey)
                        }
                    }
                }
            }

            // 返回匹配区域按钮
            regionList.filter { matchedRegions.contains(it.second) }
        }
    }

    // 🧭 UI部分
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("日本地図から店舗を選ぶ") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔍 搜索框 / 検索ボックス
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("地域名・都道府県・店舗名で検索") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "地域を選択してください",
                style = MaterialTheme.typography.titleMedium
            )

            // ✅ 区域按钮展示（两列布局）
            if (filteredRegions.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (chunk in filteredRegions.chunked(2)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            chunk.forEach { (jpName, key) ->
                                RegionBox(
                                    name = jpName,
                                    color = getRegionColor(key),
                                    onClick = { onRegionClick(key) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (chunk.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            } else {
                // ❌ 未找到匹配
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("該当する地域が見つかりません。")
                }
            }
        }
    }
}

/**
 * 🎨 区域按钮控件 / 地域ボックス
 * 点击后跳转对应区域
 */
@Composable
fun RegionBox(
    name: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(120.dp)
            .background(color, MaterialTheme.shapes.medium)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}

/**
 * 🌈 区域对应颜色 / 各地域の色定義
 */
private fun getRegionColor(key: String): Color {
    return when (key) {
        "hokkaido" -> Color(0xFF90CAF9)
        "tohoku" -> Color(0xFFA5D6A7)
        "kanto" -> Color(0xFFFFF59D)
        "chubu" -> Color(0xFFFFCC80)
        "kinki" -> Color(0xFFE6EE9C)
        "kyushu" -> Color(0xFFCE93D8)
        else -> Color.LightGray
    }
}
