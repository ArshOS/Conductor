package com.park.conductor.presentation.dashboard

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.park.conductor.R
import com.park.conductor.common.components.DottedLine
import com.park.conductor.common.components.NationalityTag
import com.park.conductor.common.components.PassengerCounterComposable
import com.park.conductor.common.utilities.Prefs
import com.park.conductor.data.remote.api.ApiConstant
import com.park.conductor.data.remote.api.ApiService
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.dto.DashboardResponse
import com.park.conductor.navigation.DummyPay
import com.park.conductor.presentation.MainActivity
import com.park.conductor.ui.theme.Green40
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

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

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 20.dp, 10.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier.clickable {
                Prefs.clear()
//                    navController.navigate(Login)


                val activity = context as? Activity
                activity?.apply {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            },
            text = "Logout"
        )

        Spacer(modifier = Modifier.padding(10.dp))
//        PassengerCounterComposable(title = "Kids", subtitle = "3-12 yrs", rateInINR = 199f)
//        PassengerCounterComposable(title = "Adults", subtitle = "12-60 yrs", rateInINR = 199f)
//        PassengerCounterComposable(title = "Sr. Citizens", subtitle = "60+ yrs", rateInINR = 199f)
        Spacer(modifier = Modifier.padding(5.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Green40, RoundedCornerShape(12.dp))
                .padding(15.dp)
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.Forest,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "InnoPark",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        DottedLine(
            color = Color.LightGray,
            strokeWidth = 2.dp,
            dashWidth = 6.dp,
            gapWidth = 4.dp,
            horizontalPadding = 15.dp
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Green40, RoundedCornerShape(12.dp))
                .padding(50.dp)
        ) {
            Text(
                text = "₹ 10",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Light,
                color = Color.White
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "/ visitor",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Light,
                color = Color.White
            )
        }
        DottedLine(
            color = Color.LightGray,
            strokeWidth = 2.dp,
            dashWidth = 6.dp,
            gapWidth = 4.dp,
            horizontalPadding = 15.dp
        )
        Button(
            onClick = {
                navController.navigate(
                    DummyPay
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Green40, RoundedCornerShape(12.dp))
                .padding(15.dp),
            colors = ButtonColors(
                containerColor = Green40,
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Green40
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "SELL TICKETS",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }



        Spacer(modifier = Modifier.padding(2.dp))
        Row(
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
    Column {
        Spacer(modifier = Modifier.padding(5.dp))
        Box(modifier = Modifier.padding(start = 10.dp)) {
            NationalityTag(
                listOf("Indian", "Foreigner"),
                listOf(
                    painterResource(id = R.drawable.ic_india),
                    painterResource(id = R.drawable.ic_globe)
                )
            )
        }
//        PassengerCounterComposable(title = "Kids", subtitle = "3-12 yrs", rateInINR = 199f)
//        PassengerCounterComposable(title = "Adults", subtitle = "12-60 yrs", rateInINR = 199f)
//        PassengerCounterComposable(title = "Sr. Citizens", subtitle = "60+ yrs", rateInINR = 199f)
        Spacer(modifier = Modifier.padding(5.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Green40, RoundedCornerShape(12.dp))
                .padding(15.dp)
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.Forest,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "InnoPark",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        DottedLine(
            color = Color.LightGray,
            strokeWidth = 2.dp,
            dashWidth = 6.dp,
            gapWidth = 4.dp,
            horizontalPadding = 15.dp
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Green40, RoundedCornerShape(12.dp))
                .padding(50.dp)
        ) {
            Text(
                text = "₹ 10",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Light,
                color = Color.White
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "/ visitor",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Light,
                color = Color.White
            )
        }
        DottedLine(
            color = Color.LightGray,
            strokeWidth = 2.dp,
            dashWidth = 6.dp,
            gapWidth = 4.dp,
            horizontalPadding = 15.dp
        )
        Button(
            onClick = {
//                navController.navigate(
//                    DummyPay
//                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Green40, RoundedCornerShape(12.dp))
                .padding(15.dp),
            colors = ButtonColors(
                containerColor = Green40,
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Green40
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "SELL TICKETS",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }

}
