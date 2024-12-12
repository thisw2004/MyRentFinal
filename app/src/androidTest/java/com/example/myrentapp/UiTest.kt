import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertIsDisplayed
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.LoginFormLayout
import com.example.myrentapp.MyMainscreenLayout
import com.example.myrentapp.UserViewModel
import org.junit.Rule
import org.junit.Test

class LoginComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule() // Use ComposeTestRule

    @Test
    fun testLoginProcess() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            // Set up NavHost for navigation testing
            NavHost(navController = navController, startDestination = "mymainscreen") {
                composable("mymainscreen") {
                    MyMainscreenLayout(navController = navController)
                }
                composable("Login") {
                    LoginFormLayout(viewModel = UserViewModel(), navController = navController)
                }
            }
        }

        // Step 1: Verify and click on the "Login" button in MyMainscreenLayout
        composeTestRule.onNodeWithTag("mainLoginButton")
            .assertIsDisplayed() // Ensure the "Login" button is visible
            .performClick() // Click the "Login" button to navigate

        // Step 2: Enter username in LoginFormLayout
        composeTestRule.onNodeWithTag("usernameField")
            .assertIsDisplayed() // Ensure the username field is visible
            .performTextInput("test") // Input username

        // Step 3: Enter password in LoginFormLayout
        composeTestRule.onNodeWithTag("passwordField")
            .assertIsDisplayed() // Ensure the password field is visible
            .performTextInput("test") // Input password

        // Step 4: Click on the "Login" button in LoginFormLayout
        composeTestRule.onNodeWithTag("loginButton")
            .assertIsDisplayed() // Ensure the "Login" button is visible
            .performClick() // Click the "Login" button to submit
    }
}
