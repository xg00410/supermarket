package com.example.supermarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.FakeRepository
import com.example.supermarket.model.Store

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    storeId: String,
    onBack: () -> Unit,
    onGoMenu: (String) -> Unit
) {
    val store: Store? = remember(storeId) {
        FakeRepository.getStoreById(storeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店舗詳細") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("戻る")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (store == null) {
            // 错误情况：如果传进来的storeId找不到
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("店舗情報が見つかりません")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 店舗名 + 住所
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = store.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = store.address,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "営業時間: ${store.openHours}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 平面図 / フロアマップ枠 (占位)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "店舗マップ",
                    style = MaterialTheme.typography.titleMedium
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ここに店舗の平面図を表示\n(拡大表示や棚の位置など)",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }

            // ボタン：商品を見る (Menu画面に行く)
            Button(
                onClick = { onGoMenu(store.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("この店舗の商品を確認する")
            }

            // TODO:
            // 后面我们会在这里加：
            // - 店铺内「検索バー」
            // - 「在庫ありの人気商品TOP5」
            // - 「カートへ追加した商品の現在地へ誘導」 等等
        }
    }
}
