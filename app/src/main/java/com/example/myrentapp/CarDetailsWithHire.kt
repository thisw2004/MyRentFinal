package com.example.myrentapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.myrentapp.ui.theme.MyRentAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CarDetailsWithHireLayout(carViewModel: CarViewModel, navController: NavController, carId: String) {
    val vehicleState by carViewModel.getVehicleById(carId).collectAsState(initial = VehicleState.Loading)
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(vehicleState) {
        if (vehicleState is VehicleState.Success) {
            val vehicle = (vehicleState as VehicleState.Success).vehicle
            vehicle.photoId?.let {
                photoBitmap = decodeBase64ToBitmapHire(it)
            }
        }
    }

    val backgroundColor = Color(0xFF1A1D1E) // Dark background color
    val accentColor = Color(0xFF4AC0FF) // Light blue accent color
    val textColor = Color(0xFFB0BEC5) // Light grey color for text

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (vehicleState) {
            is VehicleState.Loading -> {
                CircularProgressIndicator(color = accentColor)
            }
            is VehicleState.Success -> {
                val vehicle = (vehicleState as VehicleState.Success).vehicle

                Spacer(modifier = Modifier.height(30.dp)) // Add space to move the title down
                Text(
                    text = "${vehicle.brand} ${vehicle.model}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center the title text
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Show photo preview if available
                photoBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Car Photo",
                        modifier = Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .padding(bottom = 15.dp)
                    )
                }

                Text(
                    text = stringResource(id = R.string.carDetailTitle),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = accentColor,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2E3135)),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.buildYearTitle)} ${vehicle.buildYear}",
                            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "${stringResource(id = R.string.Licenseplate)} ${vehicle.kenteken}",
                            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "${stringResource(id = R.string.Fueltype)} ${vehicle.brandstof}",
                            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "${stringResource(id = R.string.Usage)} ${vehicle.verbruik} L/100km",
                            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "${stringResource(id = R.string.Mileage)} ${vehicle.kmstand} km",
                            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

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

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        carViewModel.hireVehicle(carId) {
                            navController.navigate("myCars")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text(stringResource(id = R.string.HireCar), fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(30.dp))

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
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(context.getString(R.string.Location)))
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
            .clip(RoundedCornerShape(15.dp))
            .padding(bottom = 16.dp)
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
