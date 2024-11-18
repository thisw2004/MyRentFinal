package com.example.myrentapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myrentapp.ui.theme.MyRentAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterFormLayout(viewModel: UserViewModel, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val registerState by viewModel.registerState.collectAsState()

    // Accent and background colors
    val backgroundColor = Color(0xFF1A1D1E) // Dark background color
    val accentColor = Color(0xFF4AC0FF) // Light blue accent color

    // Reset the ViewModel state when entering this screen
    LaunchedEffect(Unit) {
        viewModel.logout()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp)) // Move title down
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = accentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Username input field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = accentColor,
                cursorColor = accentColor,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = accentColor,
                unfocusedLabelColor = Color.Gray,
                containerColor = Color.Transparent // Set container color to transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = accentColor,
                cursorColor = accentColor,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = accentColor,
                unfocusedLabelColor = Color.Gray,
                containerColor = Color.Transparent // Set container color to transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Email input field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = accentColor,
                cursorColor = accentColor,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = accentColor,
                unfocusedLabelColor = Color.Gray,
                containerColor = Color.Transparent // Set container color to transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (registerState) {
            is RegisterState.Idle, is RegisterState.Error -> {
                Button(
                    onClick = { viewModel.register(username, password, email) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Register", fontWeight = FontWeight.SemiBold)
                }
                if (registerState is RegisterState.Error) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = (registerState as RegisterState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            is RegisterState.Loading -> {
                CircularProgressIndicator(color = accentColor)
            }
            is RegisterState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate("HomeScreen") {
                        popUpTo("Register") { inclusive = true }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterFormLayoutPreview() {
    MyRentAppTheme {
        val dummyViewModel = UserViewModel()
        RegisterFormLayout(dummyViewModel, rememberNavController())
    }
}
