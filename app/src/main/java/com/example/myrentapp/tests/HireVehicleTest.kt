import com.example.myrentapp.VehicleResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VehicleHireTest {

    private val BASE_URL = "http://localhost:8080/"

    private val api: MyRentApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MyRentApi::class.java)

    @Test
    fun testHuurEersteBeschikbareVoertuig() = runBlocking {
        try {
            // Step 1: Login and retrieve bearer token
            val loginRequest = LoginRequest(username = "test", password = "test")
            val loginResponse: Response<LoginResponse> = api.login(loginRequest)

            assertTrue("Login failed", loginResponse.isSuccessful)

            val token = loginResponse.body()?.token ?: throw IllegalStateException("Bearer token missing")

            // Step 2: Fetch the first available vehicle
            val vehiclesResponse: Response<List<VehicleResponse>> = api.getAvailableVehicles("Bearer $token")
            assertTrue("Failed to fetch available vehicles", vehiclesResponse.isSuccessful)

            val vehicles = vehiclesResponse.body() ?: throw IllegalStateException("No vehicles found in response")

            // Pick the first vehicle from the list
            val firstVehicle = vehicles.firstOrNull()
            assertTrue("No available vehicles to hire", firstVehicle != null)

            // Step 3: Hire the first vehicle
            val hireRequest = HireRequest(carId = firstVehicle!!.id)
            val hireResponse: Response<Unit> = api.hireVehicle("Bearer $token", hireRequest)

            assertTrue("Hiring the vehicle failed", hireResponse.isSuccessful)

            println("Vehicle with ID ${firstVehicle.id} successfully hired.")

        } catch (e: Exception) {
            System.err.println("Exception during test: ${e.message}")
            assertTrue("Exception occurred: ${e.message}", false)
        }
    }
}
