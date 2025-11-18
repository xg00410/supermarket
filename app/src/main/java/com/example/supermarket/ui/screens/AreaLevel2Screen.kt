// =========================================================
// File: AreaLevel2Screen.kt
// 第2階層エリア選択画面（SimpleListItem 版）
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes
import com.example.supermarket.ui.components.SimpleListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaLevel2Screen(navController: NavController) {

    val prefectures = listOf("東京都", "神奈川県", "千葉県")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("エリア選択（第2階層）") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            prefectures.forEach { pref ->
                SimpleListItem(
                    text = pref,
                    onClick = {
                        navController.navigate(Routes.STORE_SELECT)
                    }
                )
            }
        }
    }
}
