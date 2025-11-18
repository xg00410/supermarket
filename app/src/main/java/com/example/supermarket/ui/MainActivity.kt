// =========================================================
// File: MainActivity.kt
// 概要:
//   - アプリ起動時に最初に呼ばれる Activity。
//   - Compose の NavHostController と CartViewModel を生成し、
//     MainScaffold を通して全画面を表示する。
// 設計書ID: main
// 画面名: メイン画面（起動）
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.supermarket.ui.components.MainScaffold
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.theme.SupermarketTheme  // ※ プロジェクトのTheme名に合わせて変更

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SupermarketTheme {
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = viewModel()

                Surface(modifier = Modifier) {
                    MainScaffold(
                        navController = navController,
                        cartViewModel = cartViewModel
                    )
                }
            }
        }
    }
}
