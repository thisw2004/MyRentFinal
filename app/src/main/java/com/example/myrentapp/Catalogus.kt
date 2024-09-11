package com.example.myrentapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme

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
    val availableVehicles by viewModel.availableVehiclesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAvailableVehicles()
    }

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

        availableVehicles.forEach { vehicle ->
            CatalogVehicleItem(
                brand = "${vehicle.brand} ${vehicle.model}",
                photoBase64 = vehicle.photoId,
                onClick = {
                    navController.navigate("AvailableCar1/${vehicle.id}")  // Use the dynamic route with car ID
                }
            )
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun CatalogVehicleItem(brand: String, photoBase64: String?, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        photoBase64?.let {
            val bitmap = remember { base64ToBitmap(it) }
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 8.dp)
                )
            }
        }

        CatalogButton(text = brand, onClick = onClick)
    }
}

fun base64ToBitmap(base64String: String): Bitmap? {
    return try {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } catch (e: Exception) {
        null
    }
}

@Composable
fun CatalogButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text)
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
