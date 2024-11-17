package com.example.myrentapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme

@Composable
fun HomeScreenLayout(sharedViewModel: UserViewModel, navController: NavController) {
    val userSessionState = sharedViewModel.userSession.collectAsState()
    val username = userSessionState.value?.username ?: "User"

    // Define the primary and accent colors based on the design
    val backgroundColor = Color(0xFF1A1D1E) // Dark background color
    val accentColor = Color(0xFF4AC0FF) // Light blue accent color

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display a styled "Home" subtitle


        // Add extra spacer to move the welcome text down
        Spacer(modifier = Modifier.height(52.dp))

        // Display username greeting
        Text(
            text = "Welcome, $username",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Buttons for navigation
        Button(
            onClick = { navController.navigate("catalogus") },
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(55.dp)
        ) {
            Text("Catalogus", fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = { navController.navigate("myCars") },
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(55.dp)
        ) {
            Text("My Hired Cars", fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = { navController.navigate("verhuren") },
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(55.dp)
        ) {
            Text("Rent Your Car", fontWeight = FontWeight.SemiBold)
        }

        // Adjust the bottom spacer to move the logout button up
        Spacer(modifier = Modifier.height(350.dp))

        // Logout button
        Button(
            onClick = {
                // Implement logout logic
                sharedViewModel.logout()
                navController.navigate("mymainscreen") {
                    popUpTo("mymainscreen") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text("Logout", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenLayoutPreview() {
    MyRentAppTheme {
        HomeScreenLayout(UserViewModel(), rememberNavController())
    }
}
