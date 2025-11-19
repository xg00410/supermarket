// =========================================================
// File: SelectedStoreState.kt
// 役割:
//   - アプリ全体で「現在選択中の店舗ID」を共有するシンプルな状態ホルダー。
//   - 本来は ViewModel などで管理するのが理想だが、卒業制作では
//     構成をシンプルにするため object で実装している。
// =========================================================

package com.example.supermarket.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SelectedStoreState {
    // デフォルト店舗: 新宿店 (S001)
    // アプリ起動直後や、まだ店舗を選択していない場合に使う。
    var currentStoreId by mutableStateOf("S001")
}
