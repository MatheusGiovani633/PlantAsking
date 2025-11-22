package com.example.plantasking.ui.home

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Base64
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream


data class HomeUiState(
    val showDialog: Boolean = false,
    val capturedImageUri: Uri? = null,
    val isLoading: Boolean = false
)


class HomeViewModel : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set
    fun onTakePicture(context: Context, imageCapture: ImageCapture) {
        val file = File.createTempFile("IMG_", ".jpg")
        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        uiState = uiState.copy(
                            capturedImageUri = uri,
                            showDialog = true
                        )
                    }
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e("HomeViewModel", "Erro ao salvar a foto.", exception)
                }
            }
        )
    }


    fun onDialogConfirm(context: Context) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, showDialog = false)
            uiState.capturedImageUri?.let { uri ->
                val base64String = convertUriToBase64(context, uri)
                if (base64String != null) {
                    Log.d("HomeViewModel", "Imagem convertida com sucesso: ${base64String.take(100)}")
                    // TODO: Enviar a string para a API.
                } else {
                    Log.e("HomeViewModel", "Falha ao converter imagem.")
                }
            }

            uiState = uiState.copy(isLoading = false, capturedImageUri = null)
        }
    }

    fun onDialogDismiss() {
        uiState = uiState.copy(showDialog = false, capturedImageUri = null)
    }

    private suspend fun convertUriToBase64(context: Context, uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                bytes?.let {
                    Base64.encodeToString(it, Base64.DEFAULT)
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Falha ao converter Uri para Base64", e)
                null
            }
        }
    }
}
