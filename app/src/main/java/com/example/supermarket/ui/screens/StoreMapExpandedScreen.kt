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

data class StoreItem(
    val id: String,
    val name: String,
    val address: String,
    val imageRes: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapExpandedScreen(
    regionName: String,
    stores: List<StoreItem>,
    onStoreClick: (String) -> Unit,
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

    var searchText by remember { mutableStateOf("") }

    // 🔍 検索フィルタ / 模糊搜索：店名・住所・地域
    val filteredStores = remember(searchText, stores) {
        if (searchText.isBlank()) stores
        else stores.filter {
            it.name.contains(searchText, ignoreCase = true) ||
                    it.address.contains(searchText, ignoreCase = true) ||
                    regionJpName.contains(searchText, ignoreCase = true)
        }
    }

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
            // 🔍 検索ボックス / 搜索框
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("店舗名・住所・地域名で検索") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (filteredStores.isEmpty()) {
                // ❌ 該当なし / 无匹配结果
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("該当する店舗が見つかりません。")
                }
            } else {
                // ✅ 店舗リスト / 店铺列表
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredStores) { store ->
                        StoreListCard(store = store, onClick = { onStoreClick(store.id) })
                    }
                }
            }
        }
    }
}

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
            // 🖼 画像表示エリア / 图片区域
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
