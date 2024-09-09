package com.example.myrentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.myrentapp.ui.theme.MyRentAppTheme
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class CarViewOwnedCar : ComponentActivity() {
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
                    color = MaterialTheme.colorScheme.background
                ) {
                    CarViewOwnedCarLayout(carViewModel, rememberNavController())
                }
            }
        }
    }
}

@Composable
fun CarViewOwnedCarLayout(viewModel: CarViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.OwnedCarsTitle),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Placeholder for the list of owned cars
        // You'll need to implement this based on your CarViewModel's structure
        LazyColumn {
            items(5) { index ->
                CarItem("Car $index") {
                    // Navigate to car details screen
                    // navController.navigate("carDetails/$index")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarItem(carName: String, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = carName,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarViewOwnedCarPreview() {
    MyRentAppTheme {
        // Create a dummy CarViewModel for preview
        val dummyViewModel = CarViewModel(UserViewModel())
        CarViewOwnedCarLayout(dummyViewModel, rememberNavController())
    }
}