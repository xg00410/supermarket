package com.example.supermarket.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.supermarket.R
import com.example.supermarket.models.Product

// =========================================================
// File: ProductCard.kt
// 役割:
//   - 商品１件分の表示カード。
//   - 画像／商品名／価格／在庫数／数量（－ 数量 ＋）を表示。
//   - 「数量の状態」は呼び出し側(MenuScreen)から受け取り、
//     onQuantityChange コールバックで親に返すだけにする。
//   - カートへの追加ロジックは一切ここでは行わない。
// =========================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左: 画像
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 8.dp)
            )

            // 中央: 商品情報
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "カテゴリ：${product.category}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "価格：${product.price.toInt()} 円",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "在庫：${product.stock} 個",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // 右: 数量コントロール
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("数量", style = MaterialTheme.typography.bodySmall)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // － ボタン
                    IconButton(
                        onClick = {
                            val newValue = (quantity - 1).coerceAtLeast(0)
                            onQuantityChange(newValue)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "減らす"
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    // ＋ ボタン
                    IconButton(
                        onClick = {
                            val newValue = quantity + 1
                            // 在庫チェックは MenuScreen 側でまとめて行うので
                            // ここでは単純に +1 するだけにする
                            onQuantityChange(newValue)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "増やす"
                        )
                    }
                }
            }
        }
    }
}
