import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//data class LoginRequest(val username: String, val password: String)
//data class LoginResponse(val token: String)

//interface MyRentApi {
//    @POST("login")
//    suspend fun login(@Body loginRequest: LoginRequest): retrofit2.Response<LoginResponse>
//}


class UserViewModelLoginIncorrectTest {

    private val BASE_URL = "http://localhost:8080/"

    private val api: MyRentApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MyRentApi::class.java)

    @Test
    fun testLoginIncorrect() = runBlocking {
        // Test with correct credentials
//        val correctLoginRequest = LoginRequest(username = "test", password = "test")
//        val correctResponse = api.login(correctLoginRequest)
//        assertTrue("Login should succeed with correct credentials", correctResponse.isSuccessful)

        // Test with incorrect credentials
        val incorrectLoginRequest = LoginRequest(username = "wrongUser", password = "wrongPass")
        val incorrectResponse = api.login(incorrectLoginRequest)
        assertFalse("Login should fail with incorrect credentials", incorrectResponse.isSuccessful)
    }
}
