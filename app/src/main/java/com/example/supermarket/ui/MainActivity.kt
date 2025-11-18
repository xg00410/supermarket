// =========================================================
// File: MainActivity.kt
// =========================================================

package com.example.supermarket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.supermarket.ui.AppNavHost
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.theme.SupermarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupermarketTheme {
                val navController = rememberNavController()
                val cartViewModel = CartViewModel()

                AppNavHost(
                    navController = navController,
                    cartViewModel = cartViewModel
                )
            }
        }
    }
}
