// =========================================================
// File: MainScaffold.kt
// 概要: 各画面に共通するレイアウト構造（Scaffold）を提供するコンポーネント。
//設計書ID: なし（部品）
//画面名: メインレイアウト土台
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    title: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) }
            )
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}