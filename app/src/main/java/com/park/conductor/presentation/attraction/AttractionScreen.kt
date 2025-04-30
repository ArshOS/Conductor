package com.park.conductor.presentation.attraction

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.park.conductor.R
import com.park.conductor.common.utilities.CustomerInfo
import com.park.conductor.common.utilities.Prefs
import com.park.conductor.common.utilities.generateTicketBitmapsFromJson
import com.park.conductor.common.utilities.generateTicketFooterBitmap
import com.park.conductor.data.remote.api.ApiConstant
import com.park.conductor.data.remote.api.ApiService
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.dto.AttractionDetailsResponse
import com.park.conductor.data.remote.dto.Data
import com.park.conductor.navigation.Billing
import com.park.conductor.presentation.MainActivity
import com.park.conductor.presentation.post_transaction_screens.printLucknowQR
import com.park.conductor.ui.theme.Green40
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun AttractionScreenComposable(
    paddingValues1: PaddingValues,
    navController: NavHostController,
    attractionViewModel: AttractionViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    Log.d("TAG", "${Prefs.getLogin()?.userInfo}")

    val param = remember {
        ApiConstant.getBaseParam()
    }

    LaunchedEffect(Unit) {

        if (ApiService.NetworkUtil.isInternetAvailable(context)) {
            attractionViewModel.getAttractionDetailsResponse(param)
        } else {
            ApiService.NetworkUtil.showNoInternetDialog(context)
        }
    }

    val state by attractionViewModel.attraction.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) // Drawer state
    val coroutineScope = rememberCoroutineScope() // For managing drawer state

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(250.dp)
            ) {
                DrawerContent(navController, drawerState)
            }
        }
    ) {

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
                    SetUpObserverAttraction(state, navController)
                }

            }
        )
    }
}

@Composable
fun TopBarComposable(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    menu: ImageVector,
    parkName: String?,
    logoLda: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(all = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(modifier = Modifier.clickable {
                coroutineScope.launch { drawerState.open() }
            }, imageVector = menu, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = parkName.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
        Image(
            modifier = Modifier.size(50.dp),
            painter = painterResource(logoLda),
            contentDescription = null
        )

    }
}

@Composable
fun SetUpObserverAttraction(
    state: ApiState<AttractionDetailsResponse>,
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
            BuildAttractionsUI(state.data, navController)
            Log.d("TAG: ", "Attraction API success")
        }

        is ApiState.Error -> {

            BuildAttractionsUI(null, navController)

            Log.d("TAG: ", "Attraction API Failure")
//            BuildApiFailUI(state.message, navController)
            Log.d("TAG: ", "Attraction API Failure: errorMessage ${state.message}")

        }
    }
}

@Composable
fun BuildAttractionsUI(data: AttractionDetailsResponse?, navController: NavHostController) {

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth(),
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
            text = ""
        )

        val coroutineScope = rememberCoroutineScope()

        val ticketFooterBitmap = generateTicketFooterBitmap(
            context,
            "Ticket is non refundable and valid for same business day only."
        )

        Text(
            modifier = Modifier.clickable {
                coroutineScope.launch {
                    // Offload heavy bitmap generation to background thread
                    val tickets = withContext(Dispatchers.Default) {
                        generateTicketBitmapsFromJson(context, CustomerInfo.DUMMY_JSON_2_TICKETS)
                    }

                    tickets.forEachIndexed { _, bitmap ->
                        printLucknowQR(bitmap, ticketFooterBitmap, context)
//                        delay(1000L)
//                        printLucknowQRFooter(ticketFooterBitmap, context)
                        delay(1500L)
                    }
                }
            },
            text = ""
        )



        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(data?.data ?: emptyList()) { task ->
                AttractionComposable(attraction = task, navController = navController)
            }
        }

    }
}

@Composable
fun AttractionComposable(attraction: Data, navController: NavHostController) {

    var clicked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clickable {
                clicked = true
                navController.navigate(
                    Billing(
                        attraction.attractionName,
                        attraction.attractionId
                    )
                )
            }
            .padding(10.dp)
            .fillMaxWidth()
            .background(if (clicked) Green40 else Color.White, RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (clicked) Color.Transparent else Color.LightGray,
                RoundedCornerShape(12.dp)
            )
            .padding(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (attraction.attractionName.trim().lowercase(Locale.ROOT) == "breakfast") {
            Image(
                painter = painterResource(id = R.drawable.ic_breakfast),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
            )
        } else if (attraction.attractionName.trim().lowercase(Locale.ROOT) == "lunch") {
            Image(
                painter = painterResource(id = R.drawable.ic_lunch),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
            )
        } else if (attraction.attractionIconUrl.isNullOrEmpty()) {
            Icon(
                modifier = Modifier
                    .rotate(135f)
                    .size(30.dp),
                imageVector = Icons.Outlined.ConfirmationNumber,
                contentDescription = null,
                tint = if (clicked) Color.White else Color.Black
            )
        } else {
            AsyncImage(
                modifier = Modifier.size(30.dp),
                model = attraction.attractionIconUrl,
                contentDescription = "Translated description of what the image contains"
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = if (attraction.attractionName.trim()
                    .lowercase(Locale.ROOT) == "breakfast"
            ) "Breakfast - ₹10" else if (attraction.attractionName.trim()
                    .lowercase(Locale.ROOT) == "lunch"
            ) "Lunch - ₹10" else attraction.attractionName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (clicked) FontWeight.Bold else FontWeight.Medium,
            color = if (clicked) Color.White else Color.DarkGray
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {


    AttractionComposable(Data("1", "Entry Ticket", null), rememberNavController())

}
