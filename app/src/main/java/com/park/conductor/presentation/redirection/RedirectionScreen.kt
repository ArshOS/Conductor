package com.park.conductor.presentation.redirection

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.park.conductor.common.utilities.Prefs
import com.park.conductor.data.remote.api.ApiConstant
import com.park.conductor.data.remote.api.ApiService
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.dto.ContinuePaymentResponse
import com.park.conductor.ezetap.EzeNativeSampleActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun RedirectionScreen(
    paddingValues1: PaddingValues,
    navController: NavHostController,
    tickets: String?,
    amount: Float?,
    ticketType: Int?,
    totalVisitors: Int?,
    attractionName: String?,
    attractionId: String?,
    redirectionViewModel: RedirectionViewModel = hiltViewModel()
) {

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val today = dateFormat.format(calendar.time)

    val paramMap: MutableMap<String, Any> = mutableMapOf()

    if (amount != null) {
        paramMap["total_amount"] = amount.toFloat()
    }

    if (Prefs.getLogin()?.userInfo?.parkId != null) {
        paramMap["placeid"] = Prefs.getLogin()?.userInfo?.parkId!!.toInt()
    }

    if (attractionId != null) {
        paramMap["tickettypeid"] = attractionId.toInt()
    }

    if (totalVisitors != null) {
        paramMap["total_visitor"] = totalVisitors.toInt()
    }

    paramMap["visit_date"] = today.toString()

    if (Prefs.getLogin()?.userInfo?.userId != null) {
        paramMap["userid"] = Prefs.getLogin()?.userInfo?.userId!!.toInt()
    }

    paramMap["payment_mode"] = 4

    val paramPrepared = "{\"tickets\":" + tickets + "," + Gson().toJson(paramMap).substring(1)

    Log.d("paramPrepared", paramPrepared)

    val param = remember {
        ApiConstant.getBaseParam().apply {
            put("ticket_info", paramPrepared)
        }
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (ApiService.NetworkUtil.isInternetAvailable(context)) {
            redirectionViewModel.getContinuePaymentResponse(param)
        } else {
            ApiService.NetworkUtil.showNoInternetDialog(context)
        }
    }

    val stateContinuePayment by redirectionViewModel.continuePayment.collectAsState()

    SetUpObserverContinuePayment(
        state = stateContinuePayment,
        navController = navController,
        amount = amount,
        attractionName = attractionName,
        attractionId = attractionId
    )

    Scaffold(
        topBar = {},
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Text(text = "Redirecting...")
            }
        },
        bottomBar = {}
    )
}

@Composable
fun SetUpObserverContinuePayment(
    state: ApiState<ContinuePaymentResponse>,
    navController: NavHostController,
    attractionName: String?,
    attractionId: String?,
    amount: Float?,
) {

    val context = LocalContext.current

    when (state) {
        is ApiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ApiState.Success -> {
            Log.d("TAG: ", "Continue Payment API success")
            Log.d("TAG: ", "Continue Payment ${state.data}")
            val intent = Intent(context, EzeNativeSampleActivity::class.java)
            intent.putExtra("ticket_unique_id", state.data.ticket_unique_id)
            intent.putExtra("park_name", Prefs.getLogin()?.userInfo?.parkName)
            intent.putExtra("amount", amount)
            intent.putExtra("attraction_name", attractionName)
            intent.putExtra("attraction_id", attractionId)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            context.startActivity(intent)
        }

        is ApiState.Error -> {
            Log.d("TAG: ", "Continue Payment API Failure: errorMessage ${state.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {

//    RedirectionScreen()

}
