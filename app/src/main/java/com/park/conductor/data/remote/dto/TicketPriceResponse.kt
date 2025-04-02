package com.park.conductor.data.remote.dto

import com.google.gson.annotations.SerializedName

// Define the main data class
data class TicketPriceResponse(
    val status: Boolean,
    val message: String,
    @SerializedName("max_ticket") val maxTicket: String,
    @SerializedName("age_group") val ageGroups: AgeGroups?,
    val type: String,
    val pricing: Pricing
)

data class AgeGroups(
    val kid: String,
    val adult: String,
    val senior: String
)

// Define a data class for the pricing structure
data class Pricing(
    @SerializedName("Indian") val indian: CategoryPricing? = null,
    @SerializedName("Foreigner") val foreigner: CategoryPricing? = null,
    @SerializedName("All") val all: CategoryPricing? = null
)

// Define a data class for different categories of pricing
data class CategoryPricing(
    val adult: Float? = null,
    val kid: Float? = null,
    val senior: Float? = null,
    val visitor: Float? = null
)