// =========================================================
// File: StoreSelectScreen.kt
// 設計書ID: store_select
// 画面名: 店舗選択画面
// 役割:
//   - 店舗名／住所を入力して検索する。
//   - 検索結果として店舗リストを表示する。
//   - 店舗カードをタップすると「店舗拡大画面(StoreDetail)」へ遷移する。
//   - また、都道府県から選ぶ導線として「都道府県ボタン」を簡易表示する。
// 備考:
//   - store_result 画面は設けず、本画面内で検索と結果表示を完結させる。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSelectScreen(
    navController: NavController
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var committedKeyword by remember { mutableStateOf("") }

    // 全店舗一覧
    val allStores = StoreDataRepository.getAllStores()

    // 検索キーワードに基づく絞り込み
    val filteredStores = remember(committedKeyword) {
        if (committedKeyword.isBlank()) {
            allStores
        } else {
            allStores.filter { store ->
                store.storeName.contains(committedKeyword, ignoreCase = true) ||
                        store.address.contains(committedKeyword, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店舗選択") }
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

            // 検索ボックス
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("店舗名・住所で検索") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            committedKeyword = searchText.text
                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "検索")
                    }
                }
            )

            // 都道府県ボタン（例として一部のみ列挙）
            Text(
                text = "都道府県から選ぶ",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val prefectures = listOf("東京都", "神奈川県", "千葉県", "埼玉県")
                prefectures.forEach { pref ->
                    AssistChip(
                        onClick = {
                            navController.navigate("${Routes.STORE_REGION}/$pref")
                        },
                        label = { Text(pref) }
                    )
                }
            }

            Divider()

            // 検索結果リスト
            if (filteredStores.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("該当する店舗がありません。")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredStores, key = { it.storeId }) { store ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("${Routes.STORE_DETAIL}/${store.storeId}")
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = store.storeName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = store.address,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
