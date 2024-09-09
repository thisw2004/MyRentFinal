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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.myrentapp.ui.theme.MyRentAppTheme
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class Catalogus : ComponentActivity() {
    private lateinit var carViewModel: CarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize CarViewModel without explicitly passing UserViewModel
        carViewModel = ViewModelProvider(this).get(CarViewModel::class.java)

        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CatalogusLayout(carViewModel, rememberNavController())
                }
            }
        }
    }
}

@Composable
fun CatalogusLayout(viewModel: CarViewModel, navController: NavController) {
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
            text = stringResource(R.string.CatalogusTitle),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        CatalogButton(text = R.string.Car1Txt) { navController.navigate("AvailableCar1") }
        CatalogButton(text = R.string.Car2Txt) { /* TODO: Implement navigation */ }
        CatalogButton(text = R.string.Car3Txt) { /* TODO: Implement navigation */ }
        CatalogButton(text = R.string.Car4Txt) { /* TODO: Implement navigation */ }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun CatalogButton(text: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(stringResource(text))
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogusPreview() {
    MyRentAppTheme {
        // Create a dummy CarViewModel for preview
        val dummyViewModel = CarViewModel(UserViewModel())
        CatalogusLayout(dummyViewModel, rememberNavController())
    }
}