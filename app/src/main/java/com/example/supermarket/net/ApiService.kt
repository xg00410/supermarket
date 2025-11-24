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
    /**
     * 会員登録 API (register.php)
     */
    @POST("register.php")
    suspend fun register(
        @Body body: RegisterBody
    ): ApiResponse

    // ---------------- ログイン ----------------
    /**
     * ログイン API (login.php)
     */
    @POST("login.php")
    suspend fun login(
        @Body body: LoginBody
    ): ApiResponse

    // ---------------- プロフィール更新 ----------------
    /**
     * プロフィール更新 API (update_profile.php)
     */
    @POST("update_profile.php")
    suspend fun updateProfile(
        @Body body: UpdateProfileBody
    ): ApiResponse

    // ---------------- パスワードリセット ----------------
    /**
     * パスワードリセット API (reset_password.php)
     */
    @POST("reset_password.php")
    suspend fun resetPassword(
        @Body body: ResetPasswordBody
    ): ApiResponse

    // ---------------- 商品一覧取得 ----------------
    /**
     * 店舗ごとの商品一覧取得 API (get_products.php)
     *
     * @param storeCode 店舗コード (例: "S001")
     */
    @GET("get_products.php")
    suspend fun getProductsByStore(
        @Query("store_code") storeCode: String
    ): ApiProductsResponse

    // ---------------- 店舗一覧取得 ----------------
    /**
     * 店舗一覧取得 API (get_stores.php)
     */
    @GET("get_stores.php")
    suspend fun getStores(): StoreListResponse

    // ---------------- 注文登録 ----------------
    /**
     * 注文登録 API (insert_order.php)
     */
    @POST("insert_order.php")
    suspend fun insertOrder(
        @Body body: InsertOrderBody
    ): ApiResponse

    // ---------------- 注文履歴取得 ----------------
    /**
     * ユーザー別注文履歴取得 API (get_orders.php)
     *
     * @param userId users.id
     */
    @GET("get_orders.php")
    suspend fun getOrders(
        @Query("user_id") userId: Int
    ): OrderHistoryListResponse
}
