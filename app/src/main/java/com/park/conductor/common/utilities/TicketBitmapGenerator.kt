package com.park.conductor.common.utilities

import android.content.Context
import android.graphics.*
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.park.conductor.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale


import android.graphics.BitmapFactory

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.set
import androidx.core.graphics.withTranslation
import com.google.zxing.EncodeHintType


class TicketBitmapGenerator0(private val context: Context) {

    fun generateTicketBitmapsFromJson(json: String): List<Bitmap> {
        val ticketBitmaps = mutableListOf<Bitmap>()

        try {
            val root = JSONObject(json)
            val tickets = root.getJSONArray("tickets")

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

            val logoInnobles = BitmapFactory.decodeResource(context.resources, R.drawable.logo_innobles)
            val logoHdfc = BitmapFactory.decodeResource(context.resources, R.drawable.logo_hdfc)

            for (i in 0 until tickets.length()) {
                val ticket = tickets.getJSONObject(i)
                val visitorType = ticket.getString("visitor_type")
                val amount = ticket.getString("amount")
                val qrData = ticket.getString("qr_data")

                val width = 576
                val lineSpacing = 35
                val sectionPadding = 20
                val qrHeight = 300
                val logoHeight = 40
                val footerPadding = 60

                val lines = listOf(
                    "Lucknow Development Authority",
                    "लखनऊ विकास प्राधिकरण",
                    parkName,
                    attractionName,
                    "GST : $gstNumber",
                    "TIMING : $openTime – $closeTime",
                    "Visit Date :- $visitDate",
                    visitorType,
                    "Amount : $amount",
                    "Payment Mode : $paymentMode",
                    "Ticket is valid for same business day only.",
                    "Ticket is non transferable & non refundable.",
                    "Developed by",
                    "Powered by",
                    "Booked on : $bookedOn",
                    "Booked by : $bookedBy"
                )

                val totalHeight = lines.size * lineSpacing + qrHeight + logoHeight + footerPadding + sectionPadding * 4

                val bitmap = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                canvas.drawColor(Color.WHITE)

                val paint = Paint().apply {
                    color = Color.BLACK
                    textSize = 30f
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                }

                val leftPaint = Paint(paint).apply {
                    textAlign = Paint.Align.LEFT
                    textSize = 24f
                }

                val borderPaint = Paint().apply {
                    color = Color.BLACK
                    style = Paint.Style.STROKE
                    strokeWidth = 4f
                }

                canvas.drawRect(0f, 0f, width.toFloat(), totalHeight.toFloat(), borderPaint)

                var y = sectionPadding

                fun drawCenterText(text: String, size: Float = 30f) {
                    paint.textSize = size
                    canvas.drawText(text, width / 2f, y.toFloat(), paint)
                    y += lineSpacing
                }

                drawCenterText("Lucknow Development Authority")
                drawCenterText("लखनऊ विकास प्राधिकरण")
                drawCenterText(parkName)
                drawCenterText(attractionName)
                drawCenterText("GST : $gstNumber")
                drawCenterText("TIMING : $openTime – $closeTime")
                drawCenterText("Visit Date :- $visitDate")
                drawCenterText(visitorType)
                drawCenterText("Amount : $amount")
                drawCenterText("Payment Mode : $paymentMode")

                val qrBitmap = generateQRCode(qrData, 300, qrHeight)
                qrBitmap?.let {
                    val qrLeft = (width - it.width) / 2f
                    canvas.drawBitmap(it, qrLeft, y.toFloat(), null)
                    y += it.height + sectionPadding
                }

                drawCenterText("Ticket is valid for same business day only.", 24f)
                drawCenterText("Ticket is non transferable & non refundable.", 24f)

                canvas.drawText("Developed by", 60f, y.toFloat(), leftPaint)
                canvas.drawText("Powered by", width - 200f, y.toFloat(), leftPaint)
                y += logoHeight

                logoInnobles?.let {
                    canvas.drawBitmap(Bitmap.createScaledBitmap(it, 120, logoHeight, true), 50f, y.toFloat(), null)
                }
                logoHdfc?.let {
                    canvas.drawBitmap(Bitmap.createScaledBitmap(it, 160, logoHeight, true), width - 220f, y.toFloat(), null)
                }
                y += logoHeight + 10

                canvas.drawText("Booked on : $bookedOn", 20f, y.toFloat(), leftPaint); y += lineSpacing
                canvas.drawText("Booked by : $bookedBy", 20f, y.toFloat(), leftPaint)

                ticketBitmaps.add(bitmap)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ticketBitmaps
    }

    private fun generateQRCode(data: String, width: Int, height: Int): Bitmap? {
        return try {
            val bitMatrix = com.google.zxing.MultiFormatWriter().encode(
                data,
                com.google.zxing.BarcodeFormat.QR_CODE,
                width,
                height
            )
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bmp
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}




class TicketBitmapGenerator(private val context: Context) {

    fun generateTicketBitmapsFromJson(json: String): List<Bitmap> {
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
                    put("timestamp", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
                }

                ticketBitmaps.add(generateStyledTicket(ticketData))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ticketBitmaps
    }

    fun generateStyledTicket(data: JSONObject): Bitmap {
        val width = 600
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
            val logoScaled = logo.scale(250, 250)
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

        canvas.drawText("Amount: Rs.${data.getString("amount")}", width / 2f, y.toFloat(), paint); y += 30
        canvas.drawText("Payment Mode: ${data.getString("payment_mode")}", width / 2f, y.toFloat(), paint); y += 45

        // QR Code
//        val qrBitmap = generateQRCode(data.getString("qr_data"), 400)
        val qrBitmap = generateQRCode(data.getString("qr_data"), 500)
        qrBitmap?.let {
            val qrX = (width - it.width) / 2f
            canvas.drawBitmap(it, qrX, y.toFloat(), null)
            y += it.height + 30
        }

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
            val devLogo = BitmapFactory.decodeResource(context.resources, R.drawable.logo_innobles_0) //Inno logo
            val bankLogo = BitmapFactory.decodeResource(context.resources, R.drawable.logo_hdfc_0) // HDFC logo
            val devScaled = devLogo.scale(250, 42)
            val bankScaled = bankLogo.scale(250, 60)
            canvas.drawBitmap(devScaled, 10f, y.toFloat(), null)
            canvas.drawBitmap(bankScaled, width - 260f, y.toFloat(), null)
        } catch (e: Exception) {
            Log.w("TicketBitmapGenerator", "Footer logos not found")
        }

        y += 100
        paintSmall.textAlign = Paint.Align.LEFT

        canvas.drawText("Booked on : ${data.getString("timestamp")}", 20f, y.toFloat(), paintSmall); y += 25
        canvas.drawText("Booked by : ${data.getString("booked_by")}", 20f, y.toFloat(), paintSmall)

        y += 50

        canvas.drawText("=================== THANK YOU ===================", width / 2f, y.toFloat(), paint); y += 40

        return bitmap
    }

    private fun generateQRCode(data: String, size: Int): Bitmap? {
        return try {
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
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


    @Suppress("DEPRECATION")
    private fun drawMultilineText(
        canvas: Canvas,
        text: String,
        paint: Paint,
        width: Int,
        x: Float,
        y: Float
    ): Int {
        val textPaint = TextPaint(paint)

        val staticLayout = StaticLayout(
            text,
            textPaint,
            width,
            Layout.Alignment.ALIGN_CENTER,
            1.2f,
            0f,
            false
        )

        canvas.withTranslation(x, y) {
            staticLayout.draw(this)
        }

        return staticLayout.height
    }

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
        "park_name": "DR. BR AMBEDKAR PARK",
        "attraction_name": "ENTRY TICKET",
        "ticket_unique_id": "2567093814",
        "gst_number": "BDHFGFU345BFH",
        "booked_on": "10-04-2025 02:53 PM",
        "open_time": "07:00 AM",
        "close_time": "11:00 PM",
        "booked_by": "Operator",
        "visit_date": "10-04-2025",
        "visitor_type": "INDAIN-ADULT",
        "notes": "Ticket is non refundable and valid for same business day only.",
        "amount": "20",
        "additional_charge": "₹2",
        "total": "22",
        "payment_mode": "Card",
        "qr_data": "56748374646",
        "timestamp": "10-04-2025 02:53 PM"
    }
    """.trimIndent()

    val json = remember { JSONObject(jsonStr) }
    val bitmap = remember {
        TicketBitmapGenerator(context).generateStyledTicket(json)
    }

    TicketBitmapView(bitmap = bitmap)
}
