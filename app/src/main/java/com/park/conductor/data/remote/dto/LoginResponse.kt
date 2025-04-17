package com.park.conductor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val message: String,
    val status: Boolean,
    @SerializedName("user_info") val userInfo: UserInfo
)

data class UserInfo(
    @SerializedName("park_id") val parkId: String,
    @SerializedName("park_name") val parkName: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_type") val userType: String,
    @SerializedName("login_user_name") val loginUsername: String,
    val mobile: String,
    val email: String,
)
