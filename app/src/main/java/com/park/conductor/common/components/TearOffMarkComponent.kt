package com.park.conductor.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DottedLine(
    color: Color = Color.White,
    strokeWidth: Dp = 2.dp,
    dashWidth: Dp = 5.dp,
    gapWidth: Dp = 5.dp,
    horizontalPadding: Dp = 5.dp
) {
    Canvas(modifier = Modifier.fillMaxWidth().height(strokeWidth).padding(horizontal = horizontalPadding)) {
        val dashPx = dashWidth.toPx()
        val gapPx = gapWidth.toPx()
        val totalWidth = size.width
        var startX = 0f

        while (startX < totalWidth) {
            drawLine(
                color = color,
                start = Offset(startX, size.height / 2),
                end = Offset(startX + dashPx, size.height / 2),
                strokeWidth = strokeWidth.toPx()
            )
            startX += dashPx + gapPx
        }
    }
}
