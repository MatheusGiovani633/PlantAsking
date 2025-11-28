package com.example.plantasking.ui.home
import com.example.plantasking.util.convertUriToBitmap
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantasking.data.repository.PlantRepository
import kotlinx.coroutines.launch
import java.io.File


data class HomeUiState(
    val showDialog: Boolean = false,
    val analysisResult: String? = null,
    val capturedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val messageApiRequest: String? = null,
)


class HomeViewModel : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    fun onTakePicture(context: Context, imageCapture: ImageCapture) {
        uiState = uiState.copy(isLoading = true, showDialog = false)
        val file = File.createTempFile("IMG_", ".jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        uiState = uiState.copy(
                            capturedImageUri = uri, isLoading = false, showDialog = true
                        )
                    }
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e("HomeViewModel", "Erro ao salvar a foto.", exception)
                    uiState = uiState.copy(isLoading = false)
                }
            })
    }
    fun onDialogPictured(context: Context) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, showDialog = false)
            uiState.capturedImageUri?.let { uri ->
                val bitmap = convertUriToBitmap(context, uri)
                if (bitmap != null) {
                    val analysisApiRequest = PlantRepository().analyzeImage(bitmap = bitmap)
                    val messageApiRequest = PlantRepository().generateChatByImage(message = "Olá!", bitmap = bitmap)
                    uiState = uiState.copy(
                        isLoading = false, analysisResult = analysisApiRequest, showDialog = true, messageApiRequest = messageApiRequest
                    )
                } else {
                    Log.e("HomeViewModel", "Falha ao obter bitmap da URI.")
                    uiState = uiState.copy(isLoading = false)
                }
            }
            uiState = uiState.copy(
                isLoading = false, showDialog = true, capturedImageUri = null
            )
        }
    }

    fun onDialogDismissWithImageSaved() {
        uiState = uiState.copy(
            isLoading = false,
            analysisResult = null,
            showDialog = false
        )
    }
    fun onDialogDismissAndClearImage() {
        uiState = uiState.copy(
            isLoading = false, analysisResult = null, showDialog = false, capturedImageUri = null
        )
    }

}
