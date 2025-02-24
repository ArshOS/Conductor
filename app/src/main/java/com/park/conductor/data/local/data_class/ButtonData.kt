package com.park.conductor.data.local.data_class

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ButtonData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconAction: ImageVector,
    val direction: String,
    val onClick: () -> Unit,
    val color: Color
)
