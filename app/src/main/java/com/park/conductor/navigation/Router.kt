package com.park.conductor.navigation

import com.park.conductor.data.remote.dto.Data
import kotlinx.serialization.Contextual
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
object Dashboard

@Serializable
object DummyPay