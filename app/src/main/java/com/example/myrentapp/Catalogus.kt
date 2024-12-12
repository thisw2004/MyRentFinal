package com.example.myrentapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme

class Catalogus : ComponentActivity() {
    private lateinit var carViewModel: CarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

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

    val backgroundColor = Color(0xFF1A1D1E)
    val accentColor = Color(0xFF4AC0FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.CatalogusTitle),
            style = MaterialTheme.typography.displaySmall.copy(
                color = accentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            ),
            modifier = Modifier.padding(vertical = 24.dp)
        )

        availableVehicles.forEach { vehicle ->
            CatalogVehicleCard(
                brand = "${vehicle.brand} ${vehicle.model}",
                photoBase64 = vehicle.photoId,
                onClick = {
                    navController.navigate("AvailableCar1/${vehicle.id}")
                }
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun CatalogVehicleCard(brand: String, photoBase64: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(15.dp)),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2E3135)
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            photoBase64?.let {
                val bitmap = remember { base64ToBitmap(it) }
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .padding(bottom = 12.dp)
                    )
                }
            }

            Text(
                text = brand,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFFB0BEC5),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
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
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4AC0FF),
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(vertical = 8.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogusPreview() {
    MyRentAppTheme {

        val dummyViewModel = CarViewModel(UserViewModel())
        CatalogusLayout(dummyViewModel, rememberNavController())
    }
}
