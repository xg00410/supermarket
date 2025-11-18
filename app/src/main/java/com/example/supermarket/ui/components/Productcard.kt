package com.example.supermarket.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.supermarket.models.Product
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.R

@Composable
fun ProductCard(
    product: Product,
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier
) {
    var quantity by remember { mutableStateOf(0) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- 左: 商品图片 ---
            Image(
                painter = painterResource(
                    id = product.imageRes ?: R.drawable.logo   // 全部图片不存在时用 logo
                ),
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // --- 中央文字信息 ---
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "¥${product.price}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // --- 右侧：库存 + 数量调整按钮 ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = "在庫：${product.stock}",
                    style = MaterialTheme.typography.bodySmall
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    // 减
                    IconButton(
                        onClick = {
                            if (quantity > 0) {
                                quantity--
                                cartViewModel.removeFromCart(product)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "减少")
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )

                    // 加
                    IconButton(
                        onClick = {
                            quantity++
                            cartViewModel.addToCart(product)
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "增加")
                    }
                }
            }
        }
    }
}
