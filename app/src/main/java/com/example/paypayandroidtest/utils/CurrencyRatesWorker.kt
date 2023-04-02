package com.example.paypayandroidtest.utils

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.paypayandroidtest.data.db.CurrencyDatabase
import com.example.paypayandroidtest.data.db.CurrencyEntity
import com.example.paypayandroidtest.data.network.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/***
 * Created CurrencyRatesWorker class to handle the schedule task
 * */
class CurrencyRatesWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {

    companion object {
        const val KEY_WORKER = "KEY_WORKER"
    }

    override fun doWork(): Result {
        return try {
            CoroutineScope(Dispatchers.IO).launch {
                // call the API
                val response = ApiService.invoke().getLatestCurrencyList(APIConstant.APP_ID)
                if (response.isSuccessful) {
                    // Save data to the local database
                    val currencyLatestResponse = response.body()
                    val obj = Gson().toJsonTree(currencyLatestResponse?.rates).asJsonObject
                    val currencyMap: HashMap<String, Double> = HashMap<String, Double>()
                    for ((key, value) in obj.entrySet()) {
                        currencyMap.put(key, value.asDouble)
                        println("Key = $key Value = $value")
                    }
                    val currentDate = Date()
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("en", "IN"))
                    val currentDateTime: String = dateFormat.format(currentDate)
                    val currencyEntity = CurrencyEntity(
                        1,
                        currencyLatestResponse?.disclaimer,
                        currencyLatestResponse?.license,
                        currencyMap,
                        currentDateTime,
                        currencyLatestResponse?.base,
                    )
                    // create the data base
                    val currencyDatabase = Room.databaseBuilder(
                        applicationContext,
                        CurrencyDatabase::class.java,
                        "my_database"
                    ).build()

                    // save the data
                    currencyDatabase.currencyDAO.insertCurrencyRates(currencyEntity)
                    Log.e("TAG", "doWork: save the data ............", )
                }
            }
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val currentDateTime: String = dateFormat.format(currentDate)
            val outputData: Data = Data.Builder()
                .putString(KEY_WORKER, currentDateTime)
                .build()
            Result.success(outputData)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}