// =========================================================
// File: CartViewModel.kt
// 役割:
//   - カート内商品の追加／数量変更／削除
//   - 店舗単位チェック（list2）
//   - 商品単位チェック（list2）
//   - すべて選択（list2）
//   - 選択商品の一括削除
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.Product

class CartViewModel : ViewModel() {

    // カート一覧
    var cartItems = mutableStateListOf<CartItem>()
        private set

    // list2：商品チェック
    var selectedItemIds = mutableStateMapOf<Int, Boolean>()
        private set

    // list2：店舗チェック
    var selectedStoreIds = mutableStateMapOf<String, Boolean>()
        private set

    // list2：すべて選択
    var isSelectAll by mutableStateOf(false)
        private set

    // ======================================================
    // 商品追加（在庫を超えない）
    // ======================================================
    fun addToCart(product: Product, qty: Int = 1) {

        val existing = cartItems.find { it.productId == product.productId }

        if (existing != null) {
            existing.quantity += qty
            notifyStateChanged()
        } else {
            cartItems.add(
                CartItem(
                    productId = product.productId,
                    storeId = product.storeId,
                    storeName = product.storeName,
                    name = product.name,
                    category = product.category,
                    price = product.price,
                    quantity = qty,
                    imageRes = product.imageRes
                )
            )
        }

        updateSelectionState()
    }

    // ======================================================
    // 数量 +1
    // ======================================================
    fun increaseQuantity(productId: Int) {
        val item = cartItems.find { it.productId == productId } ?: return
        item.quantity += 1
        notifyStateChanged()
        updateSelectionState()
    }

    // ======================================================
    // 数量 -1（0 なら削除）
    // ======================================================
    fun decreaseQuantity(productId: Int) {
        val item = cartItems.find { it.productId == productId } ?: return

        if (item.quantity > 1) {
            item.quantity -= 1
        } else {
            cartItems.remove(item)
        }
        notifyStateChanged()
        updateSelectionState()
    }

    // ======================================================
    // 商品削除
    // ======================================================
    fun removeItem(productId: Int) {
        cartItems.removeAll { it.productId == productId }
        updateSelectionState()
    }

    // ======================================================
    // list2：商品チェック
    // ======================================================
    fun toggleItemChecked(productId: Int) {
        selectedItemIds[productId] = !(selectedItemIds[productId] ?: false)
        updateSelectionState()
    }

    // ======================================================
    // list2：店舗チェック
    // ======================================================
    fun toggleStoreChecked(storeId: String) {
        val newState = !(selectedStoreIds[storeId] ?: false)
        selectedStoreIds[storeId] = newState

        cartItems.filter { it.storeId == storeId }.forEach {
            selectedItemIds[it.productId] = newState
        }

        updateSelectionState()
    }

    // ======================================================
    // list2：すべて選択
    // ======================================================
    fun selectAll() {
        isSelectAll = !isSelectAll

        selectedStoreIds.keys.forEach { selectedStoreIds[it] = isSelectAll }
        selectedItemIds.keys.forEach { selectedItemIds[it] = isSelectAll }

        updateSelectionState()
    }

    // ======================================================
    // list2：選択削除
    // ======================================================
    fun deleteSelectedItems() {
        val deleteIds = selectedItemIds.filterValues { it }.keys.toSet()
        cartItems.removeAll { deleteIds.contains(it.productId) }
        updateSelectionState()
    }

    // ======================================================
    // 選択状態の更新
    // ======================================================
    private fun updateSelectionState() {

        selectedStoreIds.clear()
        cartItems.groupBy { it.storeId }.forEach { (storeId, list) ->
            selectedStoreIds[storeId] =
                list.all { selectedItemIds[it.productId] == true }
        }

        isSelectAll =
            selectedItemIds.isNotEmpty() &&
                    selectedItemIds.values.all { it }
    }

    // ======================================================
    // StateList 再描画
    // ======================================================
    private fun notifyStateChanged() {
        cartItems = cartItems.toMutableStateList()
    }

    // 合計金額（list 用）
    fun totalPrice(): Double =
        cartItems.sumOf { it.price * it.quantity }
}
