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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect

class Huren : ComponentActivity() {
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
                    HurenLayout(viewModel, rememberNavController())
                }
            }
        }
    }
}

@Composable
fun HurenLayout(viewModel: CarViewModel, navController: NavController) {

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
            text = stringResource(R.string.titleHuren),
            style = MaterialTheme.typography.displaySmall,
        )

        Text(
            text = stringResource(R.string.LicenseValid),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 10.dp)
                .align(alignment = Alignment.Start)
        )
        Text(
            text = stringResource(R.string.MyRentedCarData),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 10.dp)
                .align(alignment = Alignment.Start)
        )
        TextField(
            value = "Merk: \nModel: \nBouwjaar: \nKenteken: \nBrandstoftype: ",
            onValueChange = { /* No-op. Read-only text field. */ },
            modifier = Modifier.fillMaxSize(),
            singleLine = false,
            enabled = false
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            val carId = "1"
            viewModel.hireVehicle(carId) {
                navController.navigate("myCars")
            }
        }) {
            Text(stringResource(R.string.ConfirmHuren))
        }

        when (addVehicleState) {
            is AddVehicleState.Loading -> {
                Text("Processing...")
            }
            is AddVehicleState.Success -> {
                Text((addVehicleState as AddVehicleState.Success).message)
            }
            is AddVehicleState.Error -> {
                Text((addVehicleState as AddVehicleState.Error).message)
            }
            else -> {
                // Idle state, do nothing
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HurenLayoutPreview() {
    MyRentAppTheme {
        HurenLayout(viewModel = CarViewModel(UserViewModel()), navController = rememberNavController())
    }
}
