// =========================================================
// File: PasswordResetScreen.kt
// 画面名: パスワード再設定
// =========================================================

package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.supermarket.ui.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordResetScreen(navController: NavController) {

    var pass1 by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("パスワード再設定") }) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = pass1,
                onValueChange = { pass1 = it },
                label = { Text("新しいパスワード") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pass2,
                onValueChange = { pass2 = it },
                label = { Text("確認用パスワード") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    navController.navigate(Routes.PASSWORD_RESET_SUCCESS)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("変更する")
            }
        }
    }
}
