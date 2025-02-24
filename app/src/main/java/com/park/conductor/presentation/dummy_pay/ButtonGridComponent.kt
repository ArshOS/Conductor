package com.park.conductor.presentation.dummy_pay

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.InsertPageBreak
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.SwipeUp
import androidx.compose.material.icons.outlined.TapAndPlay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import com.park.conductor.data.local.data_class.ButtonData
import com.park.conductor.ui.theme.Green40

@Composable
fun ButtonGrid(
    totalButtons: Int,
    buttonsPerRow: Int,
    buttonData: List<ButtonData>
) {
    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until (totalButtons + buttonsPerRow - 1) / buttonsPerRow) {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                for (col in 0 until buttonsPerRow) {
                    val index = row * buttonsPerRow + col
                    if (index < totalButtons) {
                        OutlinedButton(
                            onClick = buttonData[index].onClick,
                            modifier = Modifier
                                .padding(4.dp)
                                .wrapContentHeight()
                                .width(150.dp)
                                .border(2.dp, buttonData[index].color, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Green40,
                                containerColor = Color.Transparent
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                                ) {
                                Row {
                                    Icon(
                                        imageVector = buttonData[index].icon,
                                        contentDescription = null,
                                        tint = buttonData[index].color
                                    )
                                    Icon(
                                        imageVector = buttonData[index].iconAction,
                                        contentDescription = null,
                                        tint = buttonData[index].color
                                    )
                                }
                                Text(text = buttonData[index].title, color = buttonData[index].color, style = MaterialTheme.typography.bodyMedium)
                                Text(text = buttonData[index].subtitle, style = MaterialTheme.typography.bodySmall, color = buttonData[index].color)
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.size(120.dp, 60.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonGrid() {
    val buttonData = listOf<ButtonData>(
        ButtonData(
            title = "Credit",
            subtitle = "Swipe",
            icon = Icons.Filled.CreditScore,
            iconAction = Icons.Outlined.SwipeUp,
            direction = "",
            onClick = {},
            color = Green40
        ),
        ButtonData(
            title = "Debit",
            subtitle = "Swipe",
            icon = Icons.Filled.CreditCard,
            iconAction = Icons.Outlined.SwipeUp,
            direction = "",
            onClick = {},
            color = Color(0XFFFFA500)
        ),
        ButtonData(
            title = "Credit",
            subtitle = "Insert",
            icon = Icons.Filled.CreditScore,
            iconAction = Icons.Outlined.InsertPageBreak,
            direction = "",
            onClick = {},
            color = Green40
        ),
        ButtonData(
            title = "Debit",
            subtitle = "Insert",
            icon = Icons.Filled.CreditCard,
            iconAction = Icons.Outlined.InsertPageBreak,
            direction = "",
            onClick = {},
            color = Color(0XFFFFA500)
        ),
        ButtonData(
            title = "Credit",
            subtitle = "Tap",
            icon = Icons.Filled.CreditScore,
            iconAction = Icons.Outlined.TapAndPlay,
            direction = "",
            onClick = {},
            color = Green40
        ),
        ButtonData(
            title = "Debit",
            subtitle = "Tap",
            icon = Icons.Filled.CreditCard,
            iconAction = Icons.Outlined.TapAndPlay,
            direction = "",
            onClick = {},
            color = Color(0XFFFFA500)
        ),
        ButtonData(
            title = "UPI",
            subtitle = "ID",
            icon = Icons.Filled.BookOnline,
            iconAction = Icons.Outlined.Password,
            direction = "",
            onClick = {},
            color = Color(0XFF002D62)
        ),
        ButtonData(
            title = "UPI",
            subtitle = "QR",
            icon = Icons.Filled.BookOnline,
            iconAction = Icons.Outlined.QrCode,
            direction = "",
            color = Color(0XFF002D62),
            onClick = {}
        ),

    )
    val sampleData = List(8) { index ->
        ButtonData(
            title = "Title ${index + 1}",
            subtitle = "Subtitle ${index + 1}",
            icon = Icons.Default.Info,
            iconAction = Icons.Outlined.Info,
            direction = "Direction ${index + 1}",
            onClick = {},
            color = Green40
        )
    }
    ButtonGrid(totalButtons = 8, buttonsPerRow = 2, buttonData = buttonData)
}
