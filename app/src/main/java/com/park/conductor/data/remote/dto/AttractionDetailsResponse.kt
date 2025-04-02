package com.park.conductor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AttractionDetailsResponse (
    val data: List<Data>,
    val message: String,
    val status: Boolean
)

data class Data (
    @SerializedName("attraction_id") val attractionId: String,
    @SerializedName("attraction_name") val attractionName: String,
    @SerializedName("attration_icon_url") val attractionIconUrl: String?
)