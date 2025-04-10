package com.park.conductor.presentation.post_transaction_screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.park.conductor.data.remote.api.ApiController
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.api.HomeRepository
import com.park.conductor.data.remote.dto.ContinuePaymentResponse
import com.park.conductor.data.remote.dto.LoginResponse
import com.park.conductor.data.remote.dto.UpdatePaymentResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class TransactionResultViewModel: ViewModel() {

    private val _updatePayment : MutableStateFlow<ApiState<UpdatePaymentResponse>> = MutableStateFlow(
        ApiState.loading())
    val updatePayment: StateFlow<ApiState<UpdatePaymentResponse>> = _updatePayment.asStateFlow()


    fun getUpdatePaymentResponse(param : HashMap<String,Any>){
        viewModelScope.launch {
            try {
                val res = HomeRepository.updatePayment(param = param, api = ApiController.getApi())

                if (res.status) {
                    _updatePayment.value = ApiState.success(res)
                } else {
                    Log.d("TAG","In else Condition")
                    _updatePayment.value = ApiState.Error(res.message.toString())
                }
            } catch (e: Exception) {
                Log.d("TAG","In Catch Condition")
                _updatePayment.value = ApiState.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }

}