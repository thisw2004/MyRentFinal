package com.example.myrentapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme

@Composable
fun MyMainscreenLayout(navController: NavController) {
    // Define the primary and accent colors based on modern design trends
    val backgroundColor = Color(0xFF1A1D1E) // Dark background color for contrast
    val accentColor = Color(0xFF4AC0FF) // Light blue for accent elements

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Top section with a logo
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        )
        {
            Spacer(modifier = Modifier.height(140.dp))

            Image(
                painter = painterResource(id = R.drawable.car_icon_inverted_no_bg),
                contentDescription = "MyRent Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 20.dp)

            )
        }

        Button(
            onClick = { navController.navigate("Login") },
            colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = Color.Black),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 60.dp, end = 15.dp)
        ) {
            Text("Login")
        }

        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MyRent",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 36.sp,
                    color = accentColor,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to MyRent! Here you can easily find and rent cars at your fingertips. Whether you need a quick ride or want to earn some extra by sharing your own car, we have everything you need. So, let's hit the road together and redefine mobility. Have fun with MyRent!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("Register") },
                colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = Color.Black),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("Register", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyMainscreenLayoutPreview() {
    MyRentAppTheme {
        MyMainscreenLayout(rememberNavController())
    }
}
