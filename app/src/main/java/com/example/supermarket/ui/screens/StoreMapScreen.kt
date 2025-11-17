package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository

/**
 * StoreMapScreen
 * 🇯🇵 店舗選択（地図画面・地域一覧）
 * 🇨🇳 店铺选择（地图入口 + 区域列表）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreMapScreen(
    navController: NavController,
    onRegionClick: (String) -> Unit,
    onNearbyClick: () -> Unit
) {
    val regions = StoreDataRepository.getAllRegions()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("店舗選択（地図）") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 現在地から探す / 从当前位置找店铺
            Button(
                onClick = onNearbyClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = "nearby")
                Spacer(Modifier.width(8.dp))
                Text("現在地から探す")
            }

            Divider()

            Text(
                text = "地域から探す",
                style = MaterialTheme.typography.titleMedium
            )

            // 地域カード / 区域卡片
            regions.forEach { region ->
                Card(
                    onClick = { onRegionClick(region) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = region.uppercase(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}
