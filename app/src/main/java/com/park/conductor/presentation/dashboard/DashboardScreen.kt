package com.park.conductor.presentation.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.park.conductor.common.utilities.Prefs
import com.park.conductor.data.remote.api.ApiConstant
import com.park.conductor.data.remote.api.ApiService
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.dto.DashboardResponse
import com.park.conductor.navigation.DummyPay
import com.park.conductor.presentation.dashboard.components.InfoCard
import com.park.conductor.ui.theme.Green40
import com.park.conductor.ui.theme.Green80

@Composable
fun DashboardScreenComposable(
    paddingValues: PaddingValues,
    navController: NavHostController,
    dashboardViewModel: DashboardViewModel = hiltViewModel()
) {

    val param = remember {
        ApiConstant.getBaseParam().apply {
            put("userid", Prefs.getLogin()?.userInfo?.userId ?: 0)
        }
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {

        if (ApiService.NetworkUtil.isInternetAvailable(context)) {
            dashboardViewModel.getDashboardResponse(param)
        } else {
            ApiService.NetworkUtil.showNoInternetDialog(context)
        }
    }

    val state by dashboardViewModel.dashboard.collectAsState()

    SetUpObserverDashboard(state, navController)
}

@Composable
fun SetUpObserverDashboard(
    state: ApiState<DashboardResponse>,
    navController: NavHostController,
) {
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
            BuildDashboardUI(state.data, navController)
            Log.d("TAG: ", "Dashboard2 API success")
        }

        is ApiState.Error -> {

            BuildDashboardUI(null, navController)

            Log.d("TAG: ", "Dashboard API Failure")
//            BuildApiFailUI(state.message, navController)
            Log.d("TAG: ", "Dashboard API Failure: errorMessage ${state.message}")

//            val errorMessage =
//                (state as ApiState.Error<DashboardResponse>).message ?: "Unknown error occurred"
//            val snackbarHostState = remember { SnackbarHostState() }
//
//            LaunchedEffect(errorMessage) {
//                snackbarHostState.showSnackbar(errorMessage)
//            }
//
//            Scaffold(
//                snackbarHost = {
//                    SnackbarHost(hostState = snackbarHostState)
//                },
//            ) { contentPadding ->
//                Log.d("TAG", contentPadding.toString())
//            }
        }
    }
}

@Composable
fun BuildDashboardUI(data: DashboardResponse?, navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 20.dp, 10.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InfoCard(
            title = "Tickets Sold",
            value = (data?.totalTickets ?: 0).toString(),
            icon = Icons.AutoMirrored.Outlined.StickyNote2
        )

        Spacer(modifier = Modifier.padding(10.dp))

        InfoCard(
            title = "Amount Collected",
            value = "₹ ${data?.totalAmount ?: 0}",
            icon = Icons.Outlined.CurrencyExchange
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Green80, RoundedCornerShape(12.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Outlined.History,
                contentDescription = null,
                tint = Green40
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "Transaction History",
                color = Green40,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            onClick = {
                navController.navigate(
                    DummyPay
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Green40, RoundedCornerShape(12.dp))
                .weight(1f),
            colors = ButtonColors(
                containerColor = Green40,
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Green40
            )
        ) {
            Text(
                text = "SELL TICKETS",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.padding(2.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Powered by ", color = Color.LightGray)
            Text(
                text = "INNOBLES",
                textDecoration = TextDecoration.Underline,
                color = Color.LightGray,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {

}
