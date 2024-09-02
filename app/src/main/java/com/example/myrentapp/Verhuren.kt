package com.example.myrentapp

import android.net.Uri
import android.os.Bundle
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme
import androidx.navigation.NavController

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

    val takePhoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Photo was taken successfully
            // You might want to update the UI to show that a photo was taken
        }
    }

    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        photoUri = uri
        // You might want to update the UI to show that a photo was selected
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
                    // You'll need to create a URI for the new photo here
                    // For simplicity, we're using null, but you should create a proper URI
                    takePhoto.launch(null)
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

@Preview(showBackground = true)
@Composable
fun VerhurenFormLayoutPreview() {
    MyRentAppTheme {
        VerhurenFormLayout(viewModel = CarViewModel(), navController = rememberNavController())
    }
}