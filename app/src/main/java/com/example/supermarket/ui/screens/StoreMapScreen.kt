package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapScreen(
    onRegionClick: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    // 🌏 地域リスト（日文）/ 区域列表（日文）
    val regionList = listOf(
        "北海道" to "hokkaido",
        "東北" to "tohoku",
        "関東" to "kanto",
        "中部" to "chubu",
        "近畿" to "kinki",
        "九州" to "kyushu"
    )

    // 🔍 検索フィルタ / 搜索过滤逻辑
    val filteredRegions = remember(searchText) {
        if (searchText.isBlank()) regionList
        else regionList.filter { it.first.contains(searchText, ignoreCase = true) }
    }

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
            // 🔍 検索ボックス / 搜索框
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("地域名を検索（例：関東・九州）") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "地域を選択してください",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 🗾 簡易マップ / 简易地图布局
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ここでは2列ずつ並べる / 两列排列
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
                        // 奇数个时补空白 / 若为奇数则补空列
                        if (chunk.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            if (filteredRegions.isEmpty()) {
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
            .background(color, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

// 🎨 各地域のカラーを定義 / 各区域对应的颜色
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
