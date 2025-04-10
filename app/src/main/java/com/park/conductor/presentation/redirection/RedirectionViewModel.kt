package com.park.conductor.presentation.redirection

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.park.conductor.data.remote.api.ApiController
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.api.HomeRepository
import com.park.conductor.data.remote.dto.ContinuePaymentResponse
import com.park.conductor.data.remote.dto.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RedirectionViewModel: ViewModel() {

    private val _continuePayment : MutableStateFlow<ApiState<ContinuePaymentResponse>> = MutableStateFlow(
        ApiState.loading())
    val continuePayment: StateFlow<ApiState<ContinuePaymentResponse>> = _continuePayment.asStateFlow()


    fun getContinuePaymentResponse(param : HashMap<String,Any>){
        viewModelScope.launch {
            try {
                val res = HomeRepository.continuePayment(param = param, api = ApiController.getApi())

                if (res.status) {
                    _continuePayment.value = ApiState.success(res)
                } else {
                    Log.d("TAG","In else Condition")
                    _continuePayment.value = ApiState.Error(res.message.toString())
                }
            } catch (e: Exception) {
                Log.d("TAG","In Catch Condition")
                _continuePayment.value = ApiState.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }

}