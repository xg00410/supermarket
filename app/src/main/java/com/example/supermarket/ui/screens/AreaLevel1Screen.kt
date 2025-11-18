// =========================================================
// File: AreaLevel1Screen.kt
// 第1階層エリア選択画面（SimpleListItem 版）
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
fun AreaLevel1Screen(navController: NavController) {

    val areas = listOf("北海道・東北", "関東", "中部", "近畿", "中国", "四国", "九州・沖縄")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("エリア選択（第1階層）") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            areas.forEach { area ->
                SimpleListItem(
                    text = area,
                    onClick = {
                        navController.navigate(Routes.AREA_LEVEL2)
                    }
                )
            }
        }
    }
}
