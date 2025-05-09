package com.park.conductor.presentation.drawer.my_transactions

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.park.conductor.R
import com.park.conductor.common.components.DatePickerComposable
import com.park.conductor.common.components.TransactionCardComposable
import com.park.conductor.common.utilities.Prefs
import com.park.conductor.common.utilities.getFormattedDate
import com.park.conductor.data.remote.api.ApiConstant
import com.park.conductor.data.remote.api.ApiService
import com.park.conductor.data.remote.api.ApiState
import com.park.conductor.data.remote.dto.MyTransactionsResponse
import com.park.conductor.presentation.post_transaction_screens.TransactionResultViewModel
import com.park.conductor.ui.theme.Green40
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionsScreen(
    paddingValues1: PaddingValues,
    navController: NavHostController,
    transactionResultViewModel: TransactionResultViewModel = hiltViewModel()
) {

//    userid, os_type, type, date
//
//    type: card, upi, cash, all
//    all -> if all payment
//    card-> if card tab selected
//    upi-> if UPI is selected
//    cash-> if cash tab is selected
//
//    date: selected date in (dd-mm-YYYY) format
//    by default current date would be selected

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDate by remember { mutableStateOf("$currentDay/${currentMonth + 1}/$currentYear") }

//    val formatedDate = getFormattedDate(
//        today = false,
//        format1 = "dd MMMM yyyy, EEEE",
//        format2 = "dd-MM-yyyy",
//        dateString = selectedDate.toString()
//    )
//
//    val param = remember {
//        ApiConstant.getBaseParam().apply {
//            put("type", "all")
//            put("date", formatedDate)
//        }
//    }
//
//    Log.d("TAG", "Update Params $param")
//
//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//        if (ApiService.NetworkUtil.isInternetAvailable(context)) {
//            transactionResultViewModel.getMyTransactionResponse(param)
//        } else {
//            ApiService.NetworkUtil.showNoInternetDialog(context)
//        }
//    }
//
//    val stateMyTransactions by transactionResultViewModel.myTransactions.collectAsState()

    Scaffold(
        topBar = {
            TopBarComposable(
                Icons.Filled.ArrowBackIosNew,
                Prefs.getLogin()?.userInfo?.parkName,
                R.drawable.logo_lda_white,
                navController
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TransactionsComposable(
                    transactionResultViewModel = transactionResultViewModel,
                    navController = navController,
                    selectedDate = selectedDate,
                    onDateChange = { selectedDate = it }
                )
                Log.d("TAG:", "selectedDate current $selectedDate")

            }

        }
    )

}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("d-m-yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


@Composable
fun SetUpObserverMyTransactions(
    state: ApiState<MyTransactionsResponse>,
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
            BuildMyTransactionsListUI(state.data, navController)
            Log.d("TAG: ", "MyTransactions API success")
        }

        is ApiState.Error -> {

            BuildMyTransactionsListUI(null, navController)

            Log.d("TAG: ", "MyTransactions API Failure")
//            BuildApiFailUI(state.message, navController)
            Log.d("TAG: ", "MyTransactions API Failure: errorMessage ${state.message}")

        }
    }
}

@Composable
fun BuildMyTransactionsListUI(data: MyTransactionsResponse?, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(data?.my_transactions ?: emptyList()) { transaction ->
            TransactionCardComposable(transaction, navController)
        }
    }
}

@Composable
fun TopBarComposable(
    menu: ImageVector,
    parkName: String?,
    logoLda: Int,
    navController: NavHostController
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
            Icon(
                modifier = Modifier.clickable {
                    navController.popBackStack()
                },
                imageVector = menu, contentDescription = null, tint = Color.White
            )
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
fun InfoBar(label: String, info: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .background(
                color = Color.LightGray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(20.dp)
    ) {
        Icon(modifier = Modifier.size(35.dp), imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.padding(10.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = info,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionsComposable(
    selectedDate: String,
    onDateChange: (String) -> Unit,
    navController: NavHostController,
    transactionResultViewModel: TransactionResultViewModel
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(20.dp),
            text = "My Transactions",
            textAlign = TextAlign.Center,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.padding(5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DatePickerComposable(
                placeholder = selectedDate,
                onItemSelected = onDateChange
            )
        }

        Spacer(Modifier.height(5.dp))

        var paymentMode by remember { mutableStateOf("All") }

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.padding(5.dp))
            Box(
                Modifier
                    .wrapContentSize()
                    .clickable {
                        paymentMode = "All"
                    }
            ) { PaymentModeTag("All", paymentMode) }
            Box(
                Modifier
                    .wrapContentSize()
                    .clickable {
                        paymentMode = "UPI"
                    }
            ) { PaymentModeTag("UPI", paymentMode) }
            Box(
                Modifier
                    .wrapContentSize()
                    .clickable {
                        paymentMode = "Cash"
                    }
            ) { PaymentModeTag("Cash", paymentMode) }
            Box(
                Modifier
                    .wrapContentSize()
                    .clickable {
                        paymentMode = "Card"
                    }
            ) { PaymentModeTag("Card", paymentMode) }

        }

        LaunchedEffect(paymentMode, selectedDate) {
            getTransactions(
                transactionResultViewModel = transactionResultViewModel,
                paymentMode = paymentMode,
                selectedDate = selectedDate,
                context = context
            )
        }

        val stateMyTransactions by transactionResultViewModel.myTransactions.collectAsState()

        SetUpObserverMyTransactions(stateMyTransactions, navController)


    }
}

fun getTransactions(
    transactionResultViewModel: TransactionResultViewModel,
    paymentMode: String,
    selectedDate: String,
    context: Context,
) {
    val formatedDate = getFormattedDate(
        today = false,
        format1 = "dd/MM/yyyy",
        format2 = "dd-MM-yyyy",
        dateString = selectedDate
    )
    Log.d("TAG", "onDateChange $selectedDate, formatedDate $formatedDate")

    val param = ApiConstant.getBaseParam().apply {
        put("type", paymentMode.lowercase(Locale.ROOT))
        put("date", formatedDate)
    }

    Log.d("TAG", "Update Params $param")

    if (ApiService.NetworkUtil.isInternetAvailable(context)) {
        transactionResultViewModel.getMyTransactionResponse(param)
    } else {
        ApiService.NetworkUtil.showNoInternetDialog(context)
    }
}


@Composable
fun PaymentModeTag(label: String, paymentMode: String) {
    Text(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .background(
                color = if (label == paymentMode) Green40 else Color.Transparent,
                shape = RoundedCornerShape(5.dp)
            )
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(5.dp),
                color = if (label == paymentMode) Color.Transparent else Color.Black
            )
            .padding(horizontal = 10.dp, vertical = 3.dp),
        text = label,
        color = if (label == paymentMode) Color.White else Color.Black
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {

//    val transactionResultViewModel = TransactionResultViewModel()
//
//    TransactionsComposable(LocalDate.now(), {}, ApiState<MyTransactionsResponse>, rememberNavController())
}
