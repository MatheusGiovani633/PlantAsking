package com.example.plantasking.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantasking.data.remote.PlantRepository
import kotlinx.coroutines.launch
import java.io.File


data class HomeUiState(
    val showDialog: Boolean = false,
    val analysisResult: String? = null,
    val capturedImageUri: Uri? = null,
    val isLoading: Boolean = false
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
                    val analysisJson = PlantRepository().analyzeImage(bitmap)
                    uiState = uiState.copy(
                        isLoading = false, analysisResult = analysisJson, showDialog = true
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

    fun onDialogDismiss() {
        uiState = uiState.copy(
            isLoading = false, analysisResult = null, showDialog = false, capturedImageUri = null
        )
    }

    private fun convertUriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION") android.provider.MediaStore.Images.Media.getBitmap(
                    context.contentResolver, uri
                )
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Falha ao converter URI para Bitmap", e)
            null
        }
    }
}
