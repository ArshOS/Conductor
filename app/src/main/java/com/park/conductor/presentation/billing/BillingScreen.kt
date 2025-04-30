package com.park.conductor.presentation.billing

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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
import com.google.gson.Gson
import com.park.conductor.R
import com.park.conductor.common.components.NationalityTag
import com.park.conductor.common.components.PassengerCounterComposable
import com.park.conductor.common.utilities.Prefs
import com.park.conductor.data.local.data_class.VisitorData
import com.park.conductor.data.remote.api.ApiConstant
import com.park.conductor.data.remote.api.ApiService
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.dto.TicketPriceResponse
import com.park.conductor.navigation.Redirection
import com.park.conductor.presentation.attraction.TopBarComposable
import com.park.conductor.ui.theme.Green40

const val COUNTER_TOTAL = "total"
const val COUNTER_GENERAL = "general"
const val COUNTER_INDIAN = "indian"
const val COUNTER_FOREIGNER = "foreigner"
const val COUNTER_KID = "kid"
const val COUNTER_ADULT = "adult"
const val COUNTER_SENIOR = "senior"
const val COUNTER_INDIAN_KID = "indian_kid"
const val COUNTER_INDIAN_ADULT = "indian_adult"
const val COUNTER_INDIAN_SENIOR = "indian_senior"
const val COUNTER_FOREIGNER_KID = "foreigner_kid"
const val COUNTER_FOREIGNER_ADULT = "foreigner_adult"
const val COUNTER_FOREIGNER_SENIOR = "foreigner_senior"

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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) // Drawer state
    val coroutineScope = rememberCoroutineScope() // For managing drawer state

    Scaffold(
        topBar = {
            TopBarComposable(
                coroutineScope,
                drawerState,
                Icons.Filled.Menu,
                Prefs.getLogin()?.userInfo?.parkName,
                R.drawable.logo_lda_white
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
                    SetUpObserverBilling(stateBilling, navController, attractionName, attractionId)
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
    attractionName: String?,
    attractionId: String?,
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
            BuildBillingUI(state.data, navController, attractionName, attractionId)
            Log.d("TAG: ", "Billing API success")
        }

        is ApiState.Error -> {

            BuildBillingUI(null, navController, attractionName, attractionId)
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
fun BuildBillingUI(
    data: TicketPriceResponse?,
    navController: NavHostController,
    attractionName: String?,
    attractionId: String?
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BuildCounterComposable(data, navController, attractionName, attractionId)

    }
}

@Composable
fun BuildCounterComposable(
    data: TicketPriceResponse?,
    navController: NavHostController,
    attractionName: String?,
    attractionId: String?
) {

    var amount by remember {
        mutableFloatStateOf(0f)
    }

    val counters = remember {
        mutableStateMapOf(
            "total" to 0,
            "general" to 0,
            "indian" to 0,
            "foreigner" to 0,
            "kid" to 0,
            "adult" to 0,
            "senior" to 0,
            "indian_kid" to 0,
            "indian_adult" to 0,
            "indian_senior" to 0,
            "foreigner_kid" to 0,
            "foreigner_adult" to 0,
            "foreigner_senior" to 0
        )
    }

    var totalTickerCounter by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(counters) {
        totalTickerCounter++
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            val totalTickets: Int = counters.values.sum()
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
//                    NationalityTag(
//                        labels = listOf("Indian", "Foreigner"),
//                        listOf(
//                            painterResource(id = R.drawable.ic_india),
//                            painterResource(id = R.drawable.ic_globe)
//                        )
//                    )
                    PassengerCounterComposable(
                        title = "PAX",
                        rateInINR = data.pricing.all?.visitor ?: 0f,
                        count = counters["general"]!!,
                        leftTotalCount = leftTotalCount,
                        onIncrement = {
                            counters[COUNTER_GENERAL] = counters[COUNTER_GENERAL]!! + 1
                            amount += data.pricing.all?.visitor ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_GENERAL] = counters[COUNTER_GENERAL]!! - 1
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
                        count = counters["kid"]!!,
                        onIncrement = {
                            counters[COUNTER_KID] = counters[COUNTER_KID]!! + 1
                            amount += data.pricing.all?.kid ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_KID] = counters[COUNTER_KID]!! - 1
                            amount -= data.pricing.all?.kid ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Adult",
                        subtitle = data.ageGroups?.adult,
                        rateInINR = data.pricing.all?.adult ?: 0f,
                        count = counters[COUNTER_ADULT]!!,
                        onIncrement = {
                            counters[COUNTER_ADULT] = counters[COUNTER_ADULT]!! + 1
                            amount += data.pricing.all?.adult ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_ADULT] = counters[COUNTER_ADULT]!! - 1
                            amount -= data.pricing.all?.adult ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Sr. Citizen",
                        subtitle = data.ageGroups?.senior,
                        rateInINR = data.pricing.all?.senior ?: 0f,
                        count = counters[COUNTER_SENIOR]!!,
                        onIncrement = {
                            counters[COUNTER_SENIOR] = counters[COUNTER_SENIOR]!! + 1
                            amount += data.pricing.all?.senior ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_SENIOR] = counters[COUNTER_SENIOR]!! - 1
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
                        count = counters[COUNTER_INDIAN]!!,
                        onIncrement = {
                            counters[COUNTER_INDIAN] = counters[COUNTER_INDIAN]!! + 1
                            amount += data.pricing.indian?.visitor ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_INDIAN] = counters[COUNTER_INDIAN]!! - 1
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
                        count = counters[COUNTER_FOREIGNER]!!,
                        onIncrement = {
                            counters[COUNTER_FOREIGNER] = counters[COUNTER_FOREIGNER]!! + 1
                            amount += data.pricing.foreigner?.visitor ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_FOREIGNER] = counters[COUNTER_FOREIGNER]!! - 1
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
                        count = counters[COUNTER_INDIAN_KID]!!,
                        onIncrement = {
                            counters[COUNTER_INDIAN_KID] = counters[COUNTER_INDIAN_KID]!! + 1
                            amount += data.pricing.indian?.kid ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_INDIAN_KID] = counters[COUNTER_INDIAN_KID]!! - 1
                            amount -= data.pricing.indian?.kid ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Adults",
                        subtitle = data.ageGroups?.adult,
                        rateInINR = data.pricing.indian?.adult ?: 0f,
                        count = counters[COUNTER_INDIAN_ADULT]!!,
                        onIncrement = {
                            counters[COUNTER_INDIAN_ADULT] = counters[COUNTER_INDIAN_ADULT]!! + 1
                            amount += data.pricing.indian?.adult ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_INDIAN_ADULT] = counters[COUNTER_INDIAN_ADULT]!! - 1
                            amount -= data.pricing.indian?.adult ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Sr. Citizens",
                        subtitle = data.ageGroups?.senior,
                        rateInINR = data.pricing.indian?.senior ?: 0f,
                        count = counters[COUNTER_INDIAN_SENIOR]!!,
                        onIncrement = {
                            counters[COUNTER_INDIAN_SENIOR] = counters[COUNTER_INDIAN_SENIOR]!! + 1
                            amount += data.pricing.indian?.senior ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_INDIAN_SENIOR] = counters[COUNTER_INDIAN_SENIOR]!! - 1
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
                        count = counters[COUNTER_FOREIGNER_KID]!!,
                        onIncrement = {
                            counters[COUNTER_FOREIGNER_KID] = counters[COUNTER_FOREIGNER_KID]!! + 1
                            amount += data.pricing.foreigner?.kid ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_FOREIGNER_KID] = counters[COUNTER_FOREIGNER_KID]!! - 1
                            amount -= data.pricing.foreigner?.kid ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Adults",
                        subtitle = data.ageGroups?.adult,
                        rateInINR = data.pricing.foreigner?.adult ?: 0f,
                        count = counters[COUNTER_FOREIGNER_ADULT]!!,
                        onIncrement = {
                            counters[COUNTER_FOREIGNER_ADULT] =
                                counters[COUNTER_FOREIGNER_ADULT]!! + 1
                            amount += data.pricing.foreigner?.adult ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_FOREIGNER_ADULT] =
                                counters[COUNTER_FOREIGNER_ADULT]!! - 1
                            amount -= data.pricing.foreigner?.adult ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                    PassengerCounterComposable(
                        title = "Sr. Citizens",
                        subtitle = data.ageGroups?.senior,
                        rateInINR = data.pricing.foreigner?.senior ?: 0f,
                        count = counters[COUNTER_FOREIGNER_SENIOR]!!,
                        onIncrement = {
                            counters[COUNTER_FOREIGNER_SENIOR] =
                                counters[COUNTER_FOREIGNER_SENIOR]!! + 1
                            amount += data.pricing.foreigner?.senior ?: 0f
                        },
                        onDecrement = {
                            counters[COUNTER_FOREIGNER_SENIOR] =
                                counters[COUNTER_FOREIGNER_SENIOR]!! - 1
                            amount -= data.pricing.foreigner?.senior ?: 0f
                        },
                        leftTotalCount = leftTotalCount
                    )
                }

                else -> {

                }
            }
        }

        PayNowButtonComposable(amount, navController, attractionName, attractionId, counters, data)
    }


}

@Composable
private fun PayNowButtonComposable(
    amount: Float,
    navController: NavHostController,
    attractionName: String?,
    attractionId: String?,
    counters: SnapshotStateMap<String, Int>,
    data: TicketPriceResponse?
) {

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

                val tickets: MutableList<VisitorData> = getVisitorDetails(data, counters)

                val ticketsJson = Gson().toJson(tickets)

                navController.navigate(
                    Redirection(
                        tickets = ticketsJson,
                        amount = amount,
                        ticketType = data?.type?.trim()?.toInt() ?: 0,
                        totalVisitors = counters.values.sum(),
                        attractionName = attractionName,
                        attractionId = attractionId
                    )
                )


//                val intent = Intent(context, EzeNativeSampleActivity::class.java)
//                intent.putExtra("park_name", Prefs.getLogin()?.userInfo?.parkName)
//                intent.putExtra("amount", amount)
//                intent.putExtra("attraction_name", attractionName)
//                intent.putExtra("attraction_id", attractionId)
//                context.startActivity(intent)
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

fun getVisitorDetails(
    data: TicketPriceResponse?,
    counters: SnapshotStateMap<String, Int>
): MutableList<VisitorData> {

    val pricingType: Int = data?.type?.trim()?.toInt() ?: 0
    val tickets: MutableList<VisitorData> = mutableListOf()

    when (pricingType) {
        1 -> {
            tickets.add(
                VisitorData(
                    "All",
                    "General",
                    counters[COUNTER_GENERAL]!!,
                    data?.pricing?.all?.visitor ?: 0f
                )
            )
        }

        2 -> {
            if (counters[COUNTER_KID]!! > 0) {
                tickets.add(
                    VisitorData(
                        "All",
                        "Kid",
                        counters[COUNTER_KID]!!,
                        data?.pricing?.all?.kid ?: 0f
                    )
                )
            }
            if (counters[COUNTER_ADULT]!! > 0) {
                tickets.add(
                    VisitorData(
                        "All",
                        "Adult",
                        counters[COUNTER_ADULT]!!,
                        data?.pricing?.all?.adult ?: 0f
                    )
                )
            }
            if (counters[COUNTER_SENIOR]!! > 0) {
                tickets.add(
                    VisitorData(
                        "All",
                        "Senior",
                        counters[COUNTER_SENIOR]!!,
                        data?.pricing?.all?.senior ?: 0f
                    )
                )
            }
        }

        3 -> {
            if (counters[COUNTER_INDIAN]!! > 0) {
                tickets.add(
                    VisitorData(
                        "Indian",
                        "Visitor",
                        counters[COUNTER_INDIAN]!!,
                        data?.pricing?.indian?.visitor ?: 0f
                    )
                )
            }
            if (counters[COUNTER_FOREIGNER]!! > 0) {
                tickets.add(
                    VisitorData(
                        "Foreigner",
                        "Visitor",
                        counters[COUNTER_FOREIGNER]!!,
                        data?.pricing?.foreigner?.visitor ?: 0f
                    )
                )
            }
        }

        4 -> {
            if (counters[COUNTER_INDIAN_KID]!! > 0) {
                tickets.add(
                    VisitorData(
                        "Indian",
                        "Kid",
                        counters[COUNTER_INDIAN_KID]!!,
                        data?.pricing?.indian?.kid ?: 0f
                    )
                )
            }
            if (counters[COUNTER_INDIAN_ADULT]!! > 0) {
                tickets.add(
                    VisitorData(
                        "Indian",
                        "Adult",
                        counters[COUNTER_INDIAN_ADULT]!!,
                        data?.pricing?.indian?.adult ?: 0f
                    )
                )
            }
            if (counters[COUNTER_INDIAN_SENIOR]!! > 0) {
                tickets.add(
                    VisitorData(
                        "Indian",
                        "Senior",
                        counters[COUNTER_INDIAN_SENIOR]!!,
                        data?.pricing?.indian?.senior ?: 0f
                    )
                )
            }
            if (counters[COUNTER_FOREIGNER_KID]!! > 0) {
                tickets.add(
                    VisitorData(
                        "Foreigner",
                        "Kid",
                        counters[COUNTER_FOREIGNER_KID]!!,
                        data?.pricing?.foreigner?.kid ?: 0f
                    )
                )
            }
            if (counters[COUNTER_FOREIGNER_ADULT]!! > 0) {
                tickets.add(
                    VisitorData(
                        "Foreigner",
                        "Adult",
                        counters[COUNTER_FOREIGNER_ADULT]!!,
                        data?.pricing?.foreigner?.adult ?: 0f
                    )
                )
            }
            if (counters[COUNTER_FOREIGNER_SENIOR]!! > 0) {
                tickets.add(
                    VisitorData(
                        "Foreigner",
                        "Senior",
                        counters[COUNTER_FOREIGNER_SENIOR]!!,
                        data?.pricing?.foreigner?.senior ?: 0f
                    )
                )
            }
        }

        else -> {}
    }

    return tickets

}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {

//    PayNowButtonComposable(
//        100f,
//        rememberNavController(),
//        "attractionName",
//        "attractionId",
//
//    )


//    AttractionComposable(Data("1", "Entry Ticket", null), rememberNavController())

}
