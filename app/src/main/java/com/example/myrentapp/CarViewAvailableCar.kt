package com.example.myrentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.myrentapp.ui.theme.MyRentAppTheme
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class CarViewAvailableCar : ComponentActivity() {
    private lateinit var carViewModel: CarViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        carViewModel = ViewModelProvider(this, CarViewModelFactory(userViewModel))
            .get(CarViewModel::class.java)

        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CarViewAvailableCarLayout(carViewModel, rememberNavController())
                }
            }
        }
    }
}

@Composable
fun CarViewAvailableCarLayout(viewModel: CarViewModel, navController: NavController) {
    val addVehicleState by viewModel.addVehicleState.collectAsState()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.Car1Txt),
            style = MaterialTheme.typography.displaySmall,
        )

        Text(
            text = stringResource(R.string.CarDataTitle),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 10.dp)
                .align(alignment = Alignment.Start)
        )
        TextField(
            value = "Merk: \nModel: \nBouwjaar: \nKenteken: \nBrandstoftype: ",
            onValueChange = { /* No-op. Read-only text field. */ },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            enabled = false
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                // Here you would call viewModel.addVehicle with the necessary parameters
                // For demonstration, we're not implementing the full addVehicle call
            }
        ) {
            Text(stringResource(R.string.HireCar))
        }

        // Display loading, success, or error state
        when (addVehicleState) {
            is AddVehicleState.Loading -> CircularProgressIndicator()
            is AddVehicleState.Success -> Text((addVehicleState as AddVehicleState.Success).message)
            is AddVehicleState.Error -> Text((addVehicleState as AddVehicleState.Error).message, color = MaterialTheme.colorScheme.error)
            else -> {} // Idle state, do nothing
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}



@Preview(showBackground = true)
@Composable
fun CarViewAvailableCarPreview() {
    MyRentAppTheme {
        // For preview, we're using empty implementations
        val dummyCarViewModel = CarViewModel(UserViewModel())
        CarViewAvailableCarLayout(dummyCarViewModel, rememberNavController())
    }
}