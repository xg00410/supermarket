// =========================================================
// File: MainActivity.kt
// 概要: アプリのエントリーポイント。ナビゲーションおよびテーマ設定を統括する。
//設計書ID: なし（アプリ起動）
//画面名: メインアクティビティ
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.ui


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.supermarket.ui.AppNavHost
import com.example.supermarket.ui.theme.SupermarketTheme
import com.example.supermarket.viewmodel.CartViewModel

/**
 * MainActivity
 * 🇯🇵 アプリのエントリーポイント
 * 🇨🇳 应用入口
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SupermarketTheme {
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = viewModel()

                AppNavHost(
                    navController = navController,
                    cartViewModel = cartViewModel
                )
            }
        }
    }
}
