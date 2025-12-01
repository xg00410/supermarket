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
    // 性別（任意）: 「男性」「女性」などの文字列を想定
    val gender: String?,
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
// =========================================================
// 商品一覧 API 用 DTO
// =========================================================

/**
 * 商品一覧取得 API(get_products.php) の1件分のDTO。
 *
 * PHP 側の JSON キーに合わせて snake_case を使用する。
 */
data class ProductDto(
    val product_id: Int,
    val name: String,
    val category: String?,
    val price: Double,
    val stock: Int?,
    val shelf_id: String?,          // PHP: shelf_id
    val access_point_id: String?,
    val image_name: String?// PHP: access_point_id
)

/**
 * 商品一覧レスポンス。
 */
data class ApiProductsResponse(
    val status: String,
    val message: String? = null,
    val data: List<ProductDto>?
)

// =========================================================
// 店舗一覧 API 用 DTO
// =========================================================

/**
 * 店舗一覧取得 API(get_stores.php) の1件分のDTO。
 */
data class StoreDto(
    val store_id: String,
    val name: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?
)

/**
 * 店舗一覧レスポンス。
 */
data class StoreListResponse(
    val status: String,
    val message: String? = null,
    val data: List<StoreDto>?
)

// =========================================================
// 注文履歴 API 用 DTO
// =========================================================

/**
 * 注文履歴の明細1件分のDTO。
 */
data class OrderHistoryItemDto(
    val product_id: Int,
    val name: String,
    val quantity: Int,
    val price: Int
)

/**
 * 注文履歴1件分のDTO。
 */
data class OrderHistoryDto(
    val order_id: Long,
    val store_id: String,
    val store_name: String,
    val ordered_at: String,
    val total: Int,
    val items: List<OrderHistoryItemDto>
)

/**
 * 注文履歴一覧レスポンス。
 */
data class OrderHistoryListResponse(
    val status: String,
    val message: String? = null,
    val data: List<OrderHistoryDto>?
)