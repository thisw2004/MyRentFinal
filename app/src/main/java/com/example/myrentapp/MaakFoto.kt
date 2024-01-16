
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
import com.example.myrentapp.ui.theme.MyRentAppTheme
import java.text.NumberFormat

class MaakFoto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyRentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MaakFotoLayout()
                }
            }
        }
    }
}

@Composable
fun MaakFotoLayout() {
    //hier zorgen dat camera geactiveerd wordt en er een foto gemaakt kan worden...


    //als foto goedgekeurd is die laten zien in form of evt als geupload like image1.jpg oid
    //daarna foto inc ingevulde data in database gooien dmv api? >> eerst api werkend krijgen dan.
    //kan evt voor nu in xampp db
    //TODO:rekening houden met permissies!!!!


}


@Preview(showBackground = true)
@Composable
fun MaakFotoPreview() {
    MyRentAppTheme {
        MaakFotoLayout()
    }
}

