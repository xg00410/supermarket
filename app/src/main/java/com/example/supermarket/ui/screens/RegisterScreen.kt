package com.example.supermarket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.supermarket.ui.components.AppIcon
import com.example.supermarket.net.ApiClient
import com.example.supermarket.net.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson


@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit = {}) {
    var userId by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("other") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppIcon(width = 280, height = 90)
            Spacer(modifier = Modifier.height(16.dp))
            Text("新規登録", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(value = userId, onValueChange = { userId = it }, label = { Text("ユーザーID") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("姓") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("名") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("電話番号") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("メールアドレス") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("パスワード") }, singleLine = true, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                scope.launch {
                    try {
                        val api = ApiClient.retrofit.create(ApiService::class.java)
                        val json = Gson().toJson(
                            mapOf(
                                "user_id" to userId,
                                "last_name" to lastName,
                                "first_name" to firstName,
                                "phone" to phone,
                                "gender" to gender,
                                "email" to email,
                                "password" to password
                            )
                        )
                        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
                        val response = api.register(requestBody)

                        if (response.status == "success") {
                            message = "登録成功"
                            onRegisterSuccess()
                        } else {
                            message = response.message ?: "登録失敗"
                        }
                    } catch (e: Exception) {
                        message = "通信エラー: ${e.message}"
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("登録する")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(message)
        }
    }
}
