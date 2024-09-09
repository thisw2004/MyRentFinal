package com.example.myrentapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.*
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class AddCarRequest(
    val brand: String,
    val model: String,
    val buildYear: Int,
    val kenteken: String,
    val brandstof: String,
    val verbruik: Int,
    val kmstand: Int,
    val photoId: String?,  // Changed to match the server-side field name
    val location: String,
//    val rented: Boolean = false,  // This is fine
//    val userId: Int?  // Changed to match server-side conventions
)

data class AddVehicleResponse(val message: String)

interface CarApi {
    @POST("vehicles/add")
    suspend fun addVehicle(
        @Header("Authorization") token: String,
        @Body carRequest: AddCarRequest
    ): Response<ResponseBody>
}

class CarViewModel(private val userViewModel: UserViewModel) : ViewModel() {
    private val _addVehicleState = MutableStateFlow<AddVehicleState>(AddVehicleState.Idle)
    val addVehicleState: StateFlow<AddVehicleState> = _addVehicleState

    private val BASE_URL = "https://gb8fw3jh.euw.devtunnels.ms:8080/"

    private val gson: Gson = GsonBuilder().setLenient().create()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d("OkHttp", "Sending request: ${request.url}")
            val response = chain.proceed(request)
            Log.d("OkHttp", "Received response: ${response.code}")
            response
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val api = retrofit.create(CarApi::class.java)

    fun addVehicle(
        brand: String,
        model: String,
        buildYear: Int,
        kenteken: String,
        brandstof: String,
        verbruik: Int,
        kmstand: Int,
        location: String,
        photoBase64: String?
    ) {
        viewModelScope.launch {
            if (!userViewModel.isLoggedIn()) {
                _addVehicleState.value = AddVehicleState.Error("You must be logged in to add a vehicle.")
                return@launch
            }

            _addVehicleState.value = AddVehicleState.Loading
            try {
                val token = userViewModel.userSession.value?.token ?: ""
                val authHeader = "Bearer $token"

                val carRequest = AddCarRequest(
                    brand = brand,
                    model = model,
                    buildYear = buildYear,
                    kenteken = kenteken,
                    brandstof = brandstof,
                    verbruik = verbruik,
                    kmstand = kmstand,
                    photoId = photoBase64,  // Using photoBase64 as photoId
                    location = location
//                    rented = false,  // Default value
//                    userId = null  // This will be set by the server
                )

                Log.d("CarViewModel", "Sending request: ${gson.toJson(carRequest)}")

                val response = api.addVehicle(authHeader, carRequest)

                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: ""
                    val parsedResponse = try {
                        gson.fromJson(responseBody, AddVehicleResponse::class.java)
                    } catch (e: JsonSyntaxException) {
                        null
                    }

                    _addVehicleState.value = AddVehicleState.Success(parsedResponse?.message ?: "Vehicle added successfully")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("CarViewModel", "Error adding vehicle. Code: ${response.code()}, Body: $errorBody")
                    _addVehicleState.value = AddVehicleState.Error(getSpecificErrorMessage(response.code(), errorBody))
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: "No error body"
                Log.e("CarViewModel", "HttpException: ${e.message}, Body: $errorBody")
                _addVehicleState.value = AddVehicleState.Error(getSpecificErrorMessage(e.code(), errorBody))
            } catch (e: Exception) {
                Log.e("CarViewModel", "Exception adding vehicle", e)
                _addVehicleState.value = AddVehicleState.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    private fun getSpecificErrorMessage(statusCode: Int, errorBody: String): String {
        return when (statusCode) {
            400 -> "The provided vehicle information is invalid. Please check all fields and try again."
            401 -> "Authentication failed. Please log in again."
            403 -> "You don't have permission to add a vehicle."
            404 -> "The vehicle addition service is not available. Please try again later."
            409 -> "This vehicle already exists in the system."
            500 -> "We're experiencing server issues. Please try again later. Error details: $errorBody"
            else -> "An unexpected error occurred (Error code: $statusCode). Please try again. Error details: $errorBody"
        }
    }

    fun generatePhotoName(context: Context): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val sharedPrefs = context.getSharedPreferences("PhotoCounter", Context.MODE_PRIVATE)
        val counter = sharedPrefs.getInt(currentDate, 0) + 1
        sharedPrefs.edit().putInt(currentDate, counter).apply()

        return "${currentDate}_$counter"
    }
}

sealed class AddVehicleState {
    object Idle : AddVehicleState()
    object Loading : AddVehicleState()
    data class Success(val message: String) : AddVehicleState()
    data class Error(val message: String) : AddVehicleState()
}
