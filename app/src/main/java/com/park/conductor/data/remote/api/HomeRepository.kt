package com.park.conductor.data.remote.api 

object HomeRepository {
    private val api: ApiService = ApiController.getApi()

    suspend fun login(param: HashMap<String, Any>, api: ApiService) = api.login(param)
    suspend fun dashboard(param: HashMap<String, Any>, api: ApiService) = api.dashboard(param)
    suspend fun attraction(param: HashMap<String, Any>, api: ApiService) = api.attraction(param)
    suspend fun billing(param: HashMap<String, Any>, api: ApiService) = api.billing(param)
    suspend fun continuePayment(param: HashMap<String, Any>, api: ApiService) = api.continuePayment(param)
    suspend fun updatePayment(param: HashMap<String, Any>, api: ApiService) = api.updatePayment(param)
}
