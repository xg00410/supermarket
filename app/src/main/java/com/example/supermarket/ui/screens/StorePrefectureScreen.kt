// =========================================================
// File: StorePrefectureScreen.kt
// 画面名: 店舗選択（第二層：都道府県選択）
// 役割:
//   - 第一層で選択された地域ID(regionId) に属する都道府県一覧を表示。
//   - 該当地域に都道府県が無い場合はメッセージ表示のみ。
//   - 都道府県ボタン押下で第三層 StoreResultScreen へ遷移。
//     → keyword として都道府県名を渡し、住所に含まれる店舗を検索する。
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
    // 🇯🇵 地域IDから都道府県一覧を取得
    val prefectures = StoreDataRepository.getPrefecturesByRegion(regionId)

    // 🇯🇵 タイトル用に地域名を取得（例: 関東）
    val regionName = StoreDataRepository.regions
        .firstOrNull { it.regionId == regionId }
        ?.regionName ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$regionName の都道府県") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            if (prefectures.isEmpty()) {
                // 🇯🇵 この地域に対応する都道府県（＝店舗）が存在しない場合
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("この地域には店舗が登録されていません。")
                }
            } else {
                // 🇯🇵 都道府県ボタン一覧
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(prefectures) { pref ->

                        Button(
                            onClick = {
                                // 🇯🇵 第三層へ遷移。
                                //     ここでは「都道府県名」を keyword として渡し、
                                //     StoreResultScreen 側で
                                //       - 店舗名
                                //       - 住所
                                //     に部分一致する店舗を API から絞り込み表示する。
                                navController.navigate(
                                    "${Routes.STORE_RESULT}/${pref.prefectureName}"
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
}
