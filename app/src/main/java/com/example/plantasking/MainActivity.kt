package com.example.plantasking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.example.plantasking.ui.theme.PlantAskingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantAskingTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF121212)
                ) { innerPadding ->
                    LoginScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val textFieldsColors = OutlinedTextFieldDefaults.colors(
      focusedTextColor = Color.White,
      unfocusedTextColor = Color.White,
      focusedBorderColor = Color.Transparent,
      unfocusedBorderColor = Color.Transparent,
      focusedContainerColor = Color.Transparent,
      unfocusedContainerColor = Color.Transparent,
      focusedLabelColor = Color.White,
      unfocusedLabelColor = Color.White,
      cursorColor = Color.White
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF03A898),
                        Color(red = 14, green = 174, blue = 138, alpha = 255),
                        Color(0xFF19B27A)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
            Text(
                text = "Bem vindo ao PlantAsking",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                        Text(
                            "Email",
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold)
                        },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                colors = textFieldsColors
            )
            Spacer(
                modifier = Modifier
                    .height(3.dp)
                    .width(335.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(percent = 100)
                    )
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        "Senha",
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold)
                },
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                colors = textFieldsColors
            )
            Spacer(
                modifier = Modifier
                    .height(3.dp)
                    .width(335.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(percent = 100)
                    )
            )
            Spacer(
                modifier = Modifier
                    .height(30.dp)
            )
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .width(320.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(percent = 15),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Entrar",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(
                modifier = Modifier
                    .height(30.dp)
            )
            Row {
                Text(
                    text = "Esqueceu a senha? ",
                    color = Color.White,
                    fontSize = 15.sp
                )
                Text(
                    text = "Clique aqui",
                    color = Color.Blue.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        
                    }
                )
            }

        }
    }
}
@Composable
fun HomeScreen() {

}
@Preview()
@Composable
fun TelaInicial() {
    var loggedIn by remember { mutableStateOf(false) }
    PlantAskingTheme {
        if (loggedIn) {
            HomeScreen()
        }
        else{
            LoginScreen()
        }
    }
}
