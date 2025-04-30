package com.park.conductor.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Attractions

@Serializable
data class Billing (
    val attractionName: String?,
    val attractionId: String?
)

@Serializable
data class Redirection(
    val tickets: String?,
    val amount: Float,
    val ticketType: Int,
    val totalVisitors: Int,
    val attractionName: String?,
    val attractionId: String?
)

@Serializable
object DeviceProfile

@Serializable
object MyTransactions

@Serializable
object Dashboard

@Serializable
object DummyPay