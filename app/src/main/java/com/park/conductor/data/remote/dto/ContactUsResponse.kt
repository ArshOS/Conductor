package com.park.conductor.data.remote.dto

data class ContactUsResponse(
    val address: String,
    val contact_number: String,
    val email: String,
    val message: String,
    val status: Boolean
)