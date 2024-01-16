
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myrentapp.ui.theme.MyRentAppTheme
import java.text.NumberFormat
//dit is het main scherm dus hier start de app dus vanuit deze alle andere schermen aanroepen
//todo: hoe data meegeven aan andere schermen? staat wellicht in developer.google.com manuals
//veel data moet ook meegegeven worden aan api. >> eerst api werkend krijgen

class Verhuren : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    //VerhurenFormLayout()
                }
            }
        }
    }
}


@Composable
fun VerhurenFormLayout(viewModel: SharedViewModel) {
    //alle velden hier toevoegen!
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
        //title
        Text(
            text = stringResource(R.string.hireTitle),
            style = MaterialTheme.typography.displaySmall,
            //placeholder

        )
        //field 1,brand
        Text(
            text = stringResource(R.string.brand),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 10.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField( //kan meerdere editnumberfields kwaad? dat ze ngeen conflicten rkijgen of elkaar gaan overschrijven zegmaar...
            value = brandInput,
            onValueChanged = { brandInput = it },

            modifier = Modifier
                //.padding(bottom = 32.dp)
                .fillMaxWidth(),

            )
        //field 2,model
        Text(
            text = stringResource(R.string.model),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 7.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            value = modelInput,
            onValueChanged = { modelInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        //field 3,build year
        Text(
            text = stringResource(R.string.bouwjaar),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 7.dp)
                .align(alignment = Alignment.Start)
        )
        //add here placeholder
        EditNumberField(
            value = buildInput,
            onValueChanged = { buildInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        //field 4,kenteken number
        Text(
            text = stringResource(R.string.kenteken),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 7.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            value = kentekenInput,
            onValueChanged = { kentekenInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        //field 5,fuel type
        Text(
            text = stringResource(R.string.fuelType),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 7.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(

            value = fuelInput,
            onValueChanged = { fuelInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )


        //button for confirming the form and button for taking a photo
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //navigate to take photo
            Button(onClick = {viewModel.navController?.navigate("takePhoto") }){
                Text(stringResource(R.string.TakePhoto))
            }
            Button(onClick = { /*TODO*/ }) {
                Text(stringResource(R.string.Confirm))
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        //label = { Text(stringResource(R.string.bill_amount)) } hier if satement basen on what? of apart if in functie😜,
        //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}


@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    MyRentAppTheme {
        VerhurenFormLayout(viewModel())
    }
}

