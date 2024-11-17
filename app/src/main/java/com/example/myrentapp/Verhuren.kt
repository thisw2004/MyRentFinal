package com.example.myrentapp

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import com.example.myrentapp.ui.theme.MyRentAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VerhurenFormLayout(carViewModel: CarViewModel, userViewModel: UserViewModel, navController: NavController) {
    val context = LocalContext.current
    val addVehicleState by carViewModel.addVehicleState.collectAsState()
    val isLoggedIn by remember { derivedStateOf { userViewModel.isLoggedIn() } }

    var brandInput by remember { mutableStateOf("") }
    var modelInput by remember { mutableStateOf("") }
    var buildYearInput by remember { mutableStateOf("") }
    var kentekenInput by remember { mutableStateOf("") }
    var brandstofInput by remember { mutableStateOf("") }
    var verbruikInput by remember { mutableStateOf("") }
    var kmStandInput by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<String?>(null) }

    var showPhotoDialog by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoName by remember { mutableStateOf<String?>(null) }
    var photoBase64 by remember { mutableStateOf<String?>(null) }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val takePhoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoUri?.let { uri ->
                photoBase64 = encodeImageToBase64(context, uri)
                val bitmap = decodeUriToBitmap(context, uri)
                photoBitmap = bitmap
                photoName = carViewModel.generatePhotoName(context)
            }
        }
    }

    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        photoUri = uri
        if (uri != null) {
            photoBase64 = encodeImageToBase64(context, uri)
            val bitmap = decodeUriToBitmap(context, uri)
            photoBitmap = bitmap
            photoName = carViewModel.generatePhotoName(context)
        }
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            scope.launch {
                getCurrentLocation(context) { gpsCoordinates ->
                    location = gpsCoordinates
                }
            }
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1D1E)) // Dark background color
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.hireTitle),
            style = MaterialTheme.typography.displaySmall.copy(
                color = Color(0xFF4AC0FF),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        VerhurenInputField(label = R.string.brand, value = brandInput, onValueChange = { brandInput = it })
        VerhurenInputField(label = R.string.model, value = modelInput, onValueChange = { modelInput = it })
        VerhurenInputField(label = R.string.bouwjaar, value = buildYearInput, onValueChange = { buildYearInput = it })
        VerhurenInputField(label = R.string.kenteken, value = kentekenInput, onValueChange = { kentekenInput = it })
        VerhurenInputField(label = R.string.fuelType, value = brandstofInput, onValueChange = { brandstofInput = it })
        VerhurenInputField(label = R.string.Usage, value = verbruikInput, onValueChange = { verbruikInput = it })
        VerhurenInputField(label = R.string.Mileage, value = kmStandInput, onValueChange = { kmStandInput = it })

        photoBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Selected Photo",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(vertical = 16.dp)
            )
        }

        Button(
            onClick = { showPhotoDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4AC0FF),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(stringResource(R.string.ChoosePhotoBtn), fontWeight = FontWeight.SemiBold)
        }

        photoName?.let {
            Text(
                text = "Photo selected: $it",
                color = Color(0xFFB0BEC5),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                if (isLoggedIn) {
                    carViewModel.addVehicle(
                        brandInput,
                        modelInput,
                        buildYearInput.toIntOrNull() ?: 0,
                        kentekenInput,
                        brandstofInput,
                        verbruikInput.toIntOrNull() ?: 0,
                        kmStandInput.toIntOrNull() ?: 0,
                        location ?: "",
                        photoBase64
                    )
                } else {
                    navController.navigate("login")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4AC0FF),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(if (isLoggedIn) stringResource(R.string.Confirm) else "Login to Add Vehicle", fontWeight = FontWeight.SemiBold)
        }

        when (val state = addVehicleState) {
            is AddVehicleState.Loading -> CircularProgressIndicator(color = Color(0xFF4AC0FF))
            is AddVehicleState.Success -> {
                Text(
                    text = state.message,
                    color = Color(0xFF4AC0FF),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LaunchedEffect(state) {
                    navController.popBackStack()
                }
            }
            is AddVehicleState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            else -> {}
        }
    }

    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Choose an option") },
            text = { Text("Would you like to take a new photo or select an existing one?") },
            confirmButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    if (cameraPermissionState.status.isGranted) {
                        photoUri = createImageUri(context, carViewModel)
                        takePhoto.launch(photoUri)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                }) {
                    Text("Take Photo")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPhotoDialog = false
                    pickPhoto.launch("image/*")
                }) {
                    Text("Select Photo")
                }
            }
        )
    }
}

@Composable
fun VerhurenInputField(label: Int, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

fun decodeUriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        Log.e("VerhurenFormLayout", "Error decoding URI to Bitmap", e)
        null
    }
}

fun getCurrentLocation(context: Context, onLocationReceived: (String) -> Unit) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude
                val gpsCoordinates = "$latitude,$longitude"
                onLocationReceived(gpsCoordinates)
                locationManager.removeUpdates(this)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        })
    }
}

fun encodeImageToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        Base64.encodeToString(byteArray, Base64.DEFAULT)
    } catch (e: Exception) {
        Log.e("VerhurenFormLayout", "Error encoding image to Base64", e)
        null
    }
}

fun createImageUri(context: Context, viewModel: CarViewModel): Uri {
    val photoName = viewModel.generatePhotoName(context)
    val storageDir: File? = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
    val image = File(storageDir, "$photoName.jpg")
    return FileProvider.getUriForFile(
        context,
        "com.example.myrentapp.fileprovider",
        image
    )
}
