package com.park.conductor.common.components

import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.park.conductor.common.utilities.getFormattedDate
import com.park.conductor.presentation.drawer.my_transactions.convertMillisToDate
import com.park.conductor.ui.theme.Green40
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComposable(
    placeholder: String = "Today",
    onItemSelected: (String) -> Unit
) {

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf("") }

    var showModal by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(0.dp, 5.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(15.dp, 10.dp)
                .clickable { showModal = true }
        ) {
            Icon(
                modifier = Modifier.size(35.dp),
                imageVector = Icons.Outlined.CalendarMonth,
                tint = Green40,
                contentDescription = "Calendar Icon"
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = if (selectedDate.isEmpty()) "Today" else getFormattedDate(today = false, format1 = "dd-mm-yyyy", format2 = "dd-mm-yyyy", selectedDate),
                modifier = Modifier.weight(1f),
                color = Green40,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

        }
    }

    if (showModal) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            ContextThemeWrapper(context, 0),
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                selectedDate = "$dayOfMonth-${month + 1}-$year"
                onItemSelected(selectedDate.replace("-", "/"))
                showModal = false
            },
            currentYear, currentMonth, currentDay
        )

        // Set min and max date
        val minCalendar = Calendar.getInstance()
        minCalendar.set(1900, 0, 1) // Optional: Set a very early date
        datePickerDialog.datePicker.minDate = minCalendar.timeInMillis

        val maxCalendar = Calendar.getInstance()
        datePickerDialog.datePicker.maxDate = maxCalendar.timeInMillis // Prevent future dates

        datePickerDialog.setOnCancelListener {
            showModal = false
        }

        datePickerDialog.show()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun DefaultPreview() {

    val selectedDate by remember { mutableStateOf(convertMillisToDate(System.currentTimeMillis())) }

    DatePickerComposable(
        placeholder = selectedDate,
        onItemSelected = {  }
    )
}

