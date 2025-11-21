// =========================================================
// File: ApiService.kt
// 概要: PHP API へのHTTPリクエスト（店舗・商品・ログイン等）を定義するインターフェース。
// 更新者: 郭
// 更新日: 2025-11-21
// =========================================================

package com.example.supermarket.net

import com.example.supermarket.models.InsertOrderBody
import com.example.supermarket.models.LoginBody
import com.example.supermarket.models.RegisterBody
import com.example.supermarket.models.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// 商品取得用 DTO（API専用）
data class ProductDto(
    val product_id: Int,
    val name: String,
    val category: String?,
    val price: Double,
    val stock: Int?
)

// 汎用レスポンス（登録など）
data class SimpleResponse(
    val status: String,
    val message: String?
)

// ログインレスポンス（login.php と対応）
data class LoginResponse(
    val status: String,
    val id: String?,
    val user_id: String?,
    val name: String?,
    val message: String?
)

interface ApiService {

    // ---------------- 登録 ----------------
    @POST("register.php")
    suspend fun register(
        @Body body: RegisterBody
    ): SimpleResponse

    // ---------------- ログイン ----------------
    @POST("login.php")
    suspend fun login(
        @Body body: LoginBody
    ): LoginResponse

    // ---------------- 商品一覧取得（店舗別） ----------------
    // PHP 側: get_products.php?store_code=S001
    @GET("get_products.php")
    suspend fun getProductsByStore(
        @Query("store_code") storeCode: String
    ): ApiProductsResponse

    // 商品一覧レスポンス
    data class ApiProductsResponse(
        val status: String,
        val data: List<ProductDto>?,
        val message: String? = null
    )

    // ---------------- 注文登録 ----------------
    @POST("insert_order.php")
    suspend fun insertOrder(
        @Body body: InsertOrderBody
    ): ApiResponse
}
