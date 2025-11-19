package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreResultScreen(
    navController: NavController,
    keyword: String?,
    prefectureId: Int?
) {
    // 第一段：都道府県 or キーワードで初期リストを決定
    val originalList = remember(keyword, prefectureId) {
        when {
            prefectureId != null && prefectureId > 0 ->
                StoreDataRepository.getStoresByPrefecture(prefectureId)
            keyword != null && keyword.isNotBlank() && keyword != "none" ->
                StoreDataRepository.searchStores(keyword)
            else -> emptyList()
        }
    }

    var filterText by remember { mutableStateOf("") }

    // 第二段：結果内絞り込み
    val filteredList = remember(filterText, originalList) {
        if (filterText.isBlank()) {
            originalList
        } else {
            originalList.filter {
                it.storeName.contains(filterText, ignoreCase = true) ||
                        it.address.contains(filterText, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店舗一覧") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = filterText,
                onValueChange = { filterText = it },
                label = { Text("結果内検索") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredList.isEmpty()) {
                Text("該当する店舗はありません。")
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList) { store ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = {
                            navController.navigate(
                                Routes.STORE_DETAIL + "/" + store.storeId
                            )
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = store.storeName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(text = store.address)
                        }
                    }
                }
            }
        }
    }
}
