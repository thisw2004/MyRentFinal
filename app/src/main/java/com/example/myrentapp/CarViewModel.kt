package com.example.myrentapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
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
    val photoId: String?,
    val location: String
)

data class CarIdRequest(val carId: Int)

data class AddVehicleResponse(val message: String)
data class VehicleResponse(
    val id: Int,
    val rented: Boolean,
    val userId: Int?,
    val brand: String,
    val model: String,
    val buildYear: Int,
    val kenteken: String,
    val brandstof: String,
    val verbruik: Int,
    val kmstand: Int,
    val photoId: String?,
    val location: String
)

interface CarApi {
    @POST("vehicles/getbyid")
    suspend fun getVehicleById(
        @Header("Authorization") token: String,
        @Body carIdRequest: CarIdRequest
    ): Response<VehicleResponse>

    @POST("vehicles/hire")
    suspend fun hireVehicle(
        @Header("Authorization") token: String,
        @Body hireRequest: Map<String, Int>
    ): Response<ResponseBody>

    @POST("vehicles/add")
    suspend fun addVehicle(
        @Header("Authorization") token: String,
        @Body carRequest: AddCarRequest
    ): Response<AddVehicleResponse>

    @GET("vehicles/available")
    suspend fun getAvailableVehicles(
        @Header("Authorization") token: String
    ): Response<List<VehicleResponse>>

    @GET("vehicles/myrentedvehicles")
    suspend fun getMyRentedVehicles(
        @Header("Authorization") token: String
    ): Response<List<VehicleResponse>>
}

open class CarViewModel(private val userViewModel: UserViewModel) : ViewModel() {
    private val _addVehicleState = MutableStateFlow<AddVehicleState>(AddVehicleState.Idle)
    val addVehicleState: StateFlow<AddVehicleState> = _addVehicleState

    private val _availableVehiclesState = MutableStateFlow<List<VehicleResponse>>(emptyList())
    val availableVehiclesState: StateFlow<List<VehicleResponse>> = _availableVehiclesState

    private val _rentedVehiclesState = MutableStateFlow<List<VehicleResponse>>(emptyList())
    val rentedVehiclesState: StateFlow<List<VehicleResponse>> = _rentedVehiclesState

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

    private val vehicleCache = mutableMapOf<String, StateFlow<VehicleState>>()

    fun fetchAvailableVehicles() {
        viewModelScope.launch {
            if (!userViewModel.isLoggedIn()) {
                // Handle not logged in case
                return@launch
            }

            try {
                val token = userViewModel.userSession.value?.token ?: ""
                val authHeader = "Bearer $token"

                val response = api.getAvailableVehicles(authHeader)

                if (response.isSuccessful) {
                    response.body()?.let {
                        _availableVehiclesState.value = it
                    }
                } else {
                    Log.e("CarViewModel", "Failed to fetch available vehicles: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CarViewModel", "Exception fetching available vehicles", e)
            }
        }
    }

    open fun getVehicleById(carId: String): StateFlow<VehicleState> {
        return vehicleCache.getOrPut(carId) {
            MutableStateFlow<VehicleState>(VehicleState.Loading).also { flow ->
                viewModelScope.launch {
                    if (!userViewModel.isLoggedIn()) {
                        flow.value = VehicleState.Error("You must be logged in to view vehicle details.")
                        return@launch
                    }

                    try {
                        val token = userViewModel.userSession.value?.token ?: ""
                        val authHeader = "Bearer $token"

                        val response = api.getVehicleById(authHeader, CarIdRequest(carId.toInt()))

                        if (response.isSuccessful) {
                            flow.value = VehicleState.Success(response.body()!!)
                        } else {
                            flow.value = VehicleState.Error("Failed to fetch vehicle details: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        flow.value = VehicleState.Error("Exception fetching vehicle details: ${e.message}")
                    }
                }
            }
        }
    }

    fun hireVehicle(carId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (!userViewModel.isLoggedIn()) {
                _addVehicleState.value = AddVehicleState.Error("You must be logged in to rent a vehicle.")
                return@launch
            }

            _addVehicleState.value = AddVehicleState.Loading
            try {
                val token = userViewModel.userSession.value?.token ?: ""
                val authHeader = "Bearer $token"

                val hireRequest = mapOf("carId" to carId.toInt())

                val response = api.hireVehicle(authHeader, hireRequest)

                if (response.isSuccessful) {
                    _addVehicleState.value = AddVehicleState.Success("Car rented successfully")
                    onSuccess() // Trigger the success callback
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("CarViewModel", "Error renting vehicle. Code: ${response.code()}, Body: $errorBody")
                    _addVehicleState.value = AddVehicleState.Error(getSpecificErrorMessage(response.code(), errorBody))
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: "No error body"
                Log.e("CarViewModel", "HttpException: ${e.message}, Body: $errorBody")
                _addVehicleState.value = AddVehicleState.Error(getSpecificErrorMessage(e.code(), errorBody))
            } catch (e: Exception) {
                Log.e("CarViewModel", "Exception renting vehicle", e)
                _addVehicleState.value = AddVehicleState.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }


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
                    photoId = photoBase64,
                    location = location
                )

                Log.d("CarViewModel", "Sending request: ${gson.toJson(carRequest)}")

                val response = api.addVehicle(authHeader, carRequest)

                if (response.isSuccessful) {
                    _addVehicleState.value = AddVehicleState.Success("Vehicle added successfully")
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

    fun fetchRentedVehicles() {
        viewModelScope.launch {
            if (!userViewModel.isLoggedIn()) {
                // Handle not logged in case
                return@launch
            }

            try {
                val token = userViewModel.userSession.value?.token ?: ""
                val authHeader = "Bearer $token"

                val response = api.getMyRentedVehicles(authHeader)

                if (response.isSuccessful) {
                    response.body()?.let {
                        _rentedVehiclesState.value = it
                    }
                } else {
                    Log.e("CarViewModel", "Failed to fetch rented vehicles: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CarViewModel", "Exception fetching rented vehicles", e)
            }
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

    private fun getSpecificErrorMessage(statusCode: Int, errorBody: String): String {
        return when (statusCode) {
            400 -> "The provided vehicle information is invalid. Please check all fields and try again."
            401 -> "Authentication failed. Please log in again."
            403 -> "You don't have permission to rent a vehicle."
            404 -> "The vehicle rental service is not available. Please try again later."
            409 -> "This vehicle is already rented."
            500 -> "We're experiencing server issues. Please try again later. Error details: $errorBody"
            else -> "An unexpected error occurred (Error code: $statusCode). Please try again. Error details: $errorBody"
        }
    }
}

sealed class AddVehicleState {
    object Idle : AddVehicleState()
    object Loading : AddVehicleState()
    data class Success(val message: String) : AddVehicleState()
    data class Error(val message: String) : AddVehicleState()
}

sealed class VehicleState {
    object Loading : VehicleState()
    data class Success(val vehicle: VehicleResponse) : VehicleState()
    data class Error(val message: String) : VehicleState()
}
