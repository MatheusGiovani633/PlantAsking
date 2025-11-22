package com.example.plantasking.ui.home.previews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantasking.R
import com.example.plantasking.ui.home.PermissionDeniedContent


@Preview
@Composable
fun ImageConfirmationDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Foto Capturada") },
        text = { Text(text = "Deseja usar esta foto?") },

        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("Usar Foto")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Tentar Novamente")
            }
        },
    )
}


@Preview(
    name = "Tela da Câmera", showBackground = true
)
@Composable
private fun HomeScreenWithCameraPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = { /* Somente para visualização */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .height(150.dp)
                .width(150.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.iconphoto),
                contentDescription = "Tirar Foto"
            )
        }
    }
}

@Preview(
    name = "Tela de Permissão Negada", showBackground = true
)
@Composable
private fun PermissionDeniedScreenPreview() {
    PermissionDeniedContent(
        onRequestPermission = {}, modifier = Modifier
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
