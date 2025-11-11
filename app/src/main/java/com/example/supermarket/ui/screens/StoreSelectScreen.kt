package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.FakeRepository
import com.example.supermarket.ui.components.MainScaffold
import com.example.supermarket.ui.Routes

/**
 * 🏪 店舗選択画面 / 店铺选择画面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSelectScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    val stores = remember { FakeRepository.getStores() }

    MainScaffold(navController = navController, title = "店舗選択") { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("店舗名または住所を入力") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            FilledTonalButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.GpsFixed, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("現在地の近くの店舗を探す")
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(stores.filter { it.name.contains(searchText) || it.address.contains(searchText) }) { store ->
                    TextButton(onClick = {
                        navController.navigate("${Routes.STORE_DETAIL}?storeId=${store.id}")
                    }) {
                        Text(store.name)
                    }
                }
            }
        }
    }
}
