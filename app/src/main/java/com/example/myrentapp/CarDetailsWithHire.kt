package com.example.myrentapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun CarDetailsWithHireLayout(carViewModel: CarViewModel, navController: NavController, carId: String) {
    val vehicleState by carViewModel.getVehicleById(carId).collectAsState(initial = VehicleState.Loading)
    val context = LocalContext.current
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(vehicleState) {
        if (vehicleState is VehicleState.Success) {
            val vehicle = (vehicleState as VehicleState.Success).vehicle
            vehicle.photoId?.let {
                photoBitmap = decodeBase64ToBitmapHire(it)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (vehicleState) {
            is VehicleState.Loading -> {
                CircularProgressIndicator()
            }
            is VehicleState.Success -> {
                val vehicle = (vehicleState as VehicleState.Success).vehicle
                Text(
                    text = "${vehicle.brand} ${vehicle.model}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Show photo preview if available
                photoBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Car Photo",
                        modifier = Modifier.size(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display vehicle details
                Text(
                    text = "Build Year: ${vehicle.buildYear}\n" +
                            "License Plate: ${vehicle.kenteken}\n" +
                            "Fuel Type: ${vehicle.brandstof}\n" +
                            "Usage: ${vehicle.verbruik} L/100km\n" +
                            "Mileage: ${vehicle.kmstand} km",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button to hire the car
                Button(onClick = {
                    carViewModel.hireVehicle(carId) {
                        // On success, navigate to "My Cars" page
                        navController.navigate("myCars")
                    }
                }) {
                    Text("Hire Car")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display location on the map if available
                vehicle.location.split(",").let { coordinates ->
                    if (coordinates.size == 2) {
                        val lat = coordinates[0].toDoubleOrNull()
                        val lng = coordinates[1].toDoubleOrNull()
                        if (lat != null && lng != null) {
                            GoogleMapViewHire(LatLng(lat, lng))
                        }
                    }
                }
            }
            is VehicleState.Error -> {
                Text(
                    text = (vehicleState as VehicleState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun GoogleMapViewHire(location: LatLng) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    DisposableEffect(mapView) {
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync { googleMap ->
            MapsInitializer.initialize(context)
            googleMap.addMarker(MarkerOptions().position(location).title("Car Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
        }
        onDispose {
            mapView.onPause()
            mapView.onDestroy()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

// Decode Base64 string to Bitmap
fun decodeBase64ToBitmapHire(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        Log.e("CarDetailsWithHireLayout", "Error decoding Base64 string to Bitmap", e)
        null
    }
}

@Preview(showBackground = true)
@Composable
fun CarDetailsWithHireWithHireLayoutPreview() {
    val dummyViewModel = object : CarViewModel(UserViewModel()) {
        override fun getVehicleById(carId: String): StateFlow<VehicleState> {
            return MutableStateFlow(
                VehicleState.Success(
                    VehicleResponse(
                        id = 85,
                        rented = true,
                        userId = null,
                        brand = "Toyota",
                        model = "Sedan 3",
                        buildYear = 2019,
                        kenteken = "FC21-IZC",
                        brandstof = "Hybrid",
                        verbruik = 10,
                        kmstand = 22726,
                        photoId = "iVBORw0KGgoAAAANSUhEUgAAAJYAAACWCAIAAACzY+a1AAABcUlEQVR4Xu3RMQ0AMAzAsO4Yf2bFNABDYCmSr7yZuye0+VMsLeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5LeS1kNdCXgt5DymvzQlSn0VTAAAAAElFTkSuQmCC",
                        location = "55.7558,37.6173"
                    )
                )
            )
        }
    }
    MyRentAppTheme {
        CarDetailsWithHireLayout(dummyViewModel, rememberNavController(), carId = "85")
    }
}
