package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.FakeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegionScreen(
    regionName: String,
    onPrefectureClick: (String) -> Unit,
    onBack: () -> Unit
) {
    // 🌏 英文→日本語 地域名マップ / 英→日 区域映射
    val regionJpName = when (regionName) {
        "hokkaido" -> "北海道"
        "tohoku" -> "東北"
        "kanto" -> "関東"
        "chubu" -> "中部"
        "kinki" -> "近畿"
        "kyushu" -> "九州"
        else -> regionName
    }

    // 🔹 対応する都道府県リストを取得 / 获取该区域下的都道府县
    val prefectures = FakeRepository.getPrefecturesByRegion(regionName)

    var searchText by remember { mutableStateOf("") }

    // 🔍 検索フィルタ / 检索过滤逻辑
    val filteredPrefectures = remember(searchText) {
        if (searchText.isBlank()) prefectures
        else prefectures.filter { it.contains(searchText, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("${regionJpName}地域の都道府県一覧") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("戻る", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 🔍 検索ボックス / 搜索框
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("都道府県を検索") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "以下の都道府県から店舗を選択してください",
                style = MaterialTheme.typography.titleMedium
            )

            // 🔹 都道府県リスト表示 / 都道府县列表显示
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredPrefectures) { prefecture ->
                    Button(
                        onClick = { onPrefectureClick(prefecture) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(prefecture, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            // 🔹 結果なしメッセージ / 无结果提示
            if (filteredPrefectures.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("該当する都道府県が見つかりません。")
                }
            }
        }
    }
}
