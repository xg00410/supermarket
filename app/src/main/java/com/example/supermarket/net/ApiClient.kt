// =========================================================
// File: ApiClient.kt
// 概要: Retrofitの設定を行い、ApiServiceインスタンスを提供するクライアントクラス。
// 更新者: 郭
// 更新日: 2025-11-17
// =========================================================

package com.example.supermarket.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2/supermarket_api/"  // ⚠️ 模拟器访问本机 XAMPP

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
