package com.example.myrentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme
import kotlinx.coroutines.flow.MutableStateFlow

class CarViewAvailableCar : ComponentActivity() {
    private lateinit var carViewModel: CarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize CarViewModel
        carViewModel = ViewModelProvider(this).get(CarViewModel::class.java)

        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CarViewAvailableCarLayout(carViewModel, rememberNavController(), carId = "123") // Replace with actual carId in production
                }
            }
        }
    }
}

@Composable
fun CarViewAvailableCarLayout(viewModel: CarViewModel, navController: NavController, carId: String) {
    val vehicleState by viewModel.getVehicleById(carId).collectAsState()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = vehicleState) {
            is VehicleState.Loading -> {
                CircularProgressIndicator()
            }
            is VehicleState.Success -> {
                val car = state.vehicle
                Text(
                    text = car.model,
                    style = MaterialTheme.typography.displaySmall,
                )

                TextField(
                    value = "Merk: ${car.brand}\nModel: ${car.model}\nBouwjaar: ${car.buildYear}\nKenteken: ${car.kenteken}\nBrandstoftype: ${car.brandstof}",
                    onValueChange = { /* No-op. Read-only text field. */ },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    enabled = false
                )

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = {
                        viewModel.hireVehicle(carId)
                    }
                ) {
                    Text(stringResource(R.string.HireCar))
                }
            }
            is VehicleState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun CarViewAvailableCarPreview() {
    MyRentAppTheme {
        // Create a dummy CarViewModel for preview
        val dummyViewModel = object : CarViewModel(UserViewModel()) {
            override fun getVehicleById(carId: String) = MutableStateFlow(
                VehicleState.Success(
                    VehicleResponse(
                        id = 1,
                        rented = false,
                        userId = null,
                        brand = "Toyota",
                        model = "Corolla",
                        buildYear = 2022,
                        kenteken = "ABC-123",
                        brandstof = "Gasoline",
                        verbruik = 5,
                        kmstand = 10000,
                        photoId = null,
                        location = "Amsterdam"
                    )
                )
            )
        }

        // Use a dummy NavController for preview
        val dummyNavController = rememberNavController()

        CarViewAvailableCarLayout(dummyViewModel, dummyNavController, carId = "1")
    }
}