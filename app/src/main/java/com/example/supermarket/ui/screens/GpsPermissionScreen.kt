package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp   // ✅ 必须加上这一行

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpsPermissionScreen(onGrant: () -> Unit, onDeny: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("位置情報の許可") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("アプリが位置情報を利用します。許可しますか？")
                Spacer(modifier = Modifier.height(20.dp)) // ✅ dp 已导入就不会红
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = onGrant) { Text("許可する") }
                    OutlinedButton(onClick = onDeny) { Text("許可しない") }
                }
            }
        }
    }
}
