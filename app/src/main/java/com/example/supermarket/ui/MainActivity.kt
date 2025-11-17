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
