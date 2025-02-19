package com.park.conductor.data.remote.api 

object HomeRepository {
    private val api: ApiService = ApiController.getApi()

    suspend fun login(param: HashMap<String, Any>, api: ApiService) = api.login(param)
    suspend fun dashboard(param: HashMap<String, Any>, api: ApiService) = api.dashboard(param)
}
