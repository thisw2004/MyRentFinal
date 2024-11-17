package com.example.myrentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme
import androidx.compose.foundation.shape.RoundedCornerShape


class MyCars : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(CarViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MyCarsLayout(viewModel, rememberNavController())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCarsLayout(viewModel: CarViewModel, navController: NavController) {
    // Start fetching rented vehicles when the Composable is first composed
    LaunchedEffect(Unit) {
        viewModel.fetchRentedVehicles()
    }

    val rentedVehicles by viewModel.rentedVehiclesState.collectAsState()

    val backgroundColor = Color(0xFF1A1D1E) // Dark background color
    val accentColor = Color(0xFF4AC0FF) // Light blue accent color
    val textColor = Color(0xFFB0BEC5) // Light grey for better readability

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Title
        Spacer(modifier = Modifier.height(24.dp)) // Move the title down
        Text(
            text = stringResource(R.string.MyCarTitle),
            style = MaterialTheme.typography.displaySmall.copy(
                color = accentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // List of rented cars
        if (rentedVehicles.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                rentedVehicles.forEach { vehicle ->
                    Card(
                        onClick = {
                            navController.navigate("carViewOwnedCar/${vehicle.id}")  // Navigate with the car ID
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2E3135) // Darker color for the card
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "${vehicle.brand} ${vehicle.model}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = textColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "License Plate: ${vehicle.kenteken}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = textColor
                                )
                            )
                        }
                    }
                }
            }
        } else {
            // Message when there are no rented vehicles
            Text(
                text = "No rented cars available.",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MyCarsPreview() {
    MyRentAppTheme {
        MyCarsLayout(CarViewModel(UserViewModel()), rememberNavController())
    }
}
