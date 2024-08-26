package com.example.myrentapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterFormLayout(viewModel: SharedViewModel, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val registerState by viewModel.registerState.collectAsState()

    // Reset the ViewModel state when entering this screen
    LaunchedEffect(Unit) {
        viewModel.logout()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (registerState) {
            is RegisterState.Idle, is RegisterState.Error -> {
                Button(
                    onClick = { viewModel.register(username, password, email) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
                if (registerState is RegisterState.Error) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = (registerState as RegisterState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is RegisterState.Loading -> {
                CircularProgressIndicator()
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