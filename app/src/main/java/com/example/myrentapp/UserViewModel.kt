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
import java.util.concurrent.TimeUnit

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String, val username: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val message: String, val username: String?)

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val username: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

data class RegisterRequest(val username: String, val password: String, val email: String)
data class RegisterResponse(val message: String, val username: String)

data class UserSession(val token: String, val username: String)

interface MyRentApi {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<ResponseBody>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<ResponseBody>
}

class UserViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    private val _userSession = MutableStateFlow<UserSession?>(null)
    val userSession: StateFlow<UserSession?> = _userSession

    private val _debugInfo = MutableStateFlow<String>("")
    val debugInfo: StateFlow<String> = _debugInfo

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


    fun isLoggedIn(): Boolean {
        return userSession.value != null
    }
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            _debugInfo.value = "Starting login process..."
            try {
                val response = api.login(LoginRequest(username, password))
                _debugInfo.value += "\nReceived login response: ${response.code()}"

                val responseBody = response.body()?.string() ?: response.errorBody()?.string() ?: ""
                _debugInfo.value += "\nLogin response body: $responseBody"

                if (response.isSuccessful) {
                    val parsedResponse = try {
                        gson.fromJson(responseBody, LoginResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        _debugInfo.value += "\nFailed to parse login JSON: ${e.message}"
                        null
                    }

                    if (parsedResponse != null) {
                        val sessionUsername = parsedResponse.username ?: username
                        _userSession.value = UserSession(token = parsedResponse.token, username = sessionUsername)
                        _loginState.value = LoginState.Success(parsedResponse.token, sessionUsername)
                        _debugInfo.value += "\nLogin successful. Username: $sessionUsername"
                    } else {
                        _loginState.value = LoginState.Error("Failed to parse login response")
                        _debugInfo.value += "\nLogin failed: Unable to parse response"
                    }
                } else {
                    _loginState.value = LoginState.Error(getSpecificErrorMessage(response.code(), responseBody))
                    _debugInfo.value += "\nLogin failed. Error: ${getSpecificErrorMessage(response.code(), responseBody)}"
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: ""
                _loginState.value = LoginState.Error(getSpecificErrorMessage(e.code(), errorBody))
                _debugInfo.value += "\nLogin HTTP Exception: ${e.message}. Error body: $errorBody"
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("An unexpected error occurred: ${e.message}")
                _debugInfo.value += "\nLogin Unexpected Exception: ${e::class.simpleName} - ${e.message}"
            }
        }
    }

    fun register(username: String, password: String, email: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            _debugInfo.value = "Starting registration process..."
            try {
                val response = api.register(RegisterRequest(username, password, email))
                _debugInfo.value += "\nReceived registration response: ${response.code()}"

                val responseBody = response.body()?.string() ?: response.errorBody()?.string() ?: ""
                _debugInfo.value += "\nRegistration response body: $responseBody"

                if (response.isSuccessful) {
                    val parsedResponse = try {
                        gson.fromJson(responseBody, RegisterResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        _debugInfo.value += "\nFailed to parse registration JSON: ${e.message}"
                        null
                    }

                    if (parsedResponse != null) {
                        _userSession.value = UserSession(token = "", username = parsedResponse.username)
                        _registerState.value = RegisterState.Success(parsedResponse.username)
                        _debugInfo.value += "\nRegistration successful. Username: ${parsedResponse.username}"
                    } else {
                        _registerState.value = RegisterState.Success(username)
                        _debugInfo.value += "\nRegistration assumed successful, but couldn't parse response. Using provided username: $username"
                    }
                } else {
                    _registerState.value = RegisterState.Error(getSpecificErrorMessage(response.code(), responseBody))
                    _debugInfo.value += "\nRegistration failed. Error: ${getSpecificErrorMessage(response.code(), responseBody)}"
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: ""
                _registerState.value = RegisterState.Error(getSpecificErrorMessage(e.code(), errorBody))
                _debugInfo.value += "\nRegistration HTTP Exception: ${e.message}. Error body: $errorBody"
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("An unexpected error occurred: ${e.message}")
                _debugInfo.value += "\nRegistration Unexpected Exception: ${e::class.simpleName} - ${e.message}"
            }
        }
    }

    private fun getSpecificErrorMessage(statusCode: Int, errorBody: String): String {
        return when (statusCode) {
            409 -> {
                when {
                    errorBody.contains("email", ignoreCase = true) -> "This email address is already in use. Please use a different one."
                    errorBody.contains("username", ignoreCase = true) -> "This username is already taken. Please choose a different one."
                    else -> "The username or email is already in use. Please choose different ones."
                }
            }
            400 -> {
                when {
                    errorBody.contains("email", ignoreCase = true) -> "The email address is invalid. Please enter a valid email."
                    errorBody.contains("password", ignoreCase = true) -> "The password is invalid. It must be at least 8 characters long and contain a mix of letters, numbers, and symbols."
                    errorBody.contains("username", ignoreCase = true) -> "The username is invalid. It must be between 3 and 20 characters and contain only letters, numbers, and underscores."
                    else -> "The provided information is invalid. Please check all fields and try again."
                }
            }
            401 -> "Authentication failed. Please check your credentials."
            403 -> "Access denied. You don't have permission to perform this action."
            404 -> "The requested resource was not found. Please try again later."
            500 -> "We're experiencing server issues. Please try again later."
            else -> "An unexpected error occurred (Error code: $statusCode). Please try again."
        }
    }

    fun logout() {
        _userSession.value = null
        _loginState.value = LoginState.Idle
        _registerState.value = RegisterState.Idle
        _debugInfo.value += "\nUser logged out. Session cleared."
    }

    fun clearStates() {
        _loginState.value = LoginState.Idle
        _registerState.value = RegisterState.Idle
        _debugInfo.value += "\nStates cleared."
    }
}