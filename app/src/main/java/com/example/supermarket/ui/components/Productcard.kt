// =========================================================
// File: ProductCard.kt
// 役割:
//   - 商品の画像・名称・価格・在庫をまとめて表示する共通カード。
//   - 右側に数量調整ボタン（- / +）を配置する。
//   - MenuScreen / CartScreen で共通利用。
// =========================================================

package com.example.supermarket.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.supermarket.models.Product

@Composable
fun ProductCard(
    product: Product,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 左：商品画像
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .size(70.dp)
            )

            // 中央：商品名・価格・在庫情報
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "価格：¥${product.price.toInt()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "在庫：${product.stock}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // 右：数量調整ボタン
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDecrease,
                    enabled = quantity > 0
                ) { Text("−") }

                Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedButton(
                    onClick = onIncrease,
                    enabled = quantity < product.stock
                ) { Text("+") }
            }
        }
    }
}
