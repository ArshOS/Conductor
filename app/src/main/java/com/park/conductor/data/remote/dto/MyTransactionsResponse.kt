package com.park.conductor.data.remote.dto

data class MyTransactionsResponse(
    val message: String,
    val my_transactions: List<MyTransaction>,
    val selected_visit_date: String,
    val status: Boolean
)

data class MyTransaction(
    val amount: Int,
    val booking_date: String,
    val ticket_unique_id: String,
    val visit_date: String,
    val visitor_count: String
)