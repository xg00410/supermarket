package com.example.supermarket.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.supermarket.models.CartItem
import com.example.supermarket.models.OrderHistory
import com.example.supermarket.models.OrderHistoryItem
import com.example.supermarket.models.Product
import java.time.LocalDateTime

class CartViewModel : ViewModel() {

    // カート内の商品一覧
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    // 購入履歴
    private val _orderHistory = mutableStateListOf<OrderHistory>()
    val orderHistory: List<OrderHistory> get() = _orderHistory

    // list2 用の選択状態（商品単位）
    private val _selectedItemIds = mutableStateMapOf<Int, Boolean>()
    val selectedItemIds: Map<Int, Boolean> get() = _selectedItemIds

    // list2 用の選択状態（店舗単位）
    private val _selectedStoreIds = mutableStateMapOf<String, Boolean>()
    val selectedStoreIds: Map<String, Boolean> get() = _selectedStoreIds

    // 「すべて選択」チェックボックスの状態
    private var _selectAll by mutableStateOf(false)
    val isSelectAll: Boolean get() = _selectAll

    // -------------------------
    // カート操作
    // -------------------------

    /**
     * 商品をカートに追加する
     * 同じ productId が既にあれば数量だけ加算する
     */
    fun addToCart(product: Product, quantity: Int) {
        if (quantity <= 0) return

        val existing = _cartItems.find { it.productId == product.productId }
        if (existing != null) {
            // 既にカートにある → 数量だけ増やす
            existing.quantity += quantity
        } else {
            // 新規追加
            _cartItems.add(
                CartItem(
                    productId = product.productId,
                    storeId = product.storeId,
                    storeName = product.storeName,
                    name = product.name,
                    category = product.category,
                    price = product.price,
                    quantity = quantity,
                    imageRes = product.imageRes
                )
            )
        }
        // list2 の選択状態と整合を取る
        syncSelectionState()
    }

    fun increaseQuantity(productId: Int) {
        _cartItems.find { it.productId == productId }?.let { item ->
            item.quantity++
        }
    }

    fun decreaseQuantity(productId: Int) {
        _cartItems.find { it.productId == productId }?.let { item ->
            if (item.quantity > 1) {
                item.quantity--
            }
        }
    }

    fun removeItem(productId: Int) {
        _cartItems.removeAll { it.productId == productId }
        _selectedItemIds.remove(productId)
        syncSelectionState()
    }

    fun totalPrice(): Double =
        _cartItems.sumOf { it.quantity * it.price }

    // -------------------------
    // list2 用「選択状態」操作
    // -------------------------

    /**
     * すべて選択／すべて解除
     */
    fun selectAll() {
        val newState = !_selectAll
        _selectAll = newState

        // 全商品に同じフラグを設定
        _cartItems.forEach { item ->
            _selectedItemIds[item.productId] = newState
        }

        // 店舗ごとのチェック状態を更新
        val grouped = _cartItems.groupBy { it.storeId }
        grouped.forEach { (storeId, items) ->
            _selectedStoreIds[storeId] = items.all { _selectedItemIds[it.productId] == true }
        }
    }

    /**
     * 店舗のチェックを ON/OFF
     * その店舗に属する商品もまとめて ON/OFF する
     */
    fun toggleStoreChecked(storeId: String) {
        val itemsInStore = _cartItems.filter { it.storeId == storeId }
        if (itemsInStore.isEmpty()) return

        val allSelected = itemsInStore.all { _selectedItemIds[it.productId] == true }
        val newState = !allSelected

        itemsInStore.forEach { item ->
            _selectedItemIds[item.productId] = newState
        }
        _selectedStoreIds[storeId] = newState

        updateSelectAllFlag()
    }

    /**
     * 商品 1 件分のチェックを ON/OFF
     * 店舗単位のチェック状態と「すべて選択」を連動させる
     */
    fun toggleItemChecked(productId: Int) {
        val item = _cartItems.find { it.productId == productId } ?: return

        val current = _selectedItemIds[productId] == true
        _selectedItemIds[productId] = !current

        // 店舗単位のチェック状態更新
        val itemsInStore = _cartItems.filter { it.storeId == item.storeId }
        _selectedStoreIds[item.storeId] =
            itemsInStore.all { _selectedItemIds[it.productId] == true }

        updateSelectAllFlag()
    }

    /**
     * _cartItems と選択状態マップの整合性を取る
     * （商品追加・削除後に呼び出す）
     */
    private fun syncSelectionState() {
        // カートに存在する商品のキーを保証
        _cartItems.forEach { item ->
            if (!_selectedItemIds.containsKey(item.productId)) {
                _selectedItemIds[item.productId] = false
            }
        }

        // すでにカートに存在しない商品のキーを削除
        val validIds = _cartItems.map { it.productId }.toSet()
        val toRemove = _selectedItemIds.keys - validIds
        toRemove.forEach { _selectedItemIds.remove(it) }

        // 店舗ごとのチェック状態を再計算
        _selectedStoreIds.clear()
        val grouped = _cartItems.groupBy { it.storeId }
        grouped.forEach { (storeId, items) ->
            _selectedStoreIds[storeId] =
                items.all { _selectedItemIds[it.productId] == true }
        }

        updateSelectAllFlag()
    }

    /**
     * 「すべて選択」フラグを再計算
     */
    private fun updateSelectAllFlag() {
        _selectAll =
            _cartItems.isNotEmpty() &&
                    _cartItems.all { _selectedItemIds[it.productId] == true }
    }

    // -------------------------
    // 履歴操作（RouteScreen の終了ボタン用）
    // -------------------------

    /**
     * ルート画面の「終了」時に、
     * チェック済み商品を 1 件の注文履歴として登録する
     */
    fun addHistoryEntry(
        storeId: String,
        storeName: String,
        items: List<CartItem>,
        orderedAt: LocalDateTime
    ) {
        if (items.isEmpty()) return

        val historyItems = items.map {
            OrderHistoryItem(
                productId = it.productId,
                name = it.name,
                price = it.price,
                quantity = it.quantity
            )
        }

        val historyEntry = OrderHistory(
            orderId = System.currentTimeMillis(),
            storeId = storeId,
            storeName = storeName,
            orderedAt = orderedAt,
            items = historyItems
        )

        _orderHistory.add(historyEntry)
    }

    /**
     * RouteScreen から呼び出し：
     * チェック済みの商品だけをカートから削除する
     */
    fun removeCheckedItems(checkedIds: Set<Int>) {
        if (checkedIds.isEmpty()) return

        _cartItems.removeAll { it.productId in checkedIds }
        checkedIds.forEach { _selectedItemIds.remove(it) }

        syncSelectionState()
    }
}
