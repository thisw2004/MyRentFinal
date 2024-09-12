package com.example.myrentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect // <- Add this import
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme

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

@Composable
fun MyCarsLayout(viewModel: CarViewModel, navController: NavController) {
    // Start fetching rented vehicles when the Composable is first composed
    LaunchedEffect(Unit) {
        viewModel.fetchRentedVehicles()
    }

    val rentedVehicles by viewModel.rentedVehiclesState.collectAsState()

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = stringResource(R.string.MyCarTitle),
            style = MaterialTheme.typography.displaySmall,
        )

        Spacer(modifier = Modifier.height(30.dp))

        // List of rented cars
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            rentedVehicles.forEach { vehicle ->
                Button(onClick = { /* Navigate to detailed view of this car */ }) {
                    Text(text = "${vehicle.brand} ${vehicle.model} (${vehicle.kenteken})")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
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
