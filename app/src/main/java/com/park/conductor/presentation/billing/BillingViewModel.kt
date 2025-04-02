package com.park.conductor.presentation.billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.park.conductor.data.remote.api.ApiController
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.api.HomeRepository
import com.park.conductor.data.remote.dto.AttractionDetailsResponse
import com.park.conductor.data.remote.dto.TicketPriceResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BillingViewModel: ViewModel() {

    private val _billing : MutableStateFlow<ApiState<TicketPriceResponse>> = MutableStateFlow(
        ApiState.loading())
    val billing: StateFlow<ApiState<TicketPriceResponse>> = _billing.asStateFlow()


    fun getBillingDetailsResponse(param : HashMap<String,Any>){
        viewModelScope.launch {
            try {
                val res = HomeRepository.billing(param = param, api = ApiController.getApi())

                if (res.status) {
                    _billing.value = ApiState.success(res)
                } else {
                    _billing.value = ApiState.Error(res.message.toString())
                }
            } catch (e: Exception) {
                _billing.value = ApiState.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }
}