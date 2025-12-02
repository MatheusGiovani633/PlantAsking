package com.example.plantasking.ui

import android.net.Uri
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
                PlantAsKingApp()
            }
        }
    }
}

@Composable
fun PlantAsKingApp(modifier: Modifier = Modifier) {
    var isLoggedIn by remember { mutableStateOf(false) }
    var isInitChat by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    if (!isLoggedIn) {
        LoginScreen(
            modifier = modifier,
            onLoginSuccess = { isLoggedIn = true }
        )
    } else if (isInitChat) {
        ChatScreen(
            onBackClicked = { isInitChat = false },
            plantImageUri = capturedImageUri
        )
    } else {
        HomeScreen(
            modifier = modifier,
            onInitChat = { imageUri ->
                capturedImageUri = imageUri
                isInitChat = true
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlantAskingTheme {
        PlantAsKingApp()
    }
}
