// =========================================================
// File: CartViewModel.kt
// 役割:
//   - カート内商品の追加／数量変更／削除
//   - 店舗単位チェック（list2）
//   - 商品単位チェック（list2）
//   - すべて選択（list2）
//   - 選択商品の一括削除
//   - ルート画面からの「取得済み商品」を履歴として保存
// 設計書ID: list / list2 / route / order_history
// 更新者: 郭
// 更新日: 2025-11-18
// =========================================================

package com.example.supermarket.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.Product
import java.time.LocalDateTime

// ---------------------------------------------------------
// 注文履歴1件分を表すデータ
// ---------------------------------------------------------
data class OrderHistoryEntry(
    val orderId: Int,                 // 注文ID（アプリ内で採番）
    val storeId: String,              // 店舗ID
    val storeName: String,            // 店舗名
    val orderedAt: LocalDateTime,     // 購入日時
    val items: List<CartItem>         // 購入した商品一覧
)

/**
 * CartViewModel
 * カートと注文履歴を管理する ViewModel。
 */
class CartViewModel : ViewModel() {

    // カート一覧（list / list2 / route で使用）
    var cartItems = mutableStateListOf<CartItem>()
        private set

    // 履歴一覧（order_history画面で使用）
    var orderHistory = mutableStateListOf<OrderHistoryEntry>()
        private set

    private var nextOrderId = 1

    // list2：商品チェック状態
    var selectedItemIds = mutableStateMapOf<Int, Boolean>()
        private set

    // list2：店舗チェック状態
    var selectedStoreIds = mutableStateMapOf<String, Boolean>()
        private set

    // list2：全選択フラグ
    var isSelectAll by mutableStateOf(false)
        private set

    // ======================================================
    // 商品追加（Menu画面 → カート）
    // ======================================================
    fun addToCart(product: Product, qty: Int = 1) {
        if (qty <= 0) return

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
    // 数量 -1（0になったら削除）
    // ======================================================
    fun removeFromCart(product: Product) {
        val existing = cartItems.find { it.productId == product.productId } ?: return

        if (existing.quantity > 1) {
            existing.quantity -= 1
        } else {
            cartItems.remove(existing)
        }
        notifyStateChanged()
        updateSelectionState()
    }

    // ======================================================
    // 数量 +1（直接 productId 指定）
    // ======================================================
    fun increaseQuantity(productId: Int) {
        val item = cartItems.find { it.productId == productId } ?: return
        item.quantity += 1
        notifyStateChanged()
        updateSelectionState()
    }

    // ======================================================
    // 数量 -1（productId 指定、0なら削除）
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
    // 商品1行をまるごと削除
    // ======================================================
    fun removeItem(productId: Int) {
        cartItems.removeAll { it.productId == productId }
        notifyStateChanged()
        updateSelectionState()
    }

    // ======================================================
    // Route画面から呼び出し：
    // 取得済み（チェックON）の商品をカートから消費し、履歴登録用に返す
    // ======================================================
    fun consumeItems(obtainedProductIds: List<Int>): List<CartItem> {
        if (obtainedProductIds.isEmpty()) return emptyList()

        val obtainedSet = obtainedProductIds.toSet()
        val obtainedItems = cartItems.filter { obtainedSet.contains(it.productId) }

        // カートから削除
        cartItems.removeAll { obtainedSet.contains(it.productId) }
        notifyStateChanged()
        updateSelectionState()

        return obtainedItems
    }

    // ======================================================
    // 履歴登録（Route画面 → OrderHistory画面）
    // ======================================================
    fun registerOrder(obtainedItems: List<CartItem>) {
        if (obtainedItems.isEmpty()) return

        val first = obtainedItems.first()
        val entry = OrderHistoryEntry(
            orderId = nextOrderId++,
            storeId = first.storeId,
            storeName = first.storeName,
            orderedAt = LocalDateTime.now(),
            items = obtainedItems.map { it.copy() } // 念のためコピーを保存
        )

        // 新しい履歴を先頭に追加（新しい順）
        orderHistory.add(0, entry)
    }

    // ======================================================
    // list2：商品チェック切り替え
    // ======================================================
    fun toggleItemChecked(productId: Int) {
        selectedItemIds[productId] = !(selectedItemIds[productId] ?: false)
        updateSelectionState()
    }

    // ======================================================
    // list2：店舗チェック切り替え
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
    // list2：全選択／全解除
    // ======================================================
    fun selectAll() {
        isSelectAll = !isSelectAll

        cartItems.forEach {
            selectedItemIds[it.productId] = isSelectAll
            selectedStoreIds[it.storeId] = isSelectAll
        }

        updateSelectionState()
    }

    // ======================================================
    // list2：選択されている商品を一括削除
    // ======================================================
    fun deleteSelectedItems() {
        val deleteIds = selectedItemIds.filterValues { it }.keys.toSet()
        if (deleteIds.isEmpty()) return

        cartItems.removeAll { deleteIds.contains(it.productId) }
        notifyStateChanged()
        updateSelectionState()
    }

    // ======================================================
    // チェック状態の再計算
    // ======================================================
    private fun updateSelectionState() {
        // 店舗ごとの選択状態を再計算
        selectedStoreIds.clear()
        cartItems.groupBy { it.storeId }.forEach { (storeId, list) ->
            selectedStoreIds[storeId] =
                list.isNotEmpty() && list.all { selectedItemIds[it.productId] == true }
        }

        // 全選択状態
        val allItems = cartItems.map { it.productId }.toSet()
        isSelectAll =
            allItems.isNotEmpty() && allItems.all { selectedItemIds[it] == true }
    }

    // ======================================================
    // StateList 再描画トリガ
    // ======================================================
    private fun notifyStateChanged() {
        cartItems = cartItems.toMutableStateList()
    }

    // 合計金額（list 用）
    fun totalPrice(): Double =
        cartItems.sumOf { it.price * it.quantity }
}
