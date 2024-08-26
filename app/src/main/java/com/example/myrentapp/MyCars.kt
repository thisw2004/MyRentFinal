
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
//for navigation
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

//dit is het main scherm dus hier start de app dus vanuit deze alle andere schermen aanroepen
//todo: hoe data meegeven aan andere schermen? staat wellicht in developer.google.com manuals
//veel data moet ook meegegeven worden aan api. >> eerst api werkend krijgen

class MyCars : ComponentActivity() {
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
                    //VerhurenFormLayout()
                    MyCarsLayout(viewModel, rememberNavController())
                }
            }
        }
    }
}


@Composable
fun MyCarsLayout(viewModel: CarViewModel, navController: NavController) {
    //alle velden hier toevoegen!



    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 110.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //title
        Text(
            text = stringResource(R.string.MyCarTitle),
            style = MaterialTheme.typography.displaySmall,
            //placeholder

        )
        //field 1,brand

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //use navhost here,but how?
            Button(onClick = {navController.navigate("OwnedCar1")}){
                Text(stringResource(R.string.Car1Txt))
            }
            Button(onClick = { /*TODO*/ }) {
                Text(stringResource(R.string.Car2Txt))
            }
            Button(onClick = {/*TODO*/ }){
                Text(stringResource(R.string.Car3Txt))
            }
            Button(onClick = { /*TODO*/ }) {
                Text(stringResource(R.string.Car4Txt))
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}




@Preview(showBackground = true)
@Composable
fun MyCarsPreview() {
    MyRentAppTheme {
        MyCarsLayout(CarViewModel(), rememberNavController())
    }
}

