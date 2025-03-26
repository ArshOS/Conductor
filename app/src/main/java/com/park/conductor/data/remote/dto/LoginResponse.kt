package com.park.conductor.data.remote.dto

data class LoginResponse(
    val message: String,
    val status: Boolean,
    val user_info: UserInfo
)