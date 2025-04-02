package com.park.conductor.presentation.billing

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.park.conductor.R
import com.park.conductor.common.components.NationalityTag
import com.park.conductor.common.components.PassengerCounterComposable
import com.park.conductor.common.utilities.Prefs
import com.park.conductor.data.remote.api.ApiConstant
import com.park.conductor.data.remote.api.ApiService
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.dto.TicketPriceResponse
import com.park.conductor.ezetap.EzeNativeSampleActivity
import com.park.conductor.navigation.Dashboard
import com.park.conductor.navigation.DummyPay
import com.park.conductor.presentation.attraction.TopBarComposable
import com.park.conductor.ui.theme.Green40

@Composable
fun BillingScreen(
    paddingValues1: PaddingValues,
    navController: NavHostController,
    attractionName: String?,
    attractionId: String?,
    billingViewModel: BillingViewModel = hiltViewModel()
) {

    val param = remember {
        ApiConstant.getBaseParam().apply {
            put("ticketid", attractionId?.trim()?.toInt() ?: 0)
        }
    }

    val context = LocalContext.current
    var amount by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(Unit) {

        if (ApiService.NetworkUtil.isInternetAvailable(context)) {
            billingViewModel.getBillingDetailsResponse(param)
        } else {
            ApiService.NetworkUtil.showNoInternetDialog(context)
        }
    }

    val stateBilling by billingViewModel.billing.collectAsState()

    Scaffold(
        topBar = {
            TopBarComposable(
                Icons.Filled.Menu,
                Prefs.getLogin()?.userInfo?.parkName,
                R.drawable.logo_lda
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                            .padding(20.dp),
                        text = attractionName.toString(),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    SetUpObserverBilling(stateBilling, navController)
                }

            }

        },
        bottomBar = {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Green40)
//                    .padding(all = 20.dp)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Total",
//                        style = MaterialTheme.typography.headlineLarge,
//                        color = Color.Gray,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Spacer(modifier = Modifier.padding(10.dp))
//                    Text(
//                        text = "₹$amount",
//                        color = if (amount == 0f) {
//                            Color.Gray
//                        } else Color.White,
//                        style = MaterialTheme.typography.headlineLarge,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                Icon(
//                    modifier = Modifier.size(30.dp),
//                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                    contentDescription = null,
//                    tint = if (amount == 0f) {
//                        Color.Gray
//                    } else Color.White,
//                )
//
//            }
        }
    )
}


@Composable
fun SetUpObserverBilling(
    state: ApiState<TicketPriceResponse>,
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
            BuildBillingUI(state.data, navController)
            Log.d("TAG: ", "Billing API success")
        }

        is ApiState.Error -> {

            BuildBillingUI(null, navController)

            Log.d("TAG: ", "Billing API Failure")
//            BuildApiFailUI(state.message, navController)
            Log.d("TAG: ", "Billing API Failure: errorMessage ${state.message}")

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
fun BuildBillingUI(data: TicketPriceResponse?, navController: NavHostController) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BuildCounterComposable(data, navController)

    }
}

@Composable
fun BuildCounterComposable(data: TicketPriceResponse?, navController: NavHostController) {

    var amount by remember {
        mutableFloatStateOf(0f)
    }

    var totalTickerCounter by remember {
        mutableIntStateOf(0)
    }

    /**
     * General counter
     */
    var counterGeneral by remember {
        mutableIntStateOf(0)
    }

    /**
     * Counters based on nationality
     */
    var counterIndian by remember {
        mutableIntStateOf(0)
    }
    var counterForeigner by remember {
        mutableIntStateOf(0)
    }

    /**
     * Counters based on age
     */
    var counterKid by remember {
        mutableIntStateOf(0)
    }
    var counterAdult by remember {
        mutableIntStateOf(0)
    }
    var counterSenior by remember {
        mutableIntStateOf(0)
    }

    /**
     * Counters based on nationality (Indian) and age
     */
    var counterIndianKid by remember {
        mutableIntStateOf(0)
    }
    var counterIndianAdult by remember {
        mutableIntStateOf(0)
    }
    var counterIndianSenior by remember {
        mutableIntStateOf(0)
    }

    /**
     * Counters based on nationality (Foreigner) and age
     */
    var counterForeignerKid by remember {
        mutableIntStateOf(0)
    }
    var counterForeignerAdult by remember {
        mutableIntStateOf(0)
    }
    var counterForeignerSenior by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(
        counterGeneral,
        counterIndian,
        counterForeigner,
        counterKid,
        counterAdult,
        counterSenior,
        counterIndianKid,
        counterIndianAdult,
        counterIndianSenior,
        counterForeignerKid,
        counterForeignerAdult,
        counterForeignerSenior
    ) {
        totalTickerCounter++
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            val totalTickets: Int =
                counterGeneral + counterIndian + counterForeigner + counterKid + counterAdult + counterSenior + counterIndianKid + counterIndianAdult + counterIndianSenior + counterForeignerKid + counterForeignerAdult + counterForeignerSenior
            val leftTotalCount = data?.maxTicket?.toInt()?.minus(totalTickets) ?: 0
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = "Left ",
                    color = if (leftTotalCount > 0) Green40 else Color.Red,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    modifier = Modifier
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    text = leftTotalCount.toString(),
                    color = if (leftTotalCount > 0) Green40 else Color.Red,
                    fontWeight = if (leftTotalCount > 0) FontWeight.Medium else FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            }
            when (data?.type) {
                "1" -> {
//            "pricing": {
//                "All": {
//                "visitor": 100
//            }
//            }
                    NationalityTag(
                        labels = listOf("Indian", "Foreigner"),
                        listOf(
                            painterResource(id = R.drawable.ic_india),
                            painterResource(id = R.drawable.ic_globe)
                        )
                    )
                    PassengerCounterComposable(
                        title = "Visitor",
                        rateInINR = data.pricing.all?.visitor ?: 0f,
                        count = counterGeneral,
                        leftTotalCount = leftTotalCount,
                        onIncrement = {
                            counterGeneral++
                            amount += data.pricing.all?.visitor ?: 0f
                        },
                        onDecrement = {
                            counterGeneral--
                            amount -= data.pricing.all?.visitor ?: 0f
                        }
                    )
                }

                "2" -> {
//            "pricing": {
//                "All": {
//                "adult": 30,
//                "kid": 20,
//                "senior": 40
//            }
//            }
                    NationalityTag(
                        labels = listOf("Indian", "Foreigner"),
                        listOf(
                            painterResource(id = R.drawable.ic_india),
                            painterResource(id = R.drawable.ic_globe)
                        )
                    )
                    PassengerCounterComposable(
                        title = "Kid",
                        subtitle = data.ageGroups?.kid,
                        rateInINR = data.pricing.all?.kid ?: 0f,
                        count = counterKid,
                        onIncrement = {
                            counterKid++
                            amount += data.pricing.all?.kid ?: 0f
                        },
                        onDecrement = {
                            counterKid--
                            amount -= data.pricing.all?.kid ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Adult",
                        subtitle = data.ageGroups?.adult,
                        rateInINR = data.pricing.all?.adult ?: 0f,
                        count = counterAdult,
                        onIncrement = {
                            counterAdult++
                            amount += data.pricing.all?.adult ?: 0f
                        },
                        onDecrement = {
                            counterAdult--
                            amount -= data.pricing.all?.adult ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Sr. Citizen",
                        subtitle = data.ageGroups?.senior,
                        rateInINR = data.pricing.all?.senior ?: 0f,
                        count = counterSenior,
                        onIncrement = {
                            counterSenior++
                            amount += data.pricing.all?.senior ?: 0f
                        },
                        onDecrement = {
                            counterSenior--
                            amount -= data.pricing.all?.senior ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                }

                "3" -> {
//            "pricing": {
//                "Indian": {
//                "visitor": 10
//            },
//                "Foreigner": {
//                "visitor": 20
//            }
//            }
                    NationalityTag(
                        labels = listOf("Indian"),
                        listOf(painterResource(id = R.drawable.ic_india))
                    )
                    PassengerCounterComposable(
                        title = "Visitor",
                        rateInINR = data.pricing.indian?.visitor ?: 0f,
                        count = counterIndian,
                        onIncrement = {
                            counterIndian++
                            amount += data.pricing.indian?.visitor ?: 0f
                        },
                        onDecrement = {
                            counterIndian--
                            amount -= data.pricing.indian?.visitor ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )

                    NationalityTag(
                        labels = listOf("Foreigner"),
                        listOf(painterResource(id = R.drawable.ic_globe))
                    )
                    PassengerCounterComposable(
                        title = "Visitor",
                        rateInINR = data.pricing.foreigner?.visitor ?: 0f,
                        count = counterForeigner,
                        onIncrement = {
                            counterForeigner++
                            amount += data.pricing.foreigner?.visitor ?: 0f
                        },
                        onDecrement = {
                            counterForeigner--
                            amount -= data.pricing.foreigner?.visitor ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                }

                "4" -> {
//            pricing": {
//            "Indian": {
//                "adult": 50,
//                "kid": 20,
//                "senior": 30
//            },
//            "Foreigner": {
//                "adult": 60,
//                "kid": 40,
//                "senior": 50
//            }
//        }
                    NationalityTag(
                        labels = listOf("Indian"),
                        painterResources = listOf(painterResource(id = R.drawable.ic_india))
                    )
                    PassengerCounterComposable(
                        title = "Kids",
                        subtitle = data.ageGroups?.kid,
                        rateInINR = data.pricing.indian?.kid ?: 0f,
                        count = counterIndianKid,
                        onIncrement = {
                            counterIndianKid++
                            amount += data.pricing.indian?.kid ?: 0f
                        },
                        onDecrement = {
                            counterIndianKid--
                            amount -= data.pricing.indian?.kid ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Adults",
                        subtitle = data.ageGroups?.adult,
                        rateInINR = data.pricing.indian?.adult ?: 0f,
                        count = counterIndianAdult,
                        onIncrement = {
                            counterIndianAdult++
                            amount += data.pricing.indian?.adult ?: 0f
                        },
                        onDecrement = {
                            counterIndianAdult--
                            amount -= data.pricing.indian?.adult ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Sr. Citizens",
                        subtitle = data.ageGroups?.senior,
                        rateInINR = data.pricing.indian?.senior ?: 0f,
                        count = counterIndianSenior,
                        onIncrement = {
                            counterIndianSenior++
                            amount += data.pricing.indian?.senior ?: 0f
                        },
                        onDecrement = {
                            counterIndianSenior--
                            amount -= data.pricing.indian?.senior ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )

                    NationalityTag(
                        labels = listOf("Foreigner"),
                        listOf(painterResource(id = R.drawable.ic_globe))
                    )
                    PassengerCounterComposable(
                        title = "Kids",
                        subtitle = data.ageGroups?.kid,
                        rateInINR = data.pricing.foreigner?.kid ?: 0f,
                        count = counterForeignerKid,
                        onIncrement = {
                            counterForeignerKid++
                            amount += data.pricing.foreigner?.kid ?: 0f
                        },
                        onDecrement = {
                            counterForeignerKid--
                            amount -= data.pricing.foreigner?.kid ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Adults",
                        subtitle = data.ageGroups?.adult,
                        rateInINR = data.pricing.foreigner?.adult ?: 0f,
                        count = counterForeignerAdult,
                        onIncrement = {
                            counterForeignerAdult++
                            amount += data.pricing.foreigner?.adult ?: 0f
                        },
                        onDecrement = {
                            counterForeignerAdult--
                            amount -= data.pricing.foreigner?.adult ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Sr. Citizens",
                        subtitle = data.ageGroups?.senior,
                        rateInINR = data.pricing.foreigner?.senior ?: 0f,
                        count = counterForeignerSenior,
                        onIncrement = {
                            counterForeignerSenior++
                            amount += data.pricing.foreigner?.senior ?: 0f
                        },
                        onDecrement = {
                            counterForeignerSenior--
                            amount -= data.pricing.foreigner?.senior ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                }

                else -> {

                }
            }
        }

        PayNowButtonComposable(amount, navController)
    }


}

@Composable
private fun PayNowButtonComposable(amount: Float, navController: NavHostController) {

    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(all = 20.dp)
            .fillMaxWidth()
            .background(Green40, RoundedCornerShape(12.dp))
            .padding(all = 20.dp)
            .clickable(
                enabled = amount > 0f
            ) {
                val intent = Intent(context, EzeNativeSampleActivity::class.java)
                context.startActivity(intent)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.headlineLarge,
                color = if (amount == 0f) Color.Gray else Color.LightGray,
                fontWeight = if (amount == 0f) FontWeight.Medium else FontWeight.Light
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "₹$amount",
                color = if (amount == 0f) {
                    Color.Gray
                } else Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = if (amount == 0f) {
                Color.Gray
            } else Color.White,
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {

    PayNowButtonComposable(100f, rememberNavController())


//    AttractionComposable(Data("1", "Entry Ticket", null), rememberNavController())

}
