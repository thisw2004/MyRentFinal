
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myrentapp.ui.theme.MyRentAppTheme
import java.text.NumberFormat
/*for navigation*/
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

//dit is het main scherm dus hier start de app dus vanuit deze alle andere schermen aanroepen
//todo: hoe data meegeven aan andere schermen? staat wellicht in developer.google.com manuals
//veel data moet ook meegegeven worden aan api. >> eerst api werkend krijgen

class HomeScreen : ComponentActivity() {
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
                    HomeScreenLayout(viewModel, rememberNavController())
                }
            }
        }
    }
}


@Composable
fun HomeScreenLayout(viewModel: SharedViewModel,navController: NavController) {


    Column(

        modifier = Modifier
            //.statusBarsPadding()
            .padding(horizontal = 150.dp)
            //.verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //title
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.homeTitle),
            style = MaterialTheme.typography.displaySmall,
            //placeholder
        )
    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            //.verticalScroll(rememberScrollState())
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Button(onClick = { navController.navigate("catalogus")}) {
            Text(stringResource(R.string.CatalogusTitle))
        }
        Button(onClick = { navController.navigate("myCars")}) {
            Text(stringResource(R.string.MyCarTitle))
        }
        Button(onClick = { navController.navigate("verhuren")
        }) {
            Text(stringResource(R.string.RentCar))
        }
        //was eigenlijk hetzelfde als catalogus,omdat je naar catalogus gaat om een car te bekijken en te huren
//
    }
}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyRentAppTheme {
        HomeScreenLayout(SharedViewModel(), rememberNavController())
    }
}

