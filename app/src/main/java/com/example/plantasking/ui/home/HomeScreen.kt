package com.example.plantasking.ui.home

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.plantasking.R
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState
    var hasRequiredPermissions by remember {
        mutableStateOf(
            (ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED)
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(), onResult = { permissions ->
            hasRequiredPermissions = permissions.getOrDefault(Manifest.permission.CAMERA, false)
        })
    LaunchedEffect(Unit) {
        if (!hasRequiredPermissions) {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.CAMERA) /*Futuramente irei incluir audio*/
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (hasRequiredPermissions) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(), onTakePictureClick = { imageCapture ->
                    viewModel.onTakePicture(context, imageCapture)
                })
        } else {
            PermissionDeniedContent(
                onRequestPermission = {
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                    )
                }, modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF03A898), Color(0xFF0EAE8A), Color(0xFF19B27A)
                            )
                        )
                    )
            )
        }
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        if (uiState.showDialog) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { viewModel.onDialogDismiss() }, sheetState = sheetState
            ) {
                ActionMenuContent(
                onAnalyzeClick = {
                    // Todo: Conversa ainda a ser implementada
                }, onSaveMoodClick = {
                    viewModel.onDialogPictured(context)
                }, onDismiss = {
                    viewModel.onDialogDismiss()
                })
            }
        }
        if (uiState.analysisResult != null) {
            ViewMood(
                onDismiss = { viewModel.onDialogDismiss() }, analysisText = uiState.analysisResult
            )
        }
    }
}

@Composable
private fun ActionMenuContent(
    onAnalyzeClick: () -> Unit,
    onSaveMoodClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 32.dp)
            .heightIn(250.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionMenuItem(
                drawableResId = R.drawable.talk,
                text = "Conversar",
                onClick = onAnalyzeClick,
                modifier = Modifier.weight(1f)
            )
            ActionMenuItem(
                drawableResId = R.drawable.mood,
                text = "Ver Humor",
                onClick = onSaveMoodClick,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        ActionMenuItem(
            drawableResId = R.drawable.photo,
            text = "Tirar nova foto",
            onClick = onDismiss,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ActionMenuItem(
    drawableResId: Int, text: String, onClick: () -> Unit, modifier: Modifier = Modifier
) {

    TextButton(
        onClick = onClick, modifier = modifier, shape = MaterialTheme.shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = drawableResId),
                contentDescription = text,
                modifier = Modifier.size(48.dp)
            )
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Composable
fun CameraPreview(modifier: Modifier = Modifier, onTakePictureClick: (ImageCapture) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageCapture
                        )
                    } catch (exc: Exception) {
                        Log.e("CameraPreview", "Falha ao vincular os casos de uso", exc)
                    }
                }, executor)
                previewView
            }, modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = {
                onTakePictureClick(imageCapture)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .size(150.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.iconphoto),
                contentDescription = "Tirar Foto"
            )
        }
    }
}

@Composable
fun PermissionDeniedContent(
    onRequestPermission: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "As permissões de Câmera e Áudio são necessárias para usar esta funcionalidade.",
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Conceder Permissões")
        }
    }
}

@Composable
fun ViewMood(
    onDismiss: () -> Unit,
    analysisText: String
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text("Humor da Planta")
    },
        text = {
            Text(analysisText)
    }, confirmButton = {
        TextButton(
            onClick = onDismiss, modifier = Modifier.fillMaxWidth()
        ) {
            Text("OK")
        }
    }, icon = {
        if (analysisText.contains("feliz")) {
            Image(
                painter = painterResource(id = R.drawable.moodhappy),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        }
        else if(analysisText.contains("doente")){
            Image(
                painter = painterResource(id = R.drawable.moodsick),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        }
        else if(analysisText.contains("triste")){
            Image(
                painter = painterResource(id = R.drawable.moodsad),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        }else{
            Image(
                painter = painterResource(id = R.drawable.error),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        }

    })

}
