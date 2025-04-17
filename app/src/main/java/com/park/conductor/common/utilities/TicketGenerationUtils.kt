package com.park.conductor.common.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject
import androidx.core.graphics.createBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.park.conductor.R
import androidx.core.graphics.scale
import androidx.core.graphics.set
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun generateTicketFooterBitmap(context: Context): Bitmap {
    val width = 576
    val height = 150

    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)

    val paintHeading = Paint().apply {
        color = Color.BLACK
        textSize = 35f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }


    val paintSmall = Paint().apply {
        color = Color.BLACK
        textSize = 25f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }


    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    var y = 40
    val lineSpacing = 35

//     Footer Logos
    paint.textSize = 20f
    paint.typeface = Typeface.DEFAULT
    canvas.drawText("Developed by", width / 10f, y.toFloat(), paint)
    canvas.drawText("Powered by", width - 70f, y.toFloat(), paint)
    y += 20
    try {
        val devLogo =
            BitmapFactory.decodeResource(context.resources, R.drawable.logo_innobles) //Inno logo
        val bankLogo =
            BitmapFactory.decodeResource(context.resources, R.drawable.logo_hdfc) // HDFC logo
        val devScaled = devLogo.scale(250, 42)
        val bankScaled = bankLogo.scale(250, 60)
        canvas.drawBitmap(devScaled, 10f, y.toFloat(), null)
        canvas.drawBitmap(bankScaled, width - 260f, y.toFloat(), null)
    } catch (e: Exception) {
        Log.w("TicketBitmapGenerator", "Footer logos not found")
    }

    // Footer line
    y += 60
    canvas.drawText("========= THANK YOU =========", width / 2f, y.toFloat(), paintHeading)

    return bitmap

}

fun generateTicketBitmapsFromJson(context: Context, json: String): List<Bitmap> {
    val ticketBitmaps = mutableListOf<Bitmap>()

    try {
        val root = JSONObject(json)
        val tickets = root.getJSONArray("tickets")

        // Common fields
        val parkName = root.getString("park_name")
        val attractionName = root.getString("attraction_name")
        val gstNumber = root.getString("gst_number")
        val bookedOn = root.getString("booked_on")
        val openTime = root.getString("open_time")
        val closeTime = root.getString("close_time")
        val paymentMode = root.getString("payment_mode")
        val bookedBy = root.getString("booked_by")
        val visitDate = root.getString("visit_date")
        val ticketUniqueId = root.getString("ticket_unique_id")
        val notes = root.getString("notes")

        for (i in 0 until tickets.length()) {
            val ticket = tickets.getJSONObject(i)
            val visitorId = ticket.getString("visitor_id")
            val visitorName = ticket.getString("visitor_name")
            val visitorType = ticket.getString("visitor_type")
            val amount = ticket.getString("amount")

            val width = 576
            val height = 1500


            val bitmap = createBitmap(width, height)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)

            val paintHeading = Paint().apply {
                color = Color.BLACK
                textSize = 35f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
            }


            val paintSmall = Paint().apply {
                color = Color.BLACK
                textSize = 25f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }


            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 30f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }

            var y = 40
            val lineSpacing = 35

            // Top logo (optional: replace with your app's resource)
            try {
                val logo =
                    BitmapFactory.decodeResource(context.resources, R.drawable.logo_lda_black)
                val logoScaled = logo.scale(150, 150)
                canvas.drawBitmap(logoScaled, (width - logoScaled.width) / 2f, y.toFloat(), null)
                y += logoScaled.height + 30
            } catch (e: Exception) {
                Log.w("TicketBitmapGenerator", "Top logo not found")
            }

            // Header - center align
            canvas.drawText(
                MerchantInfo.MERCHANT_NAME,
                width / 2f,
                y.toFloat(),
                paintHeading
            ); y += lineSpacing
            canvas.drawText(
                MerchantInfo.MERCHANT_NAME_HINDI,
                width / 2f,
                y.toFloat(),
                paintHeading
            ); y += lineSpacing

            y += 20
            canvas.drawText(parkName, width / 2f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText(attractionName, width / 2f, y.toFloat(), paintHeading); y += lineSpacing

            // Details
            y += 20
            canvas.drawText("GST: $gstNumber", width / 2f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText(
                "TIMING: $openTime - $closeTime",
                width / 2f,
                y.toFloat(),
                paint
            ); y += lineSpacing
            canvas.drawText(
                "VISIT DATE: $visitDate",
                width / 2f,
                y.toFloat(),
                paint
            ); y += lineSpacing

            y += 20
            canvas.drawText(visitorType, width / 2f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText("Amount: $amount", width / 2f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText(
                "Payment Mode: $paymentMode",
                width / 2f,
                y.toFloat(),
                paint
            ); y += lineSpacing

//            y += 20
//            canvas.drawText("Visitor ID: $visitorId", 20f, y.toFloat(), paint); y += lineSpacing
//            canvas.drawText("Name: $visitorName", 20f, y.toFloat(), paint); y += lineSpacing
//            canvas.drawText("Type: $visitorType", 20f, y.toFloat(), paint); y += lineSpacing
//
//
//            y += 20
//            canvas.drawText("Booked By: $bookedBy", 20f, y.toFloat(), paint); y += lineSpacing
//            canvas.drawText("Visit Date: $visitDate", 20f, y.toFloat(), paint); y += lineSpacing

            // QR Code
            val qrBitmap = generateQRCode(
                text = ticketUniqueId,
                labelText = "",
                width = 500,
                height = 500
            )
            qrBitmap?.let {
                val qrLeft = (width - it.width) / 2f
                canvas.drawBitmap(it, qrLeft, y.toFloat(), null)
                y += it.height + 20
            }

            canvas.drawText(
                "Notes",
                width / 2f,
                y.toFloat(),
                paintSmall
            ); y += lineSpacing

            // Footer Logos
//            paint.textSize = 20f
//            paint.typeface = Typeface.DEFAULT
//            canvas.drawText("Developed by", width / 10f, y.toFloat(), paint)
//            canvas.drawText("Powered by", width - 70f, y.toFloat(), paint)
//            y += 20
//            try {
//                val devLogo = BitmapFactory.decodeResource(context.resources, R.drawable.logo_innobles) //Inno logo
//                val bankLogo = BitmapFactory.decodeResource(context.resources, R.drawable.logo_hdfc) // HDFC logo
//                val devScaled = devLogo.scale(250, 42)
//                val bankScaled = bankLogo.scale(250, 60)
//                canvas.drawBitmap(devScaled, 10f, y.toFloat(), null)
//                canvas.drawBitmap(bankScaled, width - 260f, y.toFloat(), null)
//            } catch (e: Exception) {
//                Log.w("TicketBitmapGenerator", "Footer logos not found")
//            }

            y += 20
            canvas.drawText(
                "Booked by $bookedBy on $bookedOn",
                width / 2f,
                y.toFloat(),
                paintSmall
            ); y += lineSpacing


//            // Footer line
//            y += 20
////            canvas.drawText("Developed by Powered by", width / 2f, y.toFloat(), paintSmall)
//            canvas.drawText("========= THANK YOU =========", width / 2f, y.toFloat(), paintHeading)

            ticketBitmaps.add(bitmap)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ticketBitmaps
}

private fun generateQRCode(data: String, size: Int): Bitmap? {
    return try {
        val bitMatrix: BitMatrix =
            MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
        val bmp = createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp[x, y] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        bmp
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun generateQRCode(text: String, labelText: String, width: Int, height: Int): Bitmap? {
    return try {
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height)
        val qrBitmap = BarcodeEncoder().createBitmap(bitMatrix)

        // Add label below QR code
        val paint = Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 40f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val textBounds = Rect()
        paint.getTextBounds(labelText, 0, labelText.length, textBounds)
        val textHeight = textBounds.height()
        val padding = 20

        val finalBitmap = createBitmap(width, height + textHeight + padding)
        val canvas = Canvas(finalBitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        canvas.drawBitmap(qrBitmap, 0f, 0f, null)
        canvas.drawText(labelText, width / 2f, height + textHeight.toFloat(), paint)

        finalBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun generateTicketBitmapsFromJson2(context: Context, json: String): List<Bitmap> {
    val ticketBitmaps = mutableListOf<Bitmap>()

    try {
        val root = JSONObject(json)
        val tickets = root.getJSONArray("tickets")

        val commonFields = mapOf(
            "park_name" to root.getString("park_name"),
            "attraction_name" to root.getString("attraction_name"),
            "gst_number" to root.getString("gst_number"),
            "booked_on" to root.getString("booked_on"),
            "open_time" to root.getString("open_time"),
            "close_time" to root.getString("close_time"),
            "payment_mode" to root.getString("payment_mode"),
            "booked_by" to root.getString("booked_by"),
            "visit_date" to root.getString("visit_date"),
            "notes" to root.getString("notes")
        )

        for (i in 0 until tickets.length()) {
            val ticket = tickets.getJSONObject(i)

            val visitorId = ticket.getString("visitor_id")
            val visitorName = ticket.getString("visitor_name")
            val visitorType = ticket.getString("visitor_type")
            val amount = ticket.getString("amount")
            val qrData = ticket.getString("qr_data")

            val ticketData = JSONObject().apply {
                put("visitor_id", visitorId)
                put("visitor_name", visitorName)
                put("visitor_type", visitorType)
                put("amount", amount)
                put("qr_data", qrData)
                put("additional_charge", "₹5")

                // Add common fields
                for ((key, value) in commonFields) put(key, value)

                // Compute total from amount and charge
                val total = amount.filter { it.isDigit() }.toIntOrNull()?.plus(5) ?: 0
                put("total", "₹$total")
                put(
                    "timestamp",
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                        Date()
                    )
                )
            }

            ticketBitmaps.add(generateStyledTicket2(context, ticketData))
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ticketBitmaps
}

fun generateStyledTicket2(context: Context, data: JSONObject): Bitmap {

    val width = 576
    val height = 1800
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)

    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 35f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    val paintSmall = Paint().apply {
        color = Color.BLACK
        textSize = 25f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    var y = 40

    // Top logo (optional: replace with your app's resource)
    try {
        val logo = BitmapFactory.decodeResource(context.resources, R.drawable.logo_lda_black)
        val logoScaled = logo.scale(150, 150)
        canvas.drawBitmap(logoScaled, (width - logoScaled.width) / 2f, y.toFloat(), null)
        y += logoScaled.height + 20
    } catch (e: Exception) {
        Log.w("TicketBitmapGenerator", "Top logo not found")
    }

    y += 30

    canvas.drawText(MerchantInfo.MERCHANT_NAME, width / 2f, y.toFloat(), paint); y += 40
    canvas.drawText(MerchantInfo.MERCHANT_NAME_HINDI, width / 2f, y.toFloat(), paint); y += 40

    y += 30

    canvas.drawText(data.getString("park_name"), width / 2f, y.toFloat(), paint); y += 50

    paint.textSize = 40f
    paint.typeface = Typeface.DEFAULT_BOLD

    canvas.drawText(data.getString("attraction_name"), width / 2f, y.toFloat(), paint); y += 40

    y += 30

    paint.textSize = 32f
    paint.typeface = Typeface.DEFAULT

    canvas.drawText("GST: ${data.getString("gst_number")}", width / 2f, y.toFloat(), paint); y += 45

    val infoLines = listOf(
        "TIMING: ${data.getString("open_time")} - ${data.getString("close_time")}",
        "VISIT DATE: ${data.getString("visit_date")}",
    )

    for (line in infoLines) {
        canvas.drawText(line, width / 2f, y.toFloat(), paint)
        y += 35
    }

    y += 45

    paint.textSize = 32f
    paint.typeface = Typeface.DEFAULT_BOLD

    canvas.drawText(data.getString("visitor_type"), width / 2f, y.toFloat(), paint); y += 50

    paint.textSize = 30f
    paint.typeface = Typeface.DEFAULT

    canvas.drawText("Amount: ${data.getString("amount")}", width / 2f, y.toFloat(), paint); y += 30
    canvas.drawText(
        "Payment Mode: ${data.getString("payment_mode")}",
        width / 2f,
        y.toFloat(),
        paint
    ); y += 45

    // QR Code
//        val qrBitmap = generateQRCode(data.getString("qr_data"), 400)
//    val qrBitmap = generateQRCode(data.getString("qr_data"), 500)
//    qrBitmap?.let {
//        val qrX = (width - it.width) / 2f
//        canvas.drawBitmap(it, qrX, y.toFloat(), null)
//        y += it.height + 30
//    }

    // QR Code
//    val qrBitmap = generateQRCode(data.getString("qr_data"), "", 350, 350)
//    qrBitmap?.let {
//        val qrLeft = (width - it.width) / 2f
//        canvas.drawBitmap(it, qrLeft, y.toFloat(), null)
//        y += it.height + 20
//    }

    // Ticket ID
    canvas.drawText(data.getString("qr_data"), width / 2f, y.toFloat(), paint); y += 80

    paint.textSize = 20f
    paint.typeface = Typeface.DEFAULT

    // Terms
    canvas.drawText(data.getString("notes"), width / 2f, y.toFloat(), paint); y += 70

//        val notesText = data.getString("notes")
//        val textHeight = drawMultilineText(
//            canvas = canvas,
//            text = notesText,
//            paint = paint,
//            width = width - 40, // padding from both sides
//            x = 20f,
//            y = y.toFloat()
//        )
//        y += textHeight + 20


    // Footer Logos
    paint.textSize = 20f
    paint.typeface = Typeface.DEFAULT
    canvas.drawText("Developed by", width / 10f, y.toFloat(), paint)
    canvas.drawText("Powered by", width - 70f, y.toFloat(), paint)
    y += 20
    try {
        val devLogo =
            BitmapFactory.decodeResource(context.resources, R.drawable.logo_innobles) //Inno logo
        val bankLogo =
            BitmapFactory.decodeResource(context.resources, R.drawable.logo_hdfc) // HDFC logo
        val devScaled = devLogo.scale(200, 38)
        val bankScaled = bankLogo.scale(200, 50)
        canvas.drawBitmap(devScaled, 10f, y.toFloat(), null)
        canvas.drawBitmap(bankScaled, width - 240f, y.toFloat(), null)
    } catch (e: Exception) {
        Log.w("TicketBitmapGenerator", "Footer logos not found")
    }

    y += 100
    paintSmall.textAlign = Paint.Align.LEFT

    canvas.drawText(
        "Booked on : ${data.getString("timestamp")}",
        20f,
        y.toFloat(),
        paintSmall
    ); y += 25
    canvas.drawText("Booked by : ${data.getString("booked_by")}", 20f, y.toFloat(), paintSmall)

    y += 50

    canvas.drawText(
        "=================== THANK YOU ===================",
        width / 2f,
        y.toFloat(),
        paint
    ); y += 40

    return bitmap
}

private fun generateQRCode2(data: String, size: Int, borderSize: Int = 5): Bitmap? {
    return try {
        // Disable the default QR code margin (quiet zone)
        val hints = mapOf(
            EncodeHintType.MARGIN to 1
        )

        val bitMatrix = MultiFormatWriter().encode(
            data,
            BarcodeFormat.QR_CODE,
            size,
            size,
            hints
        )

        val qrBitmap = createBitmap(size, size, Bitmap.Config.RGB_565)


        // Draw QR code
        for (x in 0 until size) {
            for (y in 0 until size) {
                qrBitmap[x, y] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }

        // Create a larger bitmap with border
        val totalSize = size + 2 * borderSize
        val finalBitmap = createBitmap(totalSize, totalSize, Bitmap.Config.RGB_565)
        val canvas = Canvas(finalBitmap)
        canvas.drawColor(Color.BLACK) // Border color

        // Draw the QR bitmap in the center
        canvas.drawBitmap(qrBitmap, borderSize.toFloat(), borderSize.toFloat(), null)

        finalBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun generateStyledTicketBitmap(
    context: Context,
    data: JSONObject
): Bitmap {
    val width = 576
    val height = 1000
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)

    val paint = Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 28f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    val paintSmall = Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 22f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    var y = 40

    // Top Logo
    val logo = BitmapFactory.decodeResource(context.resources, R.drawable.logo_lda_black)
    val logoScaled = logo.scale(120, 120)
    canvas.drawBitmap(logoScaled, (width - logoScaled.width) / 2f, y.toFloat(), null)
    y += logoScaled.height + 20

    // Headers
    canvas.drawText("Lucknow Development Authority", width / 2f, y.toFloat(), paint); y += 35
    canvas.drawText("लखनऊ विकास प्राधिकरण", width / 2f, y.toFloat(), paint); y += 45
    paint.textSize = 30f
    paint.typeface = Typeface.DEFAULT_BOLD
    canvas.drawText("DR. BR AMBEDKAR PARK", width / 2f, y.toFloat(), paint); y += 35
    paint.textSize = 26f
    paint.typeface = Typeface.DEFAULT
    canvas.drawText("ENTRY TICKET", width / 2f, y.toFloat(), paint); y += 45

    // Ticket info
    val infoLines = listOf(
        "TIMING : ${data.getString("timing")}",
        "Ticket Valid Date = ${data.getString("valid_date")}",
        "${data.getString("nationality")} | ${data.getString("visitor_type")}",
        "ENTRY FEE : ₹ ${data.getString("entry_fee")} - Museum : ₹ ${data.getString("museum_fee")}",
        "Additional Charge : ₹${data.getString("additional_charge")}",
        "Total : ₹ ${data.getString("total")}",
        "Payment Mode : ${data.getString("payment_mode")}"
    )

    for (line in infoLines) {
        canvas.drawText(line, width / 2f, y.toFloat(), paint)
        y += 35
    }

    // QR Code
    y += 10
    val qrBitmap = generateQRCode(data.getString("ticket_id"), "", 350, 350)
    qrBitmap?.let {
        val qrX = (width - it.width) / 2f
        canvas.drawBitmap(it, qrX, y.toFloat(), null)
        y += it.height + 20
    }

    // Ticket ID
    canvas.drawText(data.getString("ticket_id"), width / 2f, y.toFloat(), paint); y += 35

    // Terms
    paintSmall.textAlign = Paint.Align.CENTER
    val terms = listOf(
        "Ticket is valid for one person for one time use only",
        "Entry to park will close 30 minutes before sunset"
    )
    for (line in terms) {
        canvas.drawText(line, width / 2f, y.toFloat(), paintSmall); y += 28
    }

    // Bottom logos
    y += 20
    val developedBy = BitmapFactory.decodeResource(context.resources, R.drawable.logo_lda_black)
    val poweredBy = BitmapFactory.decodeResource(context.resources, R.drawable.logo_lda_black)
    val scaledDev = developedBy.scale(100, 40)
    val scaledPower = poweredBy.scale(100, 40)

    canvas.drawBitmap(scaledDev, 40f, y.toFloat(), null)
    canvas.drawBitmap(scaledPower, width - scaledPower.width - 40f, y.toFloat(), null)

    // Timestamp
    y += 50
    paintSmall.textAlign = Paint.Align.LEFT
    canvas.drawText("Time : ${data.getString("timestamp")}", 20f, y.toFloat(), paintSmall)

    return bitmap
}

@Composable
private fun TicketBitmapView(bitmap: Bitmap) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                setImageBitmap(bitmap)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun TicketPreview() {
    val context = LocalContext.current

    val jsonStr = """
    {
        "status": true,
        "message": "successfull",
        "ticket_unique_id": "2567093814",
        "park_name": "DR. BR AMBEDKAR PARK",
        "attraction_name": "ENTRY TICKET",
        "gst_number": "BDHFGFU345BFH",
        "booked_on": "10-04-2025 02:53 PM",
        "open_time": "07:00 AM",
        "close_time": "11:00 PM",
        "payment_mode": "Online",
        "booked_by": "Operator",
        "visit_date": "10-04-2025",
        "notes": "Ticket is non refundable and valid for same business day only.",
        "tickets": [
        {
            "visitor_type": "INDAIN-ADULT",
            "amount": "₹20",
            "qr_data": "56748374646"
        }
        ]
    }
    """.trimIndent()

//    val json = remember { JSONObject(jsonStr) }
    val bitmap = remember {
//        TicketBitmapGenerator(context).generateStyledTicket(json)
        generateTicketBitmapsFromJson(context, jsonStr)
    }
    TicketBitmapView(bitmap = bitmap[0])
}