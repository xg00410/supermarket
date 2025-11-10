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
 * 🏬 店舗一覧画面 / 店铺列表界面
 * -----------------------------------------------------
 * 功能说明（中日对照）：
 * ・显示某都道府县内的所有店铺
 * ・上方搜索栏支持输入「県名・市名・店舗名」模糊搜索
 * ・输入“イオン”“東京”等时也能匹配对应结果
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapExpandedScreen(
    regionName: String,
    stores: List<StoreItem>,
    onStoreClick: (String) -> Unit,
    onBack: () -> Unit
) {
    // 🌏 英文→日本语 地区映射 / 地域キーを日本語に変換
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

    // 🔍 智能搜索逻辑 / 検索強化版
    val filteredStores = remember(searchText, stores) {
        if (searchText.isBlank()) {
            stores
        } else {
            val keyword = searchText.trim()
            val result = mutableSetOf<StoreItem>()

            // 1️⃣ 店铺名、地址直接匹配
            stores.forEach { store ->
                if (store.name.contains(keyword, ignoreCase = true) ||
                    store.address.contains(keyword, ignoreCase = true)
                ) {
                    result.add(store)
                }
            }

            // 2️⃣ 根据 FakeRepository 检查其他区域的店铺（支持跨县搜索）
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

    // 🧭 画面结构 / 画面構成
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
            // 🔍 搜索栏 / 検索ボックス
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("都道府県・市・店舗名で検索") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // 🏬 店铺列表 / 店舗リスト
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
                // ❌ 没有匹配结果 / 該当なし
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
 * 🏪 店铺卡片组件 / 店舗カードコンポーネント
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
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 🖼 图片区域 / 画像エリア
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (store.imageRes != null && store.imageRes != 0) {
                    Image(
                        painter = painterResource(id = store.imageRes),
                        contentDescription = store.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("No\nImage", color = Color.DarkGray, fontSize = 12.sp)
                }
            }

            // 📋 店铺信息 / 店舗情報
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
