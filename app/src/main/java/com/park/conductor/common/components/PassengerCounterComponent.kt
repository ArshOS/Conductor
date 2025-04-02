package com.park.conductor.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.park.conductor.ui.theme.Blue40
import com.park.conductor.ui.theme.Green40
import com.park.conductor.ui.theme.Red40

@Composable
fun PassengerCounterComposable(
    title: String,
    subtitle: String? = null,
    rateInINR: Float,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    leftTotalCount: Int = 0
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .weight(1.5f)
                .background(Color.Transparent, RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp))
                .padding(horizontal = 15.dp)
        ) {
            Text(
                text = title,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            if (!subtitle.isNullOrEmpty()) {
                Text(
                    text = subtitle.toString(),
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall
                )
            }

        }
        Text(
            modifier = Modifier.weight(1f),
            text = "₹$rateInINR",
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Icon(
            modifier = Modifier
                .size(60.dp)
                .weight(1f)
                .background(Red40, RoundedCornerShape(0.dp))
                .clickable {
                    if (count != 0)
                        onDecrement()
                },
            imageVector = Icons.Filled.Remove, contentDescription = null, tint = Color.White
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .background(Color(0XFFFFEEB5), RoundedCornerShape(0.dp))
                .padding(vertical = 12.dp),
            text = if (count <= 0) "0" else count.toString(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            color = if (count <= 0) Color.Gray else Blue40
        )
        Icon(
            modifier = Modifier
                .size(60.dp)
                .weight(1f)
                .background(Green40, RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                .clickable {
                    if (leftTotalCount > 0)
                        onIncrement()
                },
            imageVector = Icons.Filled.Add, contentDescription = null, tint = Color.White
        )

    }
}
