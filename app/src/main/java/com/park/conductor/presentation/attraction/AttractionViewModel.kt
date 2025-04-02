package com.park.conductor.presentation.attraction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.park.conductor.data.remote.api.ApiController
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.api.HomeRepository
import com.park.conductor.data.remote.dto.AttractionDetailsResponse
import com.park.conductor.data.remote.dto.DashboardResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AttractionViewModel: ViewModel() {

    private val _attraction : MutableStateFlow<ApiState<AttractionDetailsResponse>> = MutableStateFlow(
        ApiState.loading())
    val attraction: StateFlow<ApiState<AttractionDetailsResponse>> = _attraction.asStateFlow()


    fun getAttractionDetailsResponse(param : HashMap<String,Any>){
        viewModelScope.launch {
            try {
                val res = HomeRepository.attraction(param = param, api = ApiController.getApi())

                if (res.status) {
                    _attraction.value = ApiState.success(res)
                } else {
                    _attraction.value = ApiState.Error(res.message.toString())
                }
            } catch (e: Exception) {
                _attraction.value = ApiState.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }
}