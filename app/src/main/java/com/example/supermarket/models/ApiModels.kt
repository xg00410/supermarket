// =========================================================
// File: ApiModels.kt
// 概要: PHP API との通信で利用するレスポンス/リクステスト用データモデル群。
// =========================================================

package com.example.supermarket.models

data class ApiResponse(
    val status: String,
    val message: String? = null,
    val data: List<Product>? = null,

    // login.php / update_profile.php / reset_password.php で返却されるユーザー情報
    val id: Int? = null,
    val user_id: Int? = null,
    val user_code: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,

    // insert_order.php などで返る情報
    val order_id: Int? = null,
    val total: Double? = null
)

// ---------------- リクエスト用モデル ----------------

// 会員登録
data class RegisterBody(
    val user_code: String,
    val password: String,
    val email: String?,
    val name: String?,
    val phone: String?
)

// ログイン
data class LoginBody(
    val user_code: String,
    val password: String
)

// パスワードリセット
data class ResetPasswordBody(
    val user_code: String,
    val email: String,
    val new_password: String
)

// プロフィール更新
data class UpdateProfileBody(
    val user_id: Int,
    val name: String?,
    val phone: String?,
    val email: String?
)

// 注文登録
data class OrderItem(
    val product_id: Int,
    val quantity: Int,
    val price: Double
)

// store_code で注文登録（DB側で内部の store_id に変換）
data class InsertOrderBody(
    val user_id: Int,
    val store_code: String,
    val items: List<OrderItem>
)
