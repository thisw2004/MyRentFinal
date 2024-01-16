package com.example.myrentapp


// SharedViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class SharedViewModel : ViewModel() {
    var navController: NavController? = null
}
