package com.example.plantasking.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(hasRequiredPermissions(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            // Atualiza o estado da permissão apenas se a permissão da CÂMERA foi alterada
            hasCameraPermission = permissions[Manifest.permission.CAMERA] ?: hasCameraPermission
        }
    )

    // Efeito que roda uma vez para solicitar permissão se ainda não tiver
    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            )
        }
    }

    // A UI decide qual conteúdo exibir com base na permissão
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (hasCameraPermission) {
            // Se tem permissão, mostra a câmera. O fundo gradiente é desnecessário aqui.
            CameraPreview()
        } else {
            // Se não tem permissão, mostra a tela informativa COM o fundo gradiente.
            PermissionDeniedContent(
                onRequestPermission = {
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF03A898),
                                Color(0xFF0EAE8A), // Cor intermediária simplificada
                                Color(0xFF19B27A)
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun CameraPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    AndroidView(
        factory = { previewView },
        modifier = modifier.fillMaxSize(), // Garante que a câmera ocupe toda a tela
        update = {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Garante que não há outras câmeras vinculadas antes de uma nova vinculação
                    cameraProvider.unbindAll()
                    // Vincula a câmera ao ciclo de vida do Composable
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Falha ao vincular o caso de uso da câmera", exc)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
}

private fun hasRequiredPermissions(context: Context): Boolean {
    // Verifica se TODAS as permissões necessárias foram concedidas
    return arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO).all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
private fun PermissionDeniedContent(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp), // Padding para não colar nas bordas
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "A permissão da câmera é necessária para usar esta funcionalidade.",
            color = Color.White,
            textAlign = TextAlign.Center // Centraliza o texto caso ele quebre em mais de uma linha
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Conceder Permissão")
        }
    }
}
