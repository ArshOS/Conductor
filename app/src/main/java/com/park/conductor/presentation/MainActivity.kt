package com.park.conductor.presentation

import LoginScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ezetap.sdk.EzetapResponse
import com.park.conductor.ui.theme.ConductorTheme

import com.park.conductor.common.components.RippleAnywhereScreen
import com.park.conductor.common.utilities.Prefs
import com.park.conductor.data.remote.api.ApiConstant
import com.park.conductor.navigation.Dashboard
import com.park.conductor.navigation.DummyPay
import com.park.conductor.navigation.Login
import com.park.conductor.presentation.dashboard.DashboardScreenComposable
import com.park.conductor.presentation.dummy_pay.DummyPayComposable
import com.park.conductor.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

import com.ezetap.sdk.EzetapPayApis
import com.ezetap.sdk.EzetapPayApi
import com.park.conductor.navigation.Attractions
import com.park.conductor.navigation.Billing
import com.park.conductor.navigation.Redirection
import com.park.conductor.presentation.attraction.AttractionScreenComposable
import com.park.conductor.presentation.billing.BillingScreen
import com.park.conductor.presentation.redirection.RedirectionScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Ezetap SDK
//        Ezetap.initialize(this, "<YOUR_USERNAME>", "<YOUR_SECRET_KEY>", object : EzetapCallback {
//            override fun onSuccess(response: EzetapResponse) {
//                Log.d("Ezetap", "SDK Initialized Successfully")
//            }
//
//            override fun onFailure(error: String) {
//                Log.e("Ezetap", "SDK Initialization Failed: $error")
//            }
//        })

        enableEdgeToEdge()
        setContent {
            ConductorTheme {

                val loginViewModel: LoginViewModel = hiltViewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = if (Prefs.getLogin()?.userInfo?.userId != null) Attractions else Login,

                        ) {

                        composable<Login> {
                            LoginScreen(navController, { username, password ->
                                setParam(username, password, loginViewModel)
                            }, loginViewModel)
                        }

                        composable<Attractions> {
                            AttractionScreenComposable(PaddingValues(), navController)
                        }

                        composable<Billing> { it ->
                            val args = it.toRoute<Billing>()
                            BillingScreen(PaddingValues(), navController, args.attractionName, args.attractionId)
                        }

                        composable<Redirection> { it ->
                            val args = it.toRoute<Redirection>()
                            RedirectionScreen(PaddingValues(), navController, args.tickets, args.amount, args.ticketType, args.totalVisitors, args.attractionName, args.attractionId)
                        }

                        composable<Dashboard> {
                            DashboardScreenComposable(PaddingValues(), navController)
                        }

                        composable<DummyPay> {
                            DummyPayComposable(
                                paddingValues = PaddingValues(),
                                navController = navController
                            )
                        }

                    }
                }
            }
        }
    }

    private fun setParam(username: Any?, password: Any?, viewModel: LoginViewModel) {

//        https://innobles.com/eventx-himachal/app/dev/v2/tspuser_login?username=posmachine123&password=Posmachine@123&os_type=1

        Log.d("TAG", "Login API Called")

        val param = ApiConstant.getBaseParam()
        param["username"] = username ?: ""
        param["password"] = password ?: ""

        viewModel.getLoginResponse(param)

    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    RippleAnywhereScreen()

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConductorTheme {
        Greeting("Android")
    }
}
