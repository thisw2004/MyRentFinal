package com.example.myrentapp

import com.example.myrentapp.UserViewModel
import com.example.myrentapp.CarViewModel

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
                    val UserViewModel : UserViewModel = viewModel()
                    val CarViewModel : CarViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "mymainscreen"
                    ) {
                        composable("mymainscreen") {
                            MyMainscreenLayout(navController)
                        }
                        composable("catalogus") {
                            CatalogusLayout(CarViewModel, navController)
                        }
                        composable("myCars") {
                            MyCarsLayout(CarViewModel, navController)
                        }
                        composable("OwnedCar1") {
                            CarViewOwnedCarLayout(CarViewModel, navController)
                        }
                        composable("AvailableCar1") {
                            CarViewAvailableCarLayout(CarViewModel, navController)
                        }
                        composable("verhuren") {
                            VerhurenFormLayout(CarViewModel, navController)
                        }
                        composable("huren") {
                            HurenLayout() //geen viewmodel,navcontroller?
                        }
                        composable("looproute") {
                            CalcLooprouteLayout() //geen viewmodel,navcontroller?
                        }
                        composable("takePhoto") {
                            MaakFotoLayout() //geen viewmodel,navcontroller?
                        }
                        composable("Login") {
                            LoginFormLayout(UserViewModel, navController)
                        }
                        composable("Register") {
                            RegisterFormLayout(UserViewModel, navController)
                        }
                        composable("HomeScreen") {
                            HomeScreenLayout(UserViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}