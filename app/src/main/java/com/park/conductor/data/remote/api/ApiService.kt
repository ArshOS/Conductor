package com.park.conductor.data.remote.api 

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.park.conductor.common.utilities.App
import com.park.conductor.data.remote.dto.AttractionDetailsResponse
import com.park.conductor.data.remote.dto.ContactUsResponse
import com.park.conductor.data.remote.dto.ContinuePaymentResponse
import com.park.conductor.data.remote.dto.DashboardResponse
import com.park.conductor.data.remote.dto.LoginResponse
import com.park.conductor.data.remote.dto.MyTransactionsResponse
import com.park.conductor.data.remote.dto.TicketPriceResponse
import com.park.conductor.data.remote.dto.UpdatePaymentResponse
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("pos_user_login")
    suspend fun login(@QueryMap param: HashMap<String, Any>): LoginResponse

    @GET("pos_attraction_details")
    suspend fun attraction(@QueryMap param: HashMap<String, Any>): AttractionDetailsResponse

    @GET("pos_user_ticket_price")
    suspend fun billing(@QueryMap param: HashMap<String, Any>): TicketPriceResponse

    @POST("pos_continue_payment")
    suspend fun continuePayment(@QueryMap param: HashMap<String, Any>): ContinuePaymentResponse

    @POST("pos_update_payment")
    suspend fun updatePayment(@QueryMap param: HashMap<String, Any>): UpdatePaymentResponse

    @GET("pos_my_transactions")
    suspend fun myTransactions(@QueryMap param: HashMap<String, Any>): MyTransactionsResponse

    @GET("pos_contactus")
    suspend fun contactUs(@QueryMap param: HashMap<String, Any>): ContactUsResponse

    @GET("tspuser_dashboard")
    suspend fun dashboard(@QueryMap param: HashMap<String, Any>): DashboardResponse



    companion object {
        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            val client = OkHttpClient.Builder().apply {
                addInterceptor(logger)
                callTimeout(30, TimeUnit.SECONDS)
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
            }.build()

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(App.getINSTANCE().getHostUrl)
                .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(ApiService::class.java)
        }
    }

    object NetworkUtil {
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                   capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }

        fun showNoInternetDialog(context: Context) {
            AlertDialog.Builder(context)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .setCancelable(false)
                .show()
        }
    }
}
