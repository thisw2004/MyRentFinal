package com.example.myrentapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Define LoginState
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

// Define LoginRequest
data class LoginRequest(val username: String, val password: String)

// Define LoginResponse
data class LoginResponse(val token: String, val message: String)

// Define LoginApi
interface LoginApi {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}

class SharedViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    // Replace this with your actual Dev Tunnel URL
    private val DEV_TUNNEL_URL = "https://gb8fw3jh.euw.devtunnels.ms:8080"

    private val retrofit = Retrofit.Builder()
        .baseUrl(DEV_TUNNEL_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val loginApi = retrofit.create(LoginApi::class.java)

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = loginApi.login(LoginRequest(username, password))
                _loginState.value = LoginState.Success(response.token)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}