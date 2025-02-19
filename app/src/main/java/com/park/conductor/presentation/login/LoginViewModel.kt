package com.park.conductor.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.park.conductor.data.remote.api.ApiController
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.api.HomeRepository
import com.park.conductor.data.remote.dto.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginViewModel: ViewModel() {

    private val _login : MutableStateFlow<ApiState<LoginResponse>> = MutableStateFlow(
        ApiState.loading())
    val login: StateFlow<ApiState<LoginResponse>> = _login.asStateFlow()


    fun getLoginResponse(param : HashMap<String,Any>){
        viewModelScope.launch {
            try {
                val res = HomeRepository.login(param = param, api = ApiController.getApi())

                if (res.status) {
                    _login.value = ApiState.success(res)
                } else {
                    Log.d("TAG","In else Condition")
                    _login.value = ApiState.Error(res.message.toString())
                }
            } catch (e: Exception) {
                Log.d("TAG","In Catch Condition")
                _login.value = ApiState.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }

}