// C:/.../PlantAsking/app/src/main/java/com/example/plantasking/MainActivity.kt

package com.example.plantasking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.plantasking.ui.screens.HomeScreen
import com.example.plantasking.ui.screens.LoginScreen
import com.example.plantasking.ui.theme.PlantAskingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantAskingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlantAskingApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PlantAskingApp(modifier: Modifier = Modifier) {
    var isLoggedIn by remember { mutableStateOf(false) }
    if (isLoggedIn) {
        HomeScreen(modifier = modifier)
    } else {
        LoginScreen(
            modifier = modifier,
            onLoginSuccess = { isLoggedIn = true }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlantAskingTheme {
        PlantAskingApp()
    }
}
