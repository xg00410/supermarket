package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.data.StoreDataRepository

/**
 * StoreRegionScreen
 * 🇯🇵 地域に属する都道府県一覧
 * 🇨🇳 区域下的都道府县列表
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
                title = { Text("地域：${regionName.uppercase()}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(prefectures.size) { index ->
                Card(
                    onClick = { onPrefectureClick(prefectures[index]) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = prefectures[index],
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
