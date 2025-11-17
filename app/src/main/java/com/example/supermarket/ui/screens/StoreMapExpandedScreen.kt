// =========================================================
// File: StoreMapExpandedScreen.kt
// 概要: 店舗の拡大地図（剖面図）を表示する画面。
//設計書ID: Store
//画面名: 店舗フロアマップ拡大画面
// 更新者: 小林さん
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.models.Store

/**
 * 店舗一覧画面（都道府県 → 店鋪列表）
 * 🇯🇵 都道府県の全店舗一覧を表示
 * 🇨🇳 显示都道府县下的所有店铺
 *
 * UI 优化版（不加动画、功能优先）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapExpandedScreen(
    regionName: String,
    stores: List<Store>,
    onStoreClick: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("$regionName の店舗一覧") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (stores.isEmpty()) {
                Text(
                    "店舗が見つかりません。",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // ⭐ 逐个显示店铺
            stores.forEach { store ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStoreClick(store.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // 左侧：名称 + 地址 + 营业时间
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = store.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = store.address,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = "営業時間：${store.openHours}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // 右侧箭头
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
