// C:/.../PlantAsking/app/src/main/java/com/example/plantasking/MainActivity.kt

package com.example.plantasking.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.plantasking.ui.home.HomeScreen
import com.example.plantasking.ui.login.LoginScreen
import com.example.plantasking.ui.theme.PlantAskingTheme
import com.example.plantasking.ui.chat.ChatScreen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantAskingTheme {
                PlantAskingApp()
            }
        }
    }
}

@Composable
fun PlantAskingApp(modifier: Modifier = Modifier) {
    var isLoggedIn by remember { mutableStateOf(false) }
    var isInitChat by remember { mutableStateOf(false) }
    if (isLoggedIn) {
        HomeScreen(
            modifier = modifier,
            onInitChat = {
               if(isLoggedIn && !isInitChat){
                   isInitChat = true
               }
                if(isInitChat){
                   ChatScreen()
               }
            },
        )
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
