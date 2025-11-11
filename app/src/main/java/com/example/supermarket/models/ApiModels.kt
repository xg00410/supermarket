package com.example.supermarket.models

data class ApiResponse(
    val status: String,
    val message: String? = null,
    val data: List<Product>? = null,
    val user_id: Int? = null,
    val order_id: Int? = null,
    val total: Double? = null
)

data class RegisterBody(val user_code: String, val password: String, val email: String?)
data class LoginBody(val user_code: String, val password: String)
data class OrderItem(val product_id: Int, val quantity: Int, val price: Double)
data class InsertOrderBody(val user_id: Int, val store_id: Int, val items: List<OrderItem>)
