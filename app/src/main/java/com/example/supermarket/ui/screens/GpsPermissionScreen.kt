package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * GpsPermissionScreen
 * 🇯🇵 位置情報の許可画面
 * 🇨🇳 位置权限确认界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpsPermissionScreen(
    onGrant: () -> Unit,
    onDeny: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("位置情報の許可") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("アプリが位置情報を利用します。許可しますか？")
            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onGrant) { Text("許可する") }
                OutlinedButton(onClick = onDeny) { Text("許可しない") }
            }
        }
    }
}
