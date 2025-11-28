package com.example.plantasking.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantasking.R
import com.example.plantasking.data.enum.Author


data class Message(
    val text: String, val author: Author
)

@Composable
fun TextChat(onMessageSend: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val textFieldsColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.White,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White,
        cursorColor = Color.White
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(
                    "Digite uma mensagem...",
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier
                .weight(0.7f)
                .padding(top = 15.dp, start = 16.dp),
            shape = RoundedCornerShape(percent = 15),
            colors = textFieldsColors
        )
        IconButton(
            onClick = {
                if (text.isNotBlank()) {
                    onMessageSend(text)
                    text = ""
                }
            },
            modifier = Modifier
                .padding(start = 15.dp, top = 15.dp, end = 16.dp)
                .width(60.dp)
                .height(60.dp)
                .background(
                    Color(0xC6FF9100), shape = RoundedCornerShape(percent = 50)
                ),
            enabled = text.isNotBlank(),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar Mensagem",
                tint = Color.White
            )
        }
    }

}

@Composable
fun MessageBubble(message: Message) {
    val horizontalArrangement =
        if (message.author == Author.USER) Arrangement.End else Arrangement.Start

    val bubbleColor = if (message.author == Author.USER) {
        Color(0x7CF6F049)
    } else {
        Color(0xC6FF9100)
    }
    val bubbleShape = if (message.author == Author.USER) {
        RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 12.dp, bottomEnd = 20.dp)
    } else {
        RoundedCornerShape(topStart = 0.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 12.dp)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {

        if (message.author == Author.BOT) {
            Image(
                painter = painterResource(id = R.drawable.iconphoto),
                contentDescription = "Plant",
                modifier = Modifier.size(40.dp)
            )
            Box(
                modifier = Modifier
                    .clip(bubbleShape)
                    .background(bubbleColor)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = message.text, color = Color.White
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(bubbleShape)
                    .background(bubbleColor)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = message.text, color = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "User",
                modifier = Modifier
                    .size(35.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun ChatMenu(
    onBackClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(Color(0x23F6F049)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier
                .weight(0.15f)
                .fillMaxHeight()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Enviar Mensagem",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.iconphoto),
            contentDescription = "Plant",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterVertically),

            )
        Text(
            text = "Sua amiga planta",
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(start = 15.dp),
            color = Color.White,
            fontSize = 18.sp
        )
    }


}


@Composable
fun ChatScreen() {
    val messages = listOf(
        Message("Olá! Como posso te ajudar com sua planta hoje?", Author.BOT),
        Message("Oi! As folhas dela estão meio amareladas...", Author.USER),
        Message(
            "Entendi. Folhas amareladas podem indicar algumas coisas. Qual foi a última vez que você a regou?",
            Author.BOT
        ),
        Message("Acho que foi anteontem.", Author.USER)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF80E2825))
            .drawBehind {
                val dotColor = Color(0xFF19B27A).copy(alpha = 0.08f)
                val dotRadius = 1.dp.toPx()
                val dotSpacing = 24.dp.toPx()

                for (x in 0..size.width.toInt() step dotSpacing.toInt()) {
                    for (y in 0..size.height.toInt() step dotSpacing.toInt()) {
                        drawCircle(
                            color = dotColor,
                            radius = dotRadius,
                            center = androidx.compose.ui.geometry.Offset(x.toFloat(), y.toFloat())
                        )
                    }
                }
            }) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatMenu(onBackClicked = {})
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp), reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    MessageBubble(message = message)
                }
            }
            TextChat(onMessageSend = {})
        }
    }
}
