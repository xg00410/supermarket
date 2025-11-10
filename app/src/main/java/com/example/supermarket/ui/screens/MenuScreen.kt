package com.example.supermarket.ui.screens
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supermarket.data.FakeRepository
import com.example.supermarket.model.Product
import com.example.supermarket.viewmodel.CartViewModel
import com.example.supermarket.ui.components.BottomNavBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    storeId: String,
    onBack: () -> Unit,
    onGoCart: () -> Unit,
    cartViewModel: CartViewModel = viewModel()
) {
    val products = remember { FakeRepository.getProductsByStore(storeId) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val filtered = products.filter {
        it.name.contains(searchText.text, ignoreCase = true) ||
                it.category.contains(searchText.text, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("商品一覧") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("戻る") }
                },
                actions = {
                    Button(onClick = onGoCart) {
                        Text("カート(${cartViewModel.totalCount()})")
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("商品名またはカテゴリ検索") },
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered) { product ->
                    ProductCard(
                        product = product,
                        onAdd = { cartViewModel.addToCart(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onAdd: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text("カテゴリ: ${product.category}")
            Text("価格: ${product.priceYen}円")
            Text("在庫: ${product.stock}個")

            Button(
                onClick = onAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("カートに追加")
            }
        }
    }
}
