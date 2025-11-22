// =========================================================
// File: PasswordResetState.kt
// 役割:
//   - パスワード再設定フロー中に入力されたユーザーIDとメールを一時的に保持する。
// =========================================================

package com.example.supermarket.data

object PasswordResetState {
    var userCode: String? = null
    var email: String? = null

    fun clear() {
        userCode = null
        email = null
    }
}
