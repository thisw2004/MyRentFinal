import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserViewModelLoginIncorrectTest {

    private val BASE_URL = "http://localhost:8080/"

    private val api: MyRentApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MyRentApi::class.java)

    @Test
    fun testLoginIncorrect() = runBlocking {

        // Test with incorrect credentials
        val incorrectLoginRequest = LoginRequest(username = "wrongUser", password = "wrongPass")
        val incorrectResponse = api.login(incorrectLoginRequest)
        assertFalse("Login should fail with incorrect credentials", incorrectResponse.isSuccessful)
    }
}
