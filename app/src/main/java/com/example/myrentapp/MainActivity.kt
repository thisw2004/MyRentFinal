
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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
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
                            //home screen
                            composable("Login"){
                                LoginFormLayout(sharedViewModel,navController)
                            }

                            composable("Register"){
                                RegisterFormLayout(sharedViewModel,navController)
                            }

                            composable("HomeScreen"){
                                HomeScreenLayout(sharedViewModel,navController)
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
            .statusBarsPadding()
            .padding(horizontal = 16.dp) // Verminderde horizontale padding
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Button "Inloggen" verplaatst naar buiten de Column met de titel

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("Login") },
            modifier = Modifier
                 // De volledige breedte van de rij innemen
                .align(Alignment.End)
                //.padding(end = 16.dp) // Rechtse padding toevoegen
        ) {
            Text(
                text = stringResource(R.string.Login),
                style = MaterialTheme.typography.bodySmall // Kleinere tekstgrootte
            )
        }

        Spacer(modifier = Modifier.height(175.dp))

        // Titel met vetgedrukte stijl en verspreide tekst over het scherm
        Text(
            text = stringResource(R.string.MainTitle),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Intro tekst met kleinere tekstgrootte en verspreide tekst over het scherm
        Text(
            text = stringResource(R.string.IntroTxt),
            style = MaterialTheme.typography.bodyMedium, // Kleinere tekstgrootte
            modifier = Modifier.fillMaxWidth() // Tekst verspreiden over het scherm
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Navigatieknop "Registreren" met verspreide tekst over het scherm
            Button(
                onClick = { navController.navigate("Register") },
                modifier = Modifier.fillMaxWidth() // Knop de volledige breedte van de rij laten innemen
            ) {
                Text(stringResource(R.string.RegisterTitle))
            }

            Spacer(modifier = Modifier.height(15.dp))
        }

        Spacer(modifier = Modifier.height(150.dp))
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

