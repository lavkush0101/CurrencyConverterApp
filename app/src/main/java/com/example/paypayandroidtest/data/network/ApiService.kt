package com.example.paypayandroidtest.data.network

import com.example.paypayandroidtest.model.CurrencyLatestResponse
import com.example.paypayandroidtest.utils.APIConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Created to ApiService to handle the API service and also handle the http log and retrofit builder
 * */
interface ApiService {

    @GET("latest.json")
    suspend fun getLatestCurrencyList(
        @Query("app_id") appId: String
    ): Response<CurrencyLatestResponse>


    companion object {
        operator fun invoke(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl(APIConstant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create()
        }
    }
}