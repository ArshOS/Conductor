package com.park.conductor.common.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import org.json.JSONObject
import androidx.core.graphics.createBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

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

        for (i in 0 until tickets.length()) {
            val ticket = tickets.getJSONObject(i)
            val visitorId = ticket.getString("visitor_id")
            val visitorName = ticket.getString("visitor_name")
            val visitorType = ticket.getString("visitor_type")
            val amount = ticket.getString("amount")

            val width = 576
            val height = 800


            val bitmap = createBitmap(width, height)
            val canvas = Canvas(bitmap)
            canvas.drawColor(android.graphics.Color.WHITE)

            val paintHeading = Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 48f
                isAntiAlias = true
                typeface = Typeface.DEFAULT_BOLD
            }

            val paint = Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 30f
                isAntiAlias = true
            }

            var y = 40
            val lineSpacing = 35

            // Header - center align
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(parkName, width / 2f, y.toFloat(), paintHeading); y += lineSpacing
            canvas.drawText(attractionName, width / 2f, y.toFloat(), paintHeading); y += lineSpacing
            paint.textAlign = Paint.Align.LEFT

            // Details
            canvas.drawText("GST: $gstNumber", 20f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText("Booked On: $bookedOn", 20f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText("Timings: $openTime - $closeTime", 20f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText("Payment Mode: $paymentMode", 20f, y.toFloat(), paint); y += lineSpacing

            y += 20
            canvas.drawText("Visitor ID: $visitorId", 20f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText("Name: $visitorName", 20f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText("Type: $visitorType", 20f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText("Amount: $amount", 20f, y.toFloat(), paint); y += lineSpacing

            y += 20
            canvas.drawText("Booked By: $bookedBy", 20f, y.toFloat(), paint); y += lineSpacing
            canvas.drawText("Visit Date: $visitDate", 20f, y.toFloat(), paint); y += lineSpacing

            y += 30

            // QR Code
            val qrBitmap = generateQRCode(ticketUniqueId, "", 500, 500)
            qrBitmap?.let {
                val qrLeft = (width - it.width) / 2f
                canvas.drawBitmap(it, qrLeft, y.toFloat(), null)
                y += it.height + 20
            }

            // Footer line
            canvas.drawText("******************************", 20f, y.toFloat(), paint)

            ticketBitmaps.add(bitmap)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ticketBitmaps
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

