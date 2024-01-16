
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
import com.example.myrentapp.ui.theme.MyRentAppTheme
import java.text.NumberFormat
//dit is het main scherm dus hier start de app dus vanuit deze alle andere schermen aanroepen
//todo: hoe data meegeven aan andere schermen? staat wellicht in developer.google.com manuals

class Huren : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    HurenLayout()
                }
            }
        }
    }
}

@Composable
fun HurenLayout() {


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
            text = stringResource(R.string.titleHuren),
            style = MaterialTheme.typography.displaySmall,
            //placeholder

        )
        //field 1,brand
        Text(
            //todo:nog check of rijbewijs geldig is.
            //gwn check mark erachter als rijbewijs geldig is en anders melding net boven button dat rijbewijs niet geldig is en kan niet huren.
            //check met data uit api

            text = stringResource(R.string.LicenseValid),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 10.dp)
                .align(alignment = Alignment.Start)
        )
        Text(

            text = stringResource(R.string.MyRentedCarData),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 10.dp)
                .align(alignment = Alignment.Start)
        )
        TextField(
            //TODO: dit in strings gooien.
            //TODO: data die uit api wordt gehaald.
            //evt add artikelnummer en nog diverse dingen? : zie word doc
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
                Text(stringResource(R.string.ConfirmHuren))
            }
            //hierna naar mijn auto's gaan en de gehuurde en verhuurde auto's showen
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditFieldHuren(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        //label hieronder is placeholder maar toont nu dezelfde in alle velden,TODO: deze nog splitsen
        //label = { Text(stringResource(R.string.brand)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}


@Preview(showBackground = true)
@Composable
fun HurenLayoutPreview() {
    MyRentAppTheme {
        HurenLayout()
    }
}

