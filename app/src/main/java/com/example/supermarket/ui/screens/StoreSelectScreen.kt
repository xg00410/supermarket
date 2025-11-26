// =========================================================
// File: StoreSelectScreen.kt
// 画面名: 店舗選択（第一層：キーワード＋地域選択）
// 役割:
//   - 画面上部: 店舗名・住所のキーワード検索欄＋検索ボタン。
//   - 画面下部: 8地域ボタン（StoreDataRepository.regions を利用）。
//   - 検索ボタン押下: キーワードをクエリとして StoreResultScreen へ遷移。
//   - 地域ボタン押下: 第二層の StorePrefectureScreen へ遷移。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.data.StoreDataRepository
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSelectScreen(
    navController: NavController
) {
    // 🇯🇵 地域マスタ（8地域）。データ本体は StoreDataRepository.regions を利用。
    val regions = StoreDataRepository.regions

    // 🇯🇵 キーワード入力（店舗名・住所など）
    var keyword by remember { mutableStateOf("") }

    // 🇯🇵 入力チェック用エラーメッセージ
    var errorText by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("店舗選択") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // -------------------------------------------------
            // キーワード検索欄
            // -------------------------------------------------
            OutlinedTextField(
                value = keyword,
                onValueChange = {
                    keyword = it
                    errorText = null   // 入力中はエラー文言をクリア
                },
                label = { Text("店舗名・住所から検索") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (keyword.isBlank()) {
                        // 🇯🇵 未入力時はエラー表示だけ行い、画面遷移はしない
                        errorText = "* キーワードを入力してください"
                    } else {
                        // 🇯🇵 第三層の結果画面へ遷移（AppNavHost の定義: STORE_RESULT/{keyword}）
                        navController.navigate("${Routes.STORE_RESULT}/$keyword")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("検 索")
            }
//
            // 🇯🇵 入力チェックエラー表示
            if (errorText != null) {
                Text(
                    text = errorText!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Divider()

            // -------------------------------------------------
            // 地域から探す
            // -------------------------------------------------
            Text(
                text = "地域から探す",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(regions) { region ->

                    Button(
                        onClick = {
                            // 🇯🇵 第二層（都道府県選択画面）へ
                            navController.navigate("${Routes.STORE_PREFECTURE}/${region.regionId}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(region.regionName)
                    }
                }
            }
        }
    }
}
