package com.example.plantasking.ui.chat.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Passo 1: Definir os tipos de dados para o chat
enum class MessageAuthor {
    USER, BOT
}

data class Message(
    val text: String,
    val author: MessageAuthor
)

// Passo 2: Construir a visualização de uma única bolha de mensagem
@Composable
fun MessageBubble(message: Message) {
    // Determina o alinhamento (esquerda para o BOT, direita para o USER)
    val horizontalArrangement = if (message.author == MessageAuthor.USER) Arrangement.End else Arrangement.Start

    // Determina a cor da bolha
    val bubbleColor = if (message.author == MessageAuthor.USER) {
        Color(0xFF075E54) // Verde escuro para o usuário
    } else {
        Color(0xFF233138) // Cinza escuro para o bot
    }

    // Determina a forma dos cantos, para dar o efeito "WhatsApp"
    val bubbleShape = if (message.author == MessageAuthor.USER) {
        RoundedCornerShape(topStart = 12.dp, topEnd = 4.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        Box(
            modifier = Modifier
                .clip(bubbleShape)
                .background(bubbleColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White
            )
        }
    }
}


@Preview(
    name = "Tela de Chat", showBackground = true
)
@Composable
private fun ChatScreenPreview(modifier: Modifier = Modifier) {
    // Passo 3: Criar a lista de mensagens MOCKADA (de exemplo)
    val messages = listOf(
        Message("Olá! Como posso te ajudar com sua planta hoje?", MessageAuthor.BOT),
        Message("Oi! As folhas dela estão meio amareladas...", MessageAuthor.USER),
        Message("Entendi. Folhas amareladas podem indicar algumas coisas. Qual foi a última vez que você a regou?", MessageAuthor.BOT),
        Message("Acho que foi anteontem.", MessageAuthor.USER)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E2825)) // Verde bem escuro
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
            }
    ) {
        // Passo 4: Usar a lista mockada na LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            reverseLayout = true // Mensagens novas aparecem por baixo e o scroll começa de baixo
        ) {
            // A lista é invertida para funcionar corretamente com o reverseLayout
            items(messages.reversed()) { message ->
                MessageBubble(message = message)
            }
        }
    }
}
