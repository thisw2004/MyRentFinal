package com.example.myrentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme
import androidx.navigation.NavController
import androidx.compose.ui.text.style.TextAlign  // Add this import for the align modifier

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
    var buildInput by remember { mutableStateOf("") }
    var kentekenInput by remember { mutableStateOf("") }
    var fuelInput by remember { mutableStateOf("") }

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
            textAlign = TextAlign.Center  // Use textAlign instead of align for Text
        )

        VerhurenInputField(label = R.string.brand, value = brandInput, onValueChange = { brandInput = it })
        VerhurenInputField(label = R.string.model, value = modelInput, onValueChange = { modelInput = it })
        VerhurenInputField(label = R.string.bouwjaar, value = buildInput, onValueChange = { buildInput = it })
        VerhurenInputField(label = R.string.kenteken, value = kentekenInput, onValueChange = { kentekenInput = it })
        VerhurenInputField(label = R.string.fuelType, value = fuelInput, onValueChange = { fuelInput = it })

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("takePhoto") }) {
                Text(stringResource(R.string.TakePhoto))
            }
            Button(onClick = { /* TODO: Implement confirmation logic */ }) {
                Text(stringResource(R.string.Confirm))
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun VerhurenInputField(label: Int, value: String, onValueChange: (String) -> Unit) {
    Text(
        text = stringResource(label),
        modifier = Modifier
            .padding(bottom = 16.dp, top = 10.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Start  // Use textAlign instead of align for Text
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun VerhurenFormLayoutPreview() {
    MyRentAppTheme {
        VerhurenFormLayout(viewModel = CarViewModel(), navController = rememberNavController())
    }
}