// =========================================================
// File: StoreRegionScreen.kt
// 概要: 日本の地域区分を表示し、ユーザーがエリアを選択する画面。
//設計書ID: なし（追加機能）
//画面名: 地域選択画面
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.supermarket.data.StoreDataRepository

/**
 * StoreRegionScreen — 无 ListItem 版本（兼容所有 Compose 版本）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegionScreen(
    regionName: String,
    onPrefectureClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val prefectures = StoreDataRepository.getPrefecturesByRegion(regionName)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("都道府県一覧") },
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "地域：${regionName.uppercase()}",
                style = MaterialTheme.typography.titleMedium
            )

            Divider()

            // ⭐ 手写列表项（不会报红、兼容所有版本）
            prefectures.forEach { pref ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPrefectureClick(pref) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = pref,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = null
                        )
                    }
                }

                Divider()
            }
        }
    }
}
