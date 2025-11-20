// =========================================================
// File: CartViewModel.kt
// 役割:
//   - カート内商品の状態管理
//   - list / list2 画面での選択状態管理
//   - 最短ルート画面からの「取得済み商品」を履歴に保存
//   - 履歴一覧画面（OrderHistoryScreen）へ履歴データを提供
// =========================================================

package com.example.supermarket.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.Product
import com.example.supermarket.models.OrderHistory
import com.example.supermarket.models.OrderHistoryItem
import java.time.LocalDateTime

class CartViewModel : ViewModel() {

    // ----------------------------------------------------
    // カート内商品
    // ----------------------------------------------------
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    // ----------------------------------------------------
    // list2 用の選択状態
    //   - selectedItemIds: 商品単位のチェック状態
    //   - selectedStoreIds: 店舗単位のチェック状態
    //   - isSelectAll: 「すべて選択」がONかどうか
    // ----------------------------------------------------
    val selectedItemIds = mutableStateMapOf<Int, Boolean>()
    val selectedStoreIds = mutableStateMapOf<String, Boolean>()
    var isSelectAll by mutableStateOf(false)
        private set

    // ----------------------------------------------------
    // 購入履歴（RouteScreen 終了時に追加）
    //   - OrderHistoryScreen が参照する
    // ----------------------------------------------------
    private val _orderHistory = mutableStateListOf<OrderHistory>()
    val orderHistory: List<OrderHistory> get() = _orderHistory

    // ====================================================
    //                      カート操作
    // ====================================================

    /**
     * 商品一覧（MenuScreen）からカートへ追加。
     *
     * @param product  追加対象の商品
     * @param quantity 選択中の数量（0 以下なら無視）
     */
    fun addToCart(product: Product, quantity: Int) {
        if (quantity <= 0) return

        val existing = _cartItems.find { it.productId == product.productId }
        if (existing != null) {
            // すでにカートにある場合は数量だけ加算
            existing.quantity += quantity
        } else {
            // 新規追加
            _cartItems.add(
                CartItem(
                    productId = product.productId,
                    storeId = product.storeId,
                    name = product.name,
                    category = product.category,
                    quantity = quantity,
                    price = product.price,
                    storeName = product.storeName,
                    imageRes = product.imageRes
                )
            )
        }
    }

    /** 数量＋1（list / list2 / カート画面共通） */
    fun increaseQuantity(productId: Int) {
        _cartItems.find { it.productId == productId }?.let { item ->
            item.quantity++
        }
    }

    /** 数量−1（ただし1未満にはしない） */
    fun decreaseQuantity(productId: Int) {
        _cartItems.find { it.productId == productId }?.let { item ->
            if (item.quantity > 1) {
                item.quantity--
            }
        }
    }

    /** 商品を1件削除 */
    fun removeItem(productId: Int) {
        _cartItems.removeAll { it.productId == productId }
        // 選択状態も消しておく
        selectedItemIds.remove(productId)
    }

    /** カート内の合計金額 */
    fun totalPrice(): Double {
        return _cartItems.sumOf { it.quantity * it.price }
    }

    // ====================================================
    //                list2 用 チェックボックス制御
    // ====================================================

    /** すべて選択／解除 */
    fun selectAll() {
        isSelectAll = !isSelectAll

        selectedItemIds.clear()
        selectedStoreIds.clear()

        if (isSelectAll) {
            _cartItems.forEach { item ->
                selectedItemIds[item.productId] = true
                selectedStoreIds[item.storeId] = true
            }
        }
    }

    /** 店舗単位のチェック切替 */
    fun toggleStoreChecked(storeId: String) {
        val cur = selectedStoreIds[storeId] ?: false
        selectedStoreIds[storeId] = !cur

        // 店舗の商品をまとめて更新
        _cartItems.filter { it.storeId == storeId }.forEach {
            selectedItemIds[it.productId] = !cur
        }
    }

    /** 商品単位のチェック切替 */
    fun toggleItemChecked(productId: Int) {
        val cur = selectedItemIds[productId] ?: false
        selectedItemIds[productId] = !cur
    }

    /** チェックされた商品を削除（list2 の「削除」ボタン） */
    fun deleteSelectedItems() {
        val removeIds = selectedItemIds.filter { it.value }.keys
        _cartItems.removeAll { it.productId in removeIds }

        selectedItemIds.clear()
        selectedStoreIds.clear()
        isSelectAll = false
    }

    // ====================================================
    //           最短ルート画面 → 履歴への反映
    // ====================================================

    /**
     * RouteScreen の「終了」ボタンから呼び出される。
     * チェックされた商品だけを履歴に保存する。
     *
     * @param storeId   店舗ID
     * @param storeName 店舗名
     * @param items     チェックされた CartItem 一覧
     * @param orderedAt 購入日時（RouteScreen から渡される LocalDateTime）
     */
    fun addHistoryEntry(
        storeId: String,
        storeName: String,
        items: List<CartItem>,
        orderedAt: LocalDateTime
    ) {
        if (items.isEmpty()) return

        val historyItems = items.map { cartItem ->
            OrderHistoryItem(
                productId = cartItem.productId,
                name = cartItem.name,
                price = cartItem.price,
                quantity = cartItem.quantity
            )
        }

        val entry = OrderHistory(
            orderId = System.currentTimeMillis(), // 一意ID（簡易）
            storeId = storeId,
            storeName = storeName,
            orderedAt = orderedAt,
            items = historyItems
        )

        _orderHistory.add(entry)
    }

    /**
     * RouteScreen の終了時に、
     * 「取得済み（チェック済み）」の商品だけをカートから削除する。
     */
    fun removeCheckedItems(checkedIds: Set<Int>) {
        if (checkedIds.isEmpty()) return

        _cartItems.removeAll { it.productId in checkedIds }

        // 選択状態もクリア
        checkedIds.forEach { selectedItemIds.remove(it) }
    }
}
