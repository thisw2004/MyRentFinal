
package com.example.myrentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.myrentapp.ui.theme.MyRentAppTheme
import java.text.NumberFormat
//dit is het main scherm dus hier start de app dus vanuit deze alle andere schermen aanroepen
//todo: hoe data meegeven aan andere schermen? staat wellicht in developer.google.com manuals
/*for navigation*/
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class CarViewAvailableCar : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(CarViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CarViewAvailableCar()
                }
            }
        }
    }
}

//deze is voor als je van mijn auto's komt ;) (voor simulation)
@Composable
fun CarViewAvailableCarLayout(viewModel: CarViewModel, navController: NavController) {


    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //title
        Text(
            //deze titel basen op welke button is geklikt.
            text = stringResource(R.string.Car1Txt),
            style = MaterialTheme.typography.displaySmall,
            //placeholder

        )

        Text(

            text = stringResource(R.string.CarDataTitle),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 10.dp)
                .align(alignment = Alignment.Start)
        )
        TextField(
            //TODO: dit in strings gooien.
            //TODO: data die uit api wordt gehaald.
            //evt add artikelnummer en nog diverse dingen? : zie word doc
            /*value string nog correct in translation editor gooien*/
            value = "Merk: \nModel: \nBouwjaar: \nKenteken: \nBrandstoftype: ",
            onValueChange = { /* No-op. Read-only text field. */ },
            modifier = Modifier.fillMaxSize(),
            //hieronder placeholder maar is niet nodig in dit geval
            //label = { Text("Label") },
            singleLine = false,
            enabled = false
        )


        Spacer(modifier = Modifier.height(15.dp))



        //button for confirming the form and button for taking a photo
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(onClick = { /*TODO*/ }) {
                Text(stringResource(R.string.HireCar))
            }
            //hierna naar mijn auto's gaan en de gehuurde en verhuurde auto's showen
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}




@Preview(showBackground = true)
@Composable
fun CarViewavailableCarPreview() {
    MyRentAppTheme {
        CarViewOwnedCarLayout(CarViewModel(), rememberNavController())
    }
}

