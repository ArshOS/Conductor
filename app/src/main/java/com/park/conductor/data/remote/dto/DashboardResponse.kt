package com.park.conductor.data.remote.dto

data class DashboardResponse(
    val message: String,
    val status: Boolean,
    val totalAmount: Float,
    val totalTickets: Int
)