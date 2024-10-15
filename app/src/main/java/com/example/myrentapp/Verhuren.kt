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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import com.example.myrentapp.AddVehicleState
import com.example.myrentapp.CarViewModel
import com.example.myrentapp.R
import com.example.myrentapp.UserViewModel
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
    var location by remember { mutableStateOf<String?>(null) }  // Auto GPS location

    var showPhotoDialog by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoName by remember { mutableStateOf<String?>(null) }
    var photoBase64 by remember { mutableStateOf<String?>(null) }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }  // For preview

    // Permission states for location and camera
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // Launcher for taking a photo
    val takePhoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoUri?.let { uri ->
                photoBase64 = encodeImageToBase64(context, uri)
                val bitmap = decodeUriToBitmap(context, uri)
                photoBitmap = bitmap  // Set photo preview bitmap
                photoName = carViewModel.generatePhotoName(context)
            }
        }
    }

    // Launcher for picking a photo from the gallery
    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        photoUri = uri
        if (uri != null) {
            photoBase64 = encodeImageToBase64(context, uri)
            val bitmap = decodeUriToBitmap(context, uri)
            photoBitmap = bitmap  // Set photo preview bitmap
            photoName = carViewModel.generatePhotoName(context)
        }
    }

    // Fetch current location if permission is granted
    val scope = rememberCoroutineScope()
    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            scope.launch {
                getCurrentLocation(context) { gpsCoordinates ->
                    location = gpsCoordinates  // Save GPS coordinates
                }
            }
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.hireTitle),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input fields
        VerhurenInputField(label = R.string.brand, value = brandInput, onValueChange = { brandInput = it })
        VerhurenInputField(label = R.string.model, value = modelInput, onValueChange = { modelInput = it })
        VerhurenInputField(label = R.string.bouwjaar, value = buildYearInput, onValueChange = { buildYearInput = it })
        VerhurenInputField(label = R.string.kenteken, value = kentekenInput, onValueChange = { kentekenInput = it })
        VerhurenInputField(label = R.string.fuelType, value = brandstofInput, onValueChange = { brandstofInput = it })
        VerhurenInputField(label = R.string.Usage, value = verbruikInput, onValueChange = { verbruikInput = it })
        VerhurenInputField(label = R.string.Mileage, value = kmStandInput, onValueChange = { kmStandInput = it })

        // Show photo preview if available
        photoBitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = "Selected Photo", modifier = Modifier.size(200.dp))
        }

        // Photo selection buttons
        Button(
            onClick = { showPhotoDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(stringResource(R.string.ChoosePhotoBtn))
        }

        // Display selected photo name if available
        photoName?.let {
            Text(
                text = "Photo selected: $it",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Add vehicle button
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
                        location ?: "",  // Use the automatically fetched location
                        photoBase64
                    )
                } else {
                    navController.navigate("login")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(if (isLoggedIn) stringResource(R.string.Confirm) else "Login to Add Vehicle")
        }

        when (val state = addVehicleState) {
            is AddVehicleState.Loading -> CircularProgressIndicator()
            is AddVehicleState.Success -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.primary,
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
            else -> {} // Idle state, do nothing
        }
    }

    // Show dialog for photo selection (camera or gallery)
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

// Get current GPS location function
fun getCurrentLocation(context: Context, onLocationReceived: (String) -> Unit) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Request fine location (precise GPS-based location)
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {

        // Use GPS as the best provider for high accuracy
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude
                val gpsCoordinates = "$latitude,$longitude"
                onLocationReceived(gpsCoordinates)  // Pass precise location to the callback
                locationManager.removeUpdates(this)  // Stop updates after the first location is received
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        })
    }
}

// Encode image to Base64 string
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

// Create a URI for saving the photo using FileProvider
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
