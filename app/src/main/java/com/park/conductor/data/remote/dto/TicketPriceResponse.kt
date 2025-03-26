package com.park.conductor.data.remote.dto

import com.google.gson.annotations.SerializedName

// Define the main data class
data class TicketResponse(
    val status: Boolean,
    val message: String,
    @SerializedName("max_ticket") val maxTicket: String,
    val type: String,
    val pricing: Pricing
)

// Define a data class for the pricing structure
data class Pricing(
    @SerializedName("Indian") val indian: CategoryPricing? = null,
    @SerializedName("Foreigner") val foreigner: CategoryPricing? = null,
    @SerializedName("All") val all: CategoryPricing? = null
)

// Define a data class for different categories of pricing
data class CategoryPricing(
    val adult: Int? = null,
    val kid: Int? = null,
    val senior: Int? = null,
    val visitor: Int? = null
)
