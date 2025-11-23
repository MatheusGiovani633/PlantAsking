package com.example.plantasking.ui.home.previews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantasking.R
import com.example.plantasking.ui.home.PermissionDeniedContent
import kotlinx.coroutines.launch


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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Menu de Ações Pós-Foto")
@Composable
private fun PhotoActionBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = {
            scope.launch { sheetState.hide() }
        }, sheetState = sheetState
    ) {
        ActionMenuContent(onAnalyzeClick = {}, onSaveClick = {}, onDismiss = {
            scope.launch { sheetState.hide() }
        })
    }
}

@Composable
private fun ActionMenuContent(
    onAnalyzeClick: () -> Unit,
    onSaveClick: () -> Unit,
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
                icon = Icons.Default.Edit,
                text = "Conversar",
                onClick = onAnalyzeClick,
                modifier = Modifier.weight(1f)
            )
            ActionMenuItem(
                icon = Icons.Default.Done,
                text = "Ver Humor",
                onClick = onSaveClick,
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        ActionMenuItem(
            icon = Icons.Default.Close,
            text = "Descartar",
            onClick = onDismiss,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ActionMenuItem(
    icon: ImageVector, text: String, onClick: () -> Unit, modifier: Modifier = Modifier
) {

    TextButton(
        onClick = onClick, modifier = modifier, shape = MaterialTheme.shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = text)
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
