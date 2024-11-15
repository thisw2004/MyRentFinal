package com.example.myrentapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreenLayout(sharedViewModel: UserViewModel, navController: NavController) {
    val userSessionState = sharedViewModel.userSession.collectAsState()
    val username = userSessionState.value?.username ?: "User"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display username with a "Home" subtitle
        Text(
            text = "Home",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Welcome, $username",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("catalogus") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Catalogus")
        }

        Button(
            onClick = { navController.navigate("myCars") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("My hired cars")
        }

        Button(
            onClick = { navController.navigate("verhuren") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Rent your car")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Implement logout logic here
                sharedViewModel.logout()
                navController.navigate("mymainscreen") {
                    popUpTo("mymainscreen") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenLayoutPreview() {
    MaterialTheme {
        HomeScreenLayout(UserViewModel(), rememberNavController())
    }
}
