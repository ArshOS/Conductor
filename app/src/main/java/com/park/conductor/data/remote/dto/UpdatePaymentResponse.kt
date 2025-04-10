package com.park.conductor.data.remote.dto

data class UpdatePaymentResponse(
    val download_ticket: String,
    val message: String,
    val status: Boolean,
    val ticket_unique_id: String
)