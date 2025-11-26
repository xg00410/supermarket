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
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorePrefectureScreen(
    navController: NavController,
    regionId: Int
) {
    // ------------------------------
    // 地域IDから都道府県一覧を取得
    // ------------------------------
    val prefecturesRaw = StoreDataRepository.getPrefecturesByRegion(regionId)

    // ------------------------------
    // 店舗住所一覧をAPIから取得 → use for filtering
    // ------------------------------
    var storeAddressList by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val api = ApiClient.retrofit.create(ApiService::class.java)
            val res = api.getStores()

            if (res.status == "ok" && res.data != null) {
                storeAddressList = res.data.map { it.address }
            }
        } catch (_: Exception) {
            // 通信エラー時は空として扱う（＝フィルタ結果は何も出ない）
        }
    }

    // ------------------------------
    // ★ 店舗が存在する都道府県のみ表示する（B3）
    // ------------------------------
    val prefectures = prefecturesRaw.filter { pref ->
        storeAddressList.any { addr -> addr.contains(pref.prefectureName) }
    }

    // ------------------------------
    // タイトル用地域名
    // ------------------------------
    val regionName = StoreDataRepository.regions
        .firstOrNull { it.regionId == regionId }
        ?.regionName ?: ""

    // ------------------------------
    // UI レイアウト
    // ------------------------------
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
                // ------------------------------
                // 店舗が存在しない地域
                // ------------------------------
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("この地域には店舗が登録されていません。")
                }
            } else {
                // ------------------------------
                // 都道府県ボタン
                // ------------------------------
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(prefectures) { pref ->

                        Button(
                            onClick = {
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
