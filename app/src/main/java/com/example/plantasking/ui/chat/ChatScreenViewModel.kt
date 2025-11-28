package com.example.plantasking.ui.chat

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantasking.data.enum.Author
import com.example.plantasking.data.repository.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val plantRepository = PlantRepository()

    fun sendMessage(userMessage: String, plantBitmap: Bitmap?) {
        val newUserMessage = Message(userMessage, Author.USER)
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + newUserMessage,
                isLoading = true
            )
        }

        if (plantBitmap == null) {
            handleError("Imagem da planta não encontrada.")
            return
        }

        viewModelScope.launch {
            val botResponseText = plantRepository.generateChatByImage(
                bitmap = plantBitmap,
                message = userMessage
            )

            if (botResponseText != null) {
                val parsedResponse = parseBotResponse(botResponseText)
                val botMessage = Message(parsedResponse, Author.BOT)
                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + botMessage,
                        isLoading = false
                    )
                }
            } else {
                handleError("Não consegui pensar em uma resposta.")
            }
        }
    }

    private fun parseBotResponse(rawResponse: String): String {
        return rawResponse.substringAfter("Resposta:").trim()
    }

    private fun handleError(errorMessage: String) {
        val errorBotMessage = Message(errorMessage, Author.BOT)
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + errorBotMessage,
                isLoading = false
            )
        }
    }

    init {
        _uiState.update {
            it.copy(
                messages = listOf(Message("Olá! Sobre o que vamos conversar hoje?", Author.BOT))
            )
        }
    }

}