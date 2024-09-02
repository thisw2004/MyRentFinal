package com.example.myrentapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Verhuren : ComponentActivity() {
    private lateinit var viewModel: CarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CarViewModel::class.java)
        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    VerhurenFormLayout(viewModel, navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VerhurenFormLayout(viewModel: CarViewModel, navController: NavController) {
    var brandInput by remember { mutableStateOf("") }
    var modelInput by remember { mutableStateOf("") }
    var buildYearInput by remember { mutableStateOf("") }
    var kentekenInput by remember { mutableStateOf("") }
    var brandstofInput by remember { mutableStateOf("") }
    var verbruikInput by remember { mutableStateOf("") }
    var kmStandInput by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }

    var showPhotoDialog by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoName by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val takePhoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoUri?.let { uri ->
                photoName = getFileNameFromUri(context, uri)
            }
        }
    }

    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        photoUri = uri
        uri?.let {
            photoName = getFileNameFromUri(context, it)
        }
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
            text = stringResource(R.string.hireTitle),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )

        VerhurenInputField(label = R.string.brand, value = brandInput, onValueChange = { brandInput = it })
        VerhurenInputField(label = R.string.model, value = modelInput, onValueChange = { modelInput = it })
        VerhurenInputField(label = R.string.bouwjaar, value = buildYearInput, onValueChange = { buildYearInput = it })
        VerhurenInputField(label = R.string.kenteken, value = kentekenInput, onValueChange = { kentekenInput = it })
        VerhurenInputField(label = R.string.fuelType, value = brandstofInput, onValueChange = { brandstofInput = it })
        VerhurenInputField(label = R.string.Usage, value = verbruikInput, onValueChange = { verbruikInput = it })
        VerhurenInputField(label = R.string.Mileage, value = kmStandInput, onValueChange = { kmStandInput = it })
        VerhurenInputField(label = R.string.Location, value = locationInput, onValueChange = { locationInput = it })

        Button(
            onClick = { showPhotoDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(stringResource(R.string.ChoosePhotoBtn))
        }

        photoName?.let {
            Text(
                text = "$it uploaded",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                // TODO: Implement confirmation logic
                // This is where you would save all the input data, including the photoUri
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(stringResource(R.string.Confirm))
        }

        Spacer(modifier = Modifier.height(150.dp))
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
                        photoUri = createImageUri(context)
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
    Text(
        text = stringResource(label),
        modifier = Modifier
            .padding(bottom = 8.dp, top = 16.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Start
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

fun createImageUri(context: Context): Uri {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val datePart = dateFormat.format(Date())

    val storageDir: File? = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
    val existingFiles = storageDir?.listFiles { file ->
        file.name.startsWith(datePart)
    } ?: emptyArray()

    val sequenceNumber = existingFiles.size + 1
    val imageFileName = "${datePart}_${sequenceNumber}.jpg"

    val image = File(storageDir, imageFileName)

    return FileProvider.getUriForFile(
        context,
        "com.example.myrentapp.fileprovider",
        image
    )
}

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var result = "photo"  // Default name
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    result = it.getString(displayNameIndex)
                }
            }
        }
    }
    if (result == "photo") {
        // If we couldn't get the name from the content resolver, extract it from the URI
        result = uri.path?.let { File(it).name } ?: "photo"
    }
    return result
}

@Preview(showBackground = true)
@Composable
fun VerhurenFormLayoutPreview() {
    MyRentAppTheme {
        VerhurenFormLayout(viewModel = CarViewModel(), navController = rememberNavController())
    }
}