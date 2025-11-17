package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.StoreDataRepository

/**
 * StoreDetailScreen
 * 🇯🇵 店舗詳細画面
 * 🇨🇳 店铺详细画面
 *
 * - 店舗名 / 地址 / 営業時間
 * - 「この店舗の商品を確認する」→ MenuScreen へ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    storeId: String,
    onGoMenu: () -> Unit,
    onBack: () -> Unit
) {
    val store = StoreDataRepository.getStoreById(storeId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("店舗詳細") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->

        if (store == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("店舗情報が見つかりません。")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(store.name, style = MaterialTheme.typography.headlineSmall)
            Text("住所：${store.address}")
            Text("営業時間：${store.openHours}")

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onGoMenu,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("この店舗の商品を確認する")
            }
        }
    }
}
