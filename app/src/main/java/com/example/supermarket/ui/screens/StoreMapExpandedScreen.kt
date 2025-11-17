package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.supermarket.models.Store

/**
 * StoreMapExpandedScreen
 * 🇯🇵 都道府県に属する店舗一覧
 * 🇨🇳 某都道府县下所有店铺列表
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
                title = { Text("${regionName} の店舗一覧") },
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
            items(stores.size) { index ->
                val store = stores[index]

                Card(
                    onClick = { onStoreClick(store.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(store.name, style = MaterialTheme.typography.titleMedium)
                        Text(store.address, style = MaterialTheme.typography.bodyMedium)
                        Text("営業時間: ${store.openHours}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
