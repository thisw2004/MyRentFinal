package com.example.myrentapp

import com.example.myrentapp.SharedViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myrentapp.ui.theme.MyRentAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val sharedViewModel: SharedViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "mymainscreen"
                    ) {
                        composable("mymainscreen") {
                            MyMainscreenLayout(navController)
                        }
                        composable("catalogus") {
                            CatalogusLayout(sharedViewModel, navController)
                        }
                        composable("myCars") {
                            MyCarsLayout(sharedViewModel, navController)
                        }
                        composable("OwnedCar1") {
                            CarViewOwnedCarLayout(sharedViewModel, navController)
                        }
                        composable("AvailableCar1") {
                            CarViewAvailableCarLayout(sharedViewModel, navController)
                        }
                        composable("verhuren") {
                            VerhurenFormLayout(sharedViewModel, navController)
                        }
                        composable("huren") {
                            HurenLayout()
                        }
                        composable("looproute") {
                            CalcLooprouteLayout()
                        }
                        composable("takePhoto") {
                            MaakFotoLayout()
                        }
                        composable("Login") {
                            LoginFormLayout(sharedViewModel, navController)
                        }
                        composable("Register") {
                            RegisterFormLayout(sharedViewModel, navController)
                        }
                        composable("HomeScreen") {
                            HomeScreenLayout(sharedViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}