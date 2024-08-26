package com.example.myrentapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val message: String)

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

data class RegisterRequest(val username: String, val password: String, val email: String)
data class RegisterResponse(val message: String)

interface MyRentApi {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<ResponseBody>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<ResponseBody>
}

class SharedViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    private val BASE_URL = "https://gb8fw3jh.euw.devtunnels.ms:8080/"

    private val gson: Gson = GsonBuilder().setLenient().create()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val api = retrofit.create(MyRentApi::class.java)

    private fun parseResponse(response: ResponseBody, type: Type): Any {
        val responseString = response.string()
        return try {
            gson.fromJson(responseString, type)
        } catch (e: JsonSyntaxException) {
            responseString
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = api.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    val parsedResponse = parseResponse(response.body()!!, LoginResponse::class.java)
                    when (parsedResponse) {
                        is LoginResponse -> _loginState.value = LoginState.Success(parsedResponse.token)
                        is String -> {
                            if (parsedResponse.contains("success", ignoreCase = true)) {
                                _loginState.value = LoginState.Success("dummy_token")
                            } else {
                                _loginState.value = LoginState.Error(parsedResponse)
                            }
                        }
                        else -> _loginState.value = LoginState.Error("Unknown response format")
                    }
                } else {
                    _loginState.value = LoginState.Error(getErrorMessage(response.code()))
                }
            } catch (e: HttpException) {
                _loginState.value = LoginState.Error(getErrorMessage(e.code()))
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun register(username: String, password: String, email: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val response = api.register(RegisterRequest(username, password, email))
                if (response.isSuccessful) {
                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error(getErrorMessage(response.code()))
                }
            } catch (e: HttpException) {
                _registerState.value = RegisterState.Error(getErrorMessage(e.code()))
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun getErrorMessage(statusCode: Int): String {
        return when (statusCode) {
            409 -> "Username already exists"
            400 -> "Invalid input. Please check your details."
            401 -> "Unauthorized. Please check your credentials."
            404 -> "Resource not found."
            500 -> "Server error. Please try again later."
            else -> "An error occurred. Please try again."
        }
    }

    fun logout() {
        _loginState.value = LoginState.Idle
        _registerState.value = RegisterState.Idle
    }
}