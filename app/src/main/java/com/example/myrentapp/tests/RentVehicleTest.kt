import com.example.myrentapp.VehicleResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VehicleAddTest {

    private val BASE_URL = "http://localhost:8080/"

    private val api: MyRentApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MyRentApi::class.java)

    companion object {
        private var vehicleCounter = 1
        private fun generateUniqueModelName(): String {
            return "gentestcar${vehicleCounter++}"
        }
    }

    @Test
    fun testAddUniqueVehicle() = runBlocking {
        try {
            // Step 1: Login and retrieve bearer token
            val loginRequest = LoginRequest(username = "test", password = "test")
            val loginResponse: Response<LoginResponse> = api.login(loginRequest)

            assertTrue("Login failed", loginResponse.isSuccessful)

            val token = loginResponse.body()?.token ?: throw IllegalStateException("Bearer token missing")

            // Step 2: Fetch all vehicles to check if a unique model is needed
            val vehiclesResponse: Response<List<VehicleResponse>> = api.getAllVehicles("Bearer $token")
            assertTrue("Failed to fetch vehicles", vehiclesResponse.isSuccessful)

            val vehicles = vehiclesResponse.body() ?: throw IllegalStateException("Vehicles list is empty")

            // Step 3: Generate a unique model name for the vehicle
            var uniqueModelName: String
            do {
                uniqueModelName = generateUniqueModelName()
            } while (vehicles.any { it.model == uniqueModelName })

            // Step 4: Add the new vehicle
            val addVehicleRequest = AddVehicleRequest(
                brand = "Toyota${vehicleCounter}",
                model = uniqueModelName,
                buildYear = 2022,
                kenteken = "ABC-${vehicleCounter}", // Unique kenteken
                brandstof = "Petrol",
                verbruik = 6,
                kmstand = 15000,
                photoId = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", // Example photo ID
                location = "Amsterdam" // Example location
            )
            val addVehicleResponse: Response<Unit> = api.addVehicle("Bearer $token", addVehicleRequest)

            // Log response details if the addition fails
            if (!addVehicleResponse.isSuccessful) {
                println("Failed to add vehicle. Status code: ${addVehicleResponse.code()}")
                println("Error body: ${addVehicleResponse.errorBody()?.string()}")
            }

            assertTrue("Adding the vehicle failed", addVehicleResponse.isSuccessful)

            println("Vehicle with model '$uniqueModelName' successfully added.")

        } catch (e: Exception) {
            System.err.println("Exception during test: ${e.message}")
            assertTrue("Exception occurred: ${e.message}", false)
        }
    }
}
