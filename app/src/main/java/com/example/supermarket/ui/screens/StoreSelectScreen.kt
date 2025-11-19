package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSelectScreen(
    navController: NavController
) {
    // 8地域マスタ（StoreDataRepository 側の Region とは別に、
    // ここではボタン表示用に ID と名称だけ持つ）
    val regionNames = listOf(
        Pair(1, "北海道"),
        Pair(2, "東北"),
        Pair(3, "関東"),
        Pair(4, "中部"),
        Pair(5, "近畿"),
        Pair(6, "中国"),
        Pair(7, "四国"),
        Pair(8, "九州・沖縄")
    )

    var keyword by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
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

            // キーワード検索
            OutlinedTextField(
                value = keyword,
                onValueChange = {
                    keyword = it
                    errorText = null
                },
                label = { Text("店舗名・住所から検索") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (keyword.isBlank()) {
                        errorText = "* キーワードを入力してください"
                    } else {
                        navController.navigate(
                            Routes.STORE_RESULT + "/keyword=" + keyword + "/pref=0"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("検 索")
            }

            if (errorText != null) {
                Text(errorText!!, color = MaterialTheme.colorScheme.error)
            }

            Divider()

            // 地域から探す
            Text("地域から探す", style = MaterialTheme.typography.titleMedium)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(regionNames) { pair ->
                    val id = pair.first
                    val name = pair.second

                    Button(
                        onClick = {
                            navController.navigate(Routes.STORE_PREFECTURE + "/" + id)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(name)
                    }
                }
            }
        }
    }
}
