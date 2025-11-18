package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorePrefectureScreen(
    navController: NavController,
    regionId: Int
) {
    val prefectures = remember(regionId) {
        StoreDataRepository.getPrefecturesByRegion(regionId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("都道府県を選択") },
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

            if (prefectures.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("この地域には都道府県データがありません。")
                }
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(prefectures) { pref ->

                    Button(
                        onClick = {
                            navController.navigate(
                                Routes.STORE_RESULT +
                                        "/keyword=none/pref=" + pref.prefectureId
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(pref.prefectureName)
                    }
                }
            }
        }
    }
}
