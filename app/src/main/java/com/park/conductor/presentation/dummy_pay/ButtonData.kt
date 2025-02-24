package com.park.conductor.presentation.dummy_pay

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.outlined.InsertPageBreak
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.SwipeUp
import androidx.compose.material.icons.outlined.TapAndPlay
import androidx.compose.ui.graphics.Color
import com.park.conductor.data.local.data_class.ButtonData
import com.park.conductor.ui.theme.Green40

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
    )
)
