package com.example.supermarket.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.supermarket.R
import com.example.supermarket.data.FakeRepository
import com.example.supermarket.model.StoreItem

/**
 * 🏬 StoreMapExpandedScreen.kt
 * -----------------------------------------------------
 * 📘 店舗一覧画面 / 店铺列表界面
 * -----------------------------------------------------
 * 🇯🇵 指定地域内の店舗一覧を表示し、検索で絞り込み可能。
 * 🇨🇳 显示特定区域内的所有店铺，可通过关键词检索。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapExpandedScreen(
    regionName: String,
    stores: List<StoreItem>,
    onStoreClick: (String) -> Unit,
    onBack: () -> Unit
) {
    // 🌏 英文 → 日本语 地区映射
    val regionJpName = when (regionName) {
        "hokkaido" -> "北海道"
        "tohoku" -> "東北"
        "kanto" -> "関東"
        "chubu" -> "中部"
        "kinki" -> "近畿"
        "kyushu" -> "九州"
        else -> regionName
    }

    var searchText by remember { mutableStateOf("") }

    // 🔍 検索フィルター / 搜索过滤逻辑
    val filteredStores = remember(searchText, stores) {
        if (searchText.isBlank()) stores
        else {
            val keyword = searchText.trim()
            val result = mutableSetOf<StoreItem>()

            stores.forEach { store ->
                if (store.name.contains(keyword, ignoreCase = true) ||
                    store.address.contains(keyword, ignoreCase = true)
                ) {
                    result.add(store)
                }
            }

            // 追加: 全域検索 (跨县匹配)
            FakeRepository.getStores().forEach { store ->
                if (store.name.contains(keyword, ignoreCase = true) ||
                    store.address.contains(keyword, ignoreCase = true)
                ) {
                    result.add(
                        StoreItem(
                            id = store.id,
                            name = store.name,
                            address = store.address,
                            imageRes = R.drawable.ic_store_placeholder
                        )
                    )
                }
            }
            result.toList()
        }
    }

    // 🧭 画面レイアウト / 界面布局
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("${regionJpName} の店舗一覧") },
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 🔍 搜索栏
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("都道府県・市・店舗名で検索") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // 🏪 店舗リスト / 店铺列表
            if (filteredStores.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredStores) { store ->
                        StoreListCard(store = store, onClick = { onStoreClick(store.id) })
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("該当する店舗が見つかりません。")
                }
            }
        }
    }
}

/**
 * 🏪 店舗カード / 店铺卡片组件
 */
@Composable
fun StoreListCard(
    store: StoreItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 🖼 画像 / 图片区域
            val imageResId = if (store.imageRes != 0) store.imageRes else R.drawable.ic_store_placeholder

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = store.name,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // 📋 店舗情報 / 店铺信息
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(store.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(store.address, fontSize = 14.sp, color = Color.Gray)

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("店舗へ")
                    }
                }
            }
        }
    }
}
