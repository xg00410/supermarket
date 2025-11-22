// =========================================================
// File: UserSession.kt
// 役割:
//   - ログイン中ユーザーの最低限の情報をアプリ内に保持するシングルトン。
//   - ログイン成功時に値をセットし、マイページや注文登録で参照する。
// =========================================================

package com.example.supermarket.data

object UserSession {
    var userId: Int? = null
    var userCode: String? = null
    var userName: String? = null
    var phone: String? = null
    var email: String? = null

    fun clear() {
        userId = null
        userCode = null
        userName = null
        phone = null
        email = null
    }
}
