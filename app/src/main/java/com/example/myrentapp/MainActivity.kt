
package com.example.myrentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.myrentapp.ui.theme.MyRentAppTheme

//for navigation
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModelProvider


class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyRentAppTheme {
                Surface(

                    modifier = Modifier.fillMaxSize(),
                ) {

                    //added some navigation
                    run {
                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = "mymainscreen"
                        ) {
                            composable("mymainscreen") {
                                MyMainscreenLayout(navController)
                            }
                            composable("catalogus") {
                                CatalogusLayout(sharedViewModel,navController)
                            }
                            composable("myCars") {
                                MyCarsLayout(sharedViewModel,navController)
                            }
                            composable("OwnedCar1") {
                                CarViewOwnedCarLayout(sharedViewModel,navController)
                            }
                            composable("AvailableCar1") {
                                CarViewAvailableCarLayout(sharedViewModel,navController)
                            }
                            composable("verhuren") {
                                VerhurenFormLayout(sharedViewModel)
                            }
                            composable("huren") {
                                HurenLayout()
                            }
                            composable("looproute") {
                                CalcLooprouteLayout()
                            }
                            //take photo
                            composable("takePhoto") {
                                MaakFotoLayout()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyMainscreenLayout(navController: NavHostController) {

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
//            Button(onClick = { navController.navigate(("huren")) }) {
//                Text(stringResource(R.string.HireCar))
//            }
//            Button(onClick = { navController.navigate("looproute")}) {
//                Text(stringResource(R.string.CalcLooproute))
//            }
    }
}

@Preview(showBackground = true)
@Composable
fun MyMainScreenLayoutPreview() {
    val navController = rememberNavController()

    MyRentAppTheme {
        MyMainscreenLayout(navController)
    }
}

