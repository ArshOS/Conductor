package com.park.conductor.data.remote.api 

object ApiController {
    fun getApi(): ApiService = ApiService.create()
}
