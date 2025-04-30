package com.park.conductor.presentation.post_transaction_screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eze.api.EzeAPI
import com.google.gson.Gson
import com.park.conductor.common.utilities.generateTicketBitmapsFromJson
import com.park.conductor.common.utilities.generateTicketFooterBitmap
import com.park.conductor.common.utilities.restartApp
import com.park.conductor.data.remote.api.ApiConstant
import com.park.conductor.data.remote.api.ApiService
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.dto.UpdatePaymentResponse
import com.park.conductor.ui.theme.Green40
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlinx.coroutines.delay


// TransactionResultActivity.kt
class TransactionResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val responseRZP = intent.getStringExtra("response") ?: ""

        val attractionId = intent.getStringExtra("attractionId") ?: ""
        val ticketUniqueId = intent.getStringExtra("ticket_unique_id") ?: ""
        val paymentMode = intent.getStringExtra("paymentMode") ?: "4"

        var response: JSONObject = JSONObject(responseRZP)
        response = response.getJSONObject("result")
        response = response.getJSONObject("txn")
        val strTxnId = response.getString("txnId")
        val paymentAmount = response.getString("amount")

        val transactionResultViewModel = TransactionResultViewModel()

        setContent {

// ticketid, unique_ticket_id, userid, payment_mode, payment_status, payable_amount, transaction_id


            val param = remember {
                ApiConstant.getBaseParam().apply {
                    put("ticketid", attractionId)
                    put("unique_ticket_id", ticketUniqueId)
                    put("payment_mode", 2)
                    put("payment_status", 1)
                    put("payable_amount", paymentAmount)
                    put("transaction_id", strTxnId)
                }
            }

            Log.d("TAG", "Update Params $param")

            val context = LocalContext.current

            LaunchedEffect(Unit) {
                if (ApiService.NetworkUtil.isInternetAvailable(context)) {
                    transactionResultViewModel.getUpdatePaymentResponse(param)
                } else {
                    ApiService.NetworkUtil.showNoInternetDialog(context)
                }
            }

            val stateUpdatePayment by transactionResultViewModel.updatePayment.collectAsState()


            MaterialTheme {

                SetUpObserverUpdatePayment(
                    state = stateUpdatePayment,
                    transactionIdRZP = strTxnId
                )

            }
        }
    }
}

@Composable
fun SetUpObserverUpdatePayment(
    state: ApiState<UpdatePaymentResponse>,
    transactionIdRZP: String,
) {

    val context = LocalContext.current

    when (state) {
        is ApiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ApiState.Success -> {

            PaymentSuccessScreen(
                state.data,
                transactionIdRZP
            )

            Log.d("TAG: ", "Update Payment API success")

        }

        is ApiState.Error -> {
            Log.d("TAG: ", "Update Payment API Failure: errorMessage ${state.message}")
        }
    }
}


@Composable
fun TransactionResultScreen(responseRZP: String) {

    var response: JSONObject = JSONObject(responseRZP)
    response = response.getJSONObject("result")
    response = response.getJSONObject("txn")
    val strTxnId = response.getString("txnId")
    val emiID = response.getString("emiId")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Transaction Response:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(strTxnId, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun PaymentSuccessScreen(
    data: UpdatePaymentResponse,
    transactionIdRZP: String
) {

    Log.d("TAG", "update payment: $data")

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)), // Material Green
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Payment Successful",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Transaction ID: $transactionIdRZP",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Ticket ID: ${data.ticket_unique_id}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
        }

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        val ticketFooterBitmap = generateTicketFooterBitmap(context, data.notes)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(0.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { restartApp(context) },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0XFF0077B5), RoundedCornerShape(0.dp))
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF0077B5))
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Filled.Home,
                        contentDescription = null
                    )
                    Spacer(Modifier.padding(3.dp))
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                }
                Button(
                    onClick = {

                        coroutineScope.launch {


                            // Offload heavy bitmap generation to background thread
                            val tickets = withContext(Dispatchers.Default) {
                                generateTicketBitmapsFromJson(context, Gson().toJson(data))
                            }

                            val finalTicketList: List<Bitmap> = buildList {
                                tickets.forEach { ticket ->
                                    add(ticket)
                                    add(ticketFooterBitmap)
                                }
                            }




                            finalTicketList.forEachIndexed { _, bitmap ->
                                printLucknowQR(bitmap, ticketFooterBitmap, context)
                                delay(2000L)
                            }


//                            val generator = TicketBitmapGenerator(context)
//                            val bitmaps =
//                                generator.generateTicketBitmapsFromJson(data.toString())

                            Log.d("TAG", "generateTicketBitmapsFromJson: ${Gson().toJson(data)}")


//                    val generator = TicketBitmapGenerator.generateTicketBitmapsFromJson(context, dummyJson)


//                            tickets.forEachIndexed { _, bitmap ->
//                                printLucknowQR(bitmap, bitmap, context)
//
//                                // Optional delay to allow printer to complete
//                                delay(1500L) // 1.5 seconds (adjust based on printer speed)
//                            }
                        }

//                val qrTickets = generateTicketBitmapsFromJson(context, dummyJson)
//
//                qrTickets.forEachIndexed { _, bitmap ->
//                    printLucknowQR(bitmap, context)
//                }


                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(Green40, RoundedCornerShape(0.dp))
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green40)
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Filled.Print,
                        contentDescription = null
                    )
                    Spacer(Modifier.padding(3.dp))
                    Text(
                        text = "Print Tickets",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                }
            }

        }


    }
}
//.fillMaxWidth()
//.align(Alignment.BottomCenter)
//.height(60.dp)
//.padding(0.dp)
//.background(Green40, RoundedCornerShape(0.dp))

suspend fun convertPdfUrlToBitmap(context: Context, pdfUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(pdfUrl)
            val connection = url.openConnection()
            val inputStream = connection.getInputStream()

            val file = File.createTempFile("ticket", ".pdf", context.cacheDir)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            val fileDescriptor =
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)

            val page = pdfRenderer.openPage(0)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            page.close()
            pdfRenderer.close()
            fileDescriptor.close()

            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
    val byteFormat = stream.toByteArray()
    return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
}

fun printLucknowQR(qrCodeBitmap: Bitmap, footerBitmap: Bitmap, context: Context) {

    val jsonRequest = JSONObject()

    val jsonImageObj = JSONObject()

    try {
        val encodedImageData = getEncoded64ImageStringFromBitmap(qrCodeBitmap)

        // Building Image Object
        jsonImageObj.put("imageData", encodedImageData)

        jsonImageObj.put("imageType", "JPEG")

        jsonImageObj.put("height", "") // optional

        jsonImageObj.put("weight", "") // optional

        jsonRequest.put(
            "image",
            jsonImageObj
        ) // Pass this attribute when you have a valid captured signature image

        EzeAPI.printBitmap(context, 10023, jsonRequest)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

}

private fun printTicketFooter(footerBitmap: Bitmap, context: Context) {
    // Footer

    val jsonRequestFooter = JSONObject()

    val jsonImageFooterObj = JSONObject()

    try {
        val encodedImageData = getEncoded64ImageStringFromBitmap(footerBitmap)

        // Building Image Object
        jsonImageFooterObj.put("imageData", encodedImageData)

        jsonImageFooterObj.put("imageType", "JPEG")

        jsonImageFooterObj.put("height", "") // optional

        jsonImageFooterObj.put("weight", "") // optional

        jsonRequestFooter.put(
            "image",
            jsonImageFooterObj
        ) // Pass this attribute when you have a valid captured signature image

        EzeAPI.printBitmap(context, 10023, jsonRequestFooter)
    } catch (e: JSONException) {
        e.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {

    PaymentSuccessScreen(
        data = UpdatePaymentResponse(
            "", "", "", "", "", "", "", "", "", "", true, "", listOf(),
            visit_date = ""
        ),
        transactionIdRZP = "TXN123456789"
    )

}

