package com.example.myrentapp

import VerhurenFormLayout
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var carViewModel: CarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize ViewModels
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        carViewModel = ViewModelProvider(this, CarViewModelFactory(userViewModel)).get(CarViewModel::class.java)

        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "mymainscreen"
                    ) {
                        composable("mymainscreen") {
                            MyMainscreenLayout(navController)
                        }
                        composable("catalogus") {
                            CatalogusLayout(carViewModel, navController)
                        }
                        composable("myCars") {
                            MyCarsLayout(carViewModel, navController)
                        }
                        composable("OwnedCar1") {
                            CarViewOwnedCarLayout(carViewModel, navController)
                        }
                        composable("AvailableCar1/{carId}") { backStackEntry ->
                            val carId = backStackEntry.arguments?.getString("carId")
                            carId?.let {
                                CarViewAvailableCarLayout(carViewModel, navController, it)
                            }
                        }
                        composable("verhuren") {
                            VerhurenFormLayout(carViewModel, userViewModel, navController)
                        }
                        composable("huren") {
                            HurenLayout(carViewModel, navController) // Added navController and carViewModel
                        }
                        composable("looproute") {
                            CalcLooprouteLayout() // Add navController if needed
                        }
                        composable("takePhoto") {
                            MaakFotoLayout() // Add navController if needed
                        }
                        composable("Login") {
                            LoginFormLayout(userViewModel, navController)
                        }
                        composable("Register") {
                            RegisterFormLayout(userViewModel, navController)
                        }
                        composable("HomeScreen") {
                            HomeScreenLayout(userViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}

class CarViewModelFactory(private val userViewModel: UserViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarViewModel(userViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
