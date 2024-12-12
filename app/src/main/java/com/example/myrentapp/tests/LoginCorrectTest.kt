import com.example.myrentapp.VehicleResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String)

data class AddVehicleRequest(
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


data class HireRequest(val carId: Int)


interface MyRentApi {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("vehicles/available")
    suspend fun getAvailableVehicles(@Header("Authorization") token: String): Response<List<VehicleResponse>>

    @POST("vehicles/hire")
    suspend fun hireVehicle(
        @Header("Authorization") token: String,
        @Body hireRequest: HireRequest
    ): Response<Unit>

    @GET("vehicles")
    suspend fun getAllVehicles(@Header("Authorization") token: String): Response<List<VehicleResponse>>

    @POST("vehicles/add")
    suspend fun addVehicle(
        @Header("Authorization") token: String,
        @Body addVehicleRequest: AddVehicleRequest
    ): Response<Unit>
}


class UserViewModelLoginTest {

    private val BASE_URL = "http://localhost:8080/"

    private val api: MyRentApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MyRentApi::class.java)

    @Test
    fun testLoginCorrect() = runBlocking {

        val correctLoginRequest = LoginRequest(username = "test", password = "test")
        val correctResponse = api.login(correctLoginRequest)
        assertTrue("Login should succeed with correct credentials", correctResponse.isSuccessful)
    }
}
