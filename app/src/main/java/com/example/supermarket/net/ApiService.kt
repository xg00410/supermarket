// =========================================================
// File: ApiService.kt
// 概要: PHP API へのHTTPリクエスト（店舗・商品・ログイン等）を定義するインターフェース。
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.net

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    // ---- 注册接口 ----
    @POST("register.php")
    suspend fun register(@Body body: RequestBody): LoginResponse

    // ---- 获取商品列表 ----
    @GET("getProducts.php")
    suspend fun getProducts(
        @Query("store_id") storeId: Int
    ): ProductResponse

    // ---- 登录接口 ----
    @POST("login.php")
    suspend fun login(@Body body: RequestBody): LoginResponse
}

// ================================
// ✅ 数据模型（Kotlin data class）
// ================================

// 商品响应（与 getProducts.php 对应）
data class ProductResponse(
    val status: String,
    val data: List<Product>?
)

// 单个商品
data class Product(
    val product_id: Int,
    val name: String,
    val price: Double
)

// 登录响应（与 login.php 对应）
data class LoginResponse(
    val status: String,
    val id: String?,         // ← 改成 String
    val user_id: String?,    // ← 改成 String
    val name: String?,
    val message: String?
)
