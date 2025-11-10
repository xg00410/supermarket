package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.FakeRepository
import com.example.supermarket.model.Store
import androidx.compose.material3.Icon
import androidx.navigation.NavController

/**
 * 🏪 店舗検索画面（現在地・キーワード対応）
 * 店铺选择画面（支持当前位置与关键词搜索）
 * -------------------------------------------------
 * 功能说明（中日对照）：
 * ・用户可通过输入「県名・市名・店舗名」进行搜索
 * ・系统自动判断输入内容匹配的店铺
 * ・可显示当前位置附近按钮（暂为占位）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSelectScreen(
    navController: NavController,
    onStoreClick: (String) -> Unit,
    onSearchSubmit: (String) -> Unit,
    onNearbyClick: () -> Unit
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val allStores = remember { FakeRepository.getStores() }

    // 🔍 智能搜索逻辑 / スマート検索ロジック
    val filteredStores = remember(searchText) {
        val keyword = searchText.text.trim()
        if (keyword.isBlank()) {
            allStores
        } else {
            val matched = mutableSetOf<Store>()

            // 1️⃣ 店铺名 / 地址直接匹配
            allStores.forEach { store ->
                if (store.name.contains(keyword, ignoreCase = true) ||
                    store.address.contains(keyword, ignoreCase = true)
                ) {
                    matched.add(store)
                }
            }

            // 2️⃣ 匹配区域或都道府县
            FakeRepository.regionMap.forEach { (regionKey, prefectures) ->
                if (prefectures.any { it.contains(keyword, ignoreCase = true) }) {
                    // 将属于该区域的所有店铺加入结果
                    matched.addAll(FakeRepository.getStoresByRegion(regionKey))
                }
            }

            matched.toList()
        }
    }

    // 🧭 UI结构 / 画面構成
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店舗選択") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 🔍 搜索栏 + 附近按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 56.dp),
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("都道府県 / 店舗名 / 住所で検索") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalButton(
                    onClick = {
                        onSearchSubmit(searchText.text)
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("検索")
                }
            }

            // 📍 附近店铺按钮 / 現在地検索ボタン
            FilledTonalButton(
                onClick = { onNearbyClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.GpsFixed, contentDescription = "nearby")
                Spacer(modifier = Modifier.width(8.dp))
                Text("現在地の近くの店舗を探す")
            }

            // 📋 店铺列表 / 店舗リスト
            Text(
                text = "店舗一覧",
                style = MaterialTheme.typography.titleMedium
            )

            if (filteredStores.isEmpty()) {
                // ❌ 无匹配结果 / 該当なし
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("該当する店舗が見つかりません。")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredStores) { store ->
                        StoreCard(
                            store = store,
                            onClick = { onStoreClick(store.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 🏬 店铺卡片组件 / 店舗カード
 */
@Composable
private fun StoreCard(
    store: Store,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = store.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = store.address,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "営業時間: ${store.openHours}",
                    style = MaterialTheme.typography.bodySmall
                )
                store.distanceMeters?.let {
                    Text(
                        text = "約${it}m",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF388E3C)
                    )
                }
            }
            Text(
                text = "タップしてこの店舗を選択 ▶",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
