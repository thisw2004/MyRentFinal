package com.example.myrentapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertIsDisplayed
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class LoginComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoginProcess() {
        val userViewModel = object : UserViewModel() {
            override fun login(username: String, password: String) {
                _loginState.value = LoginState.Success("mockToken", username)
            }
        }

        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "mymainscreen") {
                composable("mymainscreen") {
                    MyMainscreenLayout(navController = navController)
                }
                composable("Login") {
                    LoginFormLayout(viewModel = userViewModel, navController = navController)
                }
                composable("HomeScreen") {
                    HomeScreenLayout(sharedViewModel = userViewModel, navController = navController)
                }
            }
        }

        // Navigate to Login screen
        composeTestRule.onNodeWithTag("mainLoginButton")
            .assertIsDisplayed()
            .performClick()

        Thread.sleep(1000)

        // Enter username
        composeTestRule.onNodeWithTag("usernameField")
            .assertIsDisplayed()
            .performTextInput("test")

        Thread.sleep(1000)

        // Enter password
        composeTestRule.onNodeWithTag("passwordField")
            .assertIsDisplayed()
            .performTextInput("test")

        Thread.sleep(1000)

        // Click login button
        composeTestRule.onNodeWithTag("loginButton")
            .assertIsDisplayed()
            .performClick()

        Thread.sleep(1000)

        // Verify navigation to HomeScreen
        composeTestRule.onNodeWithTag("homeScreenContainer")
            .assertIsDisplayed()

        Thread.sleep(1000)
    }
}