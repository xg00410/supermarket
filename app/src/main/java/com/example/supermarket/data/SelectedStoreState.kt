// =========================================================
// File: SelectedStoreState.kt
// 役割:
//   - 「直近でユーザーが見ていた店舗」の storeId を保持する簡易な状態ホルダー。
//   - BottomNavBar（店舗タブ）などから参照して、
//     その店舗の Menu 画面を開くために利用する。
// 備考:
//   - 本来は ViewModel や DataStore 等で管理すべきだが、
//     現段階では最小限の改修に留めるため、単純なオブジェクトで実装する。
// =========================================================

package com.example.supermarket.data

object SelectedStoreState {
    // デフォルトは新宿店（S001）
    var currentStoreId: String = "S001"
}
