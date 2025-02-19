package com.park.conductor.data.remote.dto

data class LoginResponse(
    val message: String,
    val status: Boolean,
    val userInfo: UserInfo
)

data class UserInfo(
    val mobile: String,
    val userId: Int,
    val userName: String,
    val userType: String
)