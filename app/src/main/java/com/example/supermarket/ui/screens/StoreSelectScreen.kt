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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSelectScreen(
    onStoreClick: (String) -> Unit,
    onSearchSubmit: (String) -> Unit,
    onNearbyClick: () -> Unit
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val allStores = remember { FakeRepository.getStores() }
    // 简单过滤：名字或地址包含关键字
    val filteredStores by remember(searchText) {
        mutableStateOf(
            if (searchText.text.isBlank()) {
                allStores
            } else {
                allStores.filter {
                    it.name.contains(searchText.text, ignoreCase = true) ||
                            it.address.contains(searchText.text, ignoreCase = true)
                }
            }
        )
    }

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

            // 🔍 搜索+附近按钮 行
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
                    placeholder = { Text("店舗名 / 住所で検索") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalButton(
                    onClick = {
                        onSearchSubmit(searchText.text)
                        // 暂时我们本地filter就够了，不一定要额外交互
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("検索")
                }
            }

            // 📍 最近の店舗ボタン
            FilledTonalButton(
                onClick = { onNearbyClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.GpsFixed, contentDescription = "nearby")
                Spacer(modifier = Modifier.width(8.dp))
                Text("現在地の近くの店舗を探す")
            }


            // 店舗リスト
            Text(
                text = "店舗一覧",
                style = MaterialTheme.typography.titleMedium
            )

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
                        color = Color(0xFF388E3C) // 深绿色系
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
