package com.park.conductor.data.remote.dto

data class UpdatePaymentResponse(

    val attraction_name: String,
    val booked_by: String,
    val booked_on: String,
    val close_time: String,
    val gst_number: String,
    val message: String,
    val notes: String,
    val open_time: String,
    val park_name: String,
    val payment_mode: String,
    val status: Boolean,
    val ticket_unique_id: String,
    val tickets: List<Ticket>,
    val visit_date: String

//    val download_ticket: String,
//    val message: String,
//    val status: Boolean,
//    val ticket_unique_id: String
)

data class Ticket(
    val amount: String,
    val qr_data: String,
    val visitor_type: String
)