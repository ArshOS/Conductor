package com.park.conductor.data.remote.dto

data class AttractionDetailsResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)