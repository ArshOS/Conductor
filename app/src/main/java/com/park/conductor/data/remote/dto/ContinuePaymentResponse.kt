package com.park.conductor.data.remote.dto

data class ContinuePaymentResponse(
    val download_ticket: String,
    val message: String,
    val payment_mode: String,
    val status: Boolean,
    val ticket_unique_id: String,
    val userid: String
)