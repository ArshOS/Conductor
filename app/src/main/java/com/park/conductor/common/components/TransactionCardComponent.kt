package com.park.conductor.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.park.conductor.data.remote.dto.MyTransaction
import com.park.conductor.ui.theme.Card40
import com.park.conductor.ui.theme.Cash40
import com.park.conductor.ui.theme.Green40
import com.park.conductor.ui.theme.UPI40
import java.util.Locale

@Composable
fun TransactionCardComposable(transaction: MyTransaction, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = "Ticket ID - ${transaction.ticket_unique_id}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Visitor(s) - ${transaction.visitor_count}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box(
                modifier = Modifier
                    .offset(x = 20.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                    .background(getBackgroundColor(transaction.mode_of_payment?.trim()?.uppercase()))
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                transaction.mode_of_payment?.trim()?.uppercase(Locale.getDefault())?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = "Fare",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "₹${transaction.amount}",
                    color = Color(0xFF2E7D32), // green color
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Booked on",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = transaction.booking_date,
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.labelLarge
                )
            }

        }
    }
}

//            UPI - Orange - FFA500
//            CASH - Green40 - FFA500
//            CARD - LinkedIn Blue - 0077B5

private fun getBackgroundColor(paymentMode: String?): Color {
    return if (paymentMode.isNullOrEmpty()) Color.DarkGray else when (paymentMode) {
        "UPI" -> UPI40
        "CARD" -> Card40
        "CASH" -> Cash40
        else -> Color.DarkGray
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val transaction =
        MyTransaction(100, "Fri, 28 Mar 2025 02:41 PM", "2567093814", "28-03-2025", "2", "UPI")
    TransactionCardComposable(transaction, rememberNavController())
}
