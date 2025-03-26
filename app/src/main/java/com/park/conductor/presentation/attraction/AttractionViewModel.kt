package com.park.conductor.presentation.attraction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.park.conductor.data.remote.api.ApiController
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.api.HomeRepository
import com.park.conductor.data.remote.dto.DashboardResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AttractionViewModel: ViewModel() {

    private val _dashboard : MutableStateFlow<ApiState<DashboardResponse>> = MutableStateFlow(
        ApiState.loading())
    val dashboard: StateFlow<ApiState<DashboardResponse>> = _dashboard.asStateFlow()


    fun getDashboardResponse(param : HashMap<String,Any>){
        viewModelScope.launch {
            try {
                val res = HomeRepository.dashboard(param = param, api = ApiController.getApi())

                if (res.status) {
                    _dashboard.value = ApiState.success(res)
                } else {
                    _dashboard.value = ApiState.Error(res.message.toString())
                }
            } catch (e: Exception) {
                _dashboard.value = ApiState.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }
}