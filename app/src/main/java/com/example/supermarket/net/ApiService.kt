// =========================================================
// File: ApiService.kt
// 概要: PHP API へのHTTPリクエスト（店舗・商品・ログイン等）を定義するインターフェース。
// =========================================================

package com.example.supermarket.net

import com.example.supermarket.models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // ---------------- 会員登録 ----------------
    @POST("register.php")
    suspend fun register(
        @Body body: RegisterBody
    ): ApiResponse

    // ---------------- ログイン ----------------
    @POST("login.php")
    suspend fun login(
        @Body body: LoginBody
    ): ApiResponse

    // ---------------- プロフィール更新 ----------------
    @POST("update_profile.php")
    suspend fun updateProfile(
        @Body body: UpdateProfileBody
    ): ApiResponse

    // ---------------- パスワードリセット ----------------
    @POST("reset_password.php")
    suspend fun resetPassword(
        @Body body: ResetPasswordBody
    ): ApiResponse

    // ---------------- 商品一覧取得 ----------------
    // PHP: get_products.php?store_code=S001
    data class ProductDto(
        val product_id: Int,
        val name: String,
        val category: String?,
        val price: Double,
        val stock: Int?
    )

    data class ApiProductsResponse(
        val status: String,
        val data: List<ProductDto>?,
        val message: String? = null
    )

    @GET("get_products.php")
    suspend fun getProductsByStore(
        @Query("store_code") storeCode: String
    ): ApiProductsResponse

    // ---------------- 注文登録 ----------------
    @POST("insert_order.php")
    suspend fun insertOrder(
        @Body body: InsertOrderBody
    ): ApiResponse
}
