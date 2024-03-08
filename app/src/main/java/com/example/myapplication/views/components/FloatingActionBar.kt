package com.example.myapplication.views.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.ui.theme.Background
import com.example.myapplication.ui.theme.Primary

data class FabState(
    val drawable: ImageVector = Icons.Filled.Add
)

@Composable
fun Fab (action:()->Unit,fabState: FabState = FabState()){
    FloatingActionButton(
        shape = CircleShape,
        contentColor = Background,
        containerColor = Primary,
        onClick = { action() },
    ) {
        Icon(fabState.drawable, "Floating action button.")
    }
}