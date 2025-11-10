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

/**
 * 🗾 都道府県選択画面 / 県域选择画面
 * --------------------------------------------------------
 * 功能说明（中日对照）：
 * ・根据区域（region）显示该区域下的都道府县按钮
 * ・上方可搜索「県名・城市名・店铺名」
 * ・若输入店铺名或地址，系统会推断其所属的都道府县
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegionScreen(
    regionName: String,
    onPrefectureClick: (String) -> Unit,
    onBack: () -> Unit
) {
    // 🌏 区域英文→日文映射 / 英文キーを日本語名に変換
    val regionJpName = when (regionName) {
        "hokkaido" -> "北海道"
        "tohoku" -> "東北"
        "kanto" -> "関東"
        "chubu" -> "中部"
        "kinki" -> "近畿"
        "kyushu" -> "九州"
        else -> regionName
    }

    // 🔹 获取该区域的都道府县列表 / 対応する都道府県リストを取得
    val prefectures = FakeRepository.getPrefecturesByRegion(regionName)
    var searchText by remember { mutableStateOf("") }

    // 🔍 智能搜索逻辑 / 検索ロジック強化版
    val filteredPrefectures = remember(searchText) {
        if (searchText.isBlank()) {
            prefectures
        } else {
            val keyword = searchText.trim()
            val result = mutableSetOf<String>()

            // 1️⃣ 按都道府县名直接匹配
            result.addAll(prefectures.filter { it.contains(keyword, ignoreCase = true) })

            // 2️⃣ 按店铺名或地址匹配
            FakeRepository.getStores().forEach { store ->
                if (store.name.contains(keyword, ignoreCase = true) ||
                    store.address.contains(keyword, ignoreCase = true)
                ) {
                    // 若匹配到店铺 → 找出它所属的都道府县
                    prefectures.forEach { pref ->
                        if (store.address.contains(pref)) {
                            result.add(pref)
                        }
                    }
                }
            }

            result.toList()
        }
    }

    // 🧭 UI结构
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
            // 🔍 搜索栏 / 検索ボックス
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("都道府県・店舗名で検索") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "以下の都道府県から店舗を選択してください",
                style = MaterialTheme.typography.titleMedium
            )

            // ✅ 都道府县按钮列表 / ボタンリスト
            if (filteredPrefectures.isNotEmpty()) {
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
            } else {
                // ❌ 无结果提示 / 結果なしメッセージ
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
