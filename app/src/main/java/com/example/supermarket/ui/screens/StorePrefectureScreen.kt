// =========================================================
// File: StorePrefectureScreen.kt
// 設計書ID: store_select_2
// 画面名: 店舗選択（第二層：都道府県選択）
// 役割:
//   - 第一層の「地域」選択後、その地域に属する「店舗が存在する都道府県のみ」表示する。
//   - 店舗が1件も無い都道府県（例：千葉）はボタンを表示しない。
//   - 都道府県ボタン押下で第三層の StoreResultScreen へ遷移する。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    // 「店舗が存在する都道府県のみ」を取得
    val prefectures = remember(regionId) {
        StoreDataRepository.getPrefecturesWithStores(regionId)
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
                    Text("この地域には店舗が登録されていません。")
                }
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(prefectures) { pref ->

                    Button(
                        onClick = {
                            // keyword=none として第三層へ（都道府県のみで検索）
                            navController.navigate(
                                Routes.STORE_RESULT + "/keyword=none/pref=" + pref.prefectureId
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
