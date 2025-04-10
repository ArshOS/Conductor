package com.park.conductor.common.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QRCodeGenerator {

    public static Bitmap generateQRCode(String text, int width, int height) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
            BarcodeEncoder encoder = new BarcodeEncoder();
            return encoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null; // Return null in case of an error
        }
    }

    public static Bitmap generateQRCode1(String text, String labelText, int width, int height) {
        try {
            // Generate QR code bitmap
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap qrBitmap = encoder.createBitmap(bitMatrix);

            // Create bitmap for text
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);

            // Measure text height
            Rect textBounds = new Rect();
            paint.getTextBounds(labelText, 0, labelText.length(), textBounds);
            int textHeight = textBounds.height();

            // Create final bitmap: QR + Text
            int padding = 20;
            Bitmap combinedBitmap = Bitmap.createBitmap(
                    width,
                    height + textHeight + padding,
                    Bitmap.Config.ARGB_8888
            );

            Canvas canvas = new Canvas(combinedBitmap);
            canvas.drawColor(Color.WHITE); // optional background

            // Draw QR code
            canvas.drawBitmap(qrBitmap, 0, 0, null);

            // Draw text centered below the QR code
            canvas.drawText(labelText, width / 2f, height + textHeight, paint);

            return combinedBitmap;

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Bitmap> buildTicketsFromJson(Context context, String json, int width, int height) {
        List<Bitmap> ticketBitmaps = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(json);
            JSONArray tickets = root.getJSONArray("tickets");

            // Common data
            String parkName = root.getString("park_name");
            String attractionName = root.getString("attraction_name");
            String gstNumber = root.getString("gst_number");
            String bookedOn = root.getString("booked_on");
            String openTime = root.getString("open_time");
            String closeTime = root.getString("close_time");
            String paymentMode = root.getString("payment_mode");
            String bookedBy = root.getString("booked_by");
            String visitDate = root.getString("visit_date");
            String ticketUniqueId = root.getString("ticket_unique_id");

            for (int i = 0; i < tickets.length(); i++) {
                JSONObject ticket = tickets.getJSONObject(i);
                String visitorId = ticket.getString("visitor_id");
                String visitorName = ticket.getString("visitor_name");
                String visitorType = ticket.getString("visitor_type");
                String amount = ticket.getString("amount");

                // Build the label text to print
                String label = "Park: " + parkName + "\n" +
                        "Attraction: " + attractionName + "\n" +
                        "GST: " + gstNumber + "\n" +
                        "Booked On: " + bookedOn + "\n" +
                        "Timing: " + openTime + " - " + closeTime + "\n" +
                        "Payment: " + paymentMode + "\n" +
                        "Visitor ID: " + visitorId + "\n" +
                        "Name: " + visitorName + "\n" +
                        "Type: " + visitorType + "\n" +
                        "Amount: " + amount + "\n" +
                        "Booked By: " + bookedBy + "\n" +
                        "Visit Date: " + visitDate;

                // Create ticket bitmap using ticket_unique_id as QR code and full info as label
                Bitmap ticketBitmap = generateQRCode(ticketUniqueId, width, height);
                ticketBitmaps.add(ticketBitmap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ticketBitmaps;
    }


}
