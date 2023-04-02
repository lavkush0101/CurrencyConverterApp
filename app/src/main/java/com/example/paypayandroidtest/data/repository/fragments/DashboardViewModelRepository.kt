package com.example.paypayandroidtest.data.repository.fragments

import com.example.paypayandroidtest.data.db.CurrencyDAO
import com.example.paypayandroidtest.data.db.CurrencyEntity
import com.example.paypayandroidtest.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Created DashboardViewModelRepository to handle the API data local db
 * */
class DashboardViewModelRepository(private val currencyDAO: CurrencyDAO) {

   /**
    * created getCurrencyRates to the handle the API
    * */
    suspend fun getCurrencyRates(appId: String): Any {
        return try {
            val response = ApiService.invoke().getLatestCurrencyList(appId)
            if (response.isSuccessful) {
                response.body()!!
            } else {
                response.body()!!.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Created saveCurrencyRates to the save the data on the ROOM DB
     * */
     fun saveCurrencyRates(currencyEntity: CurrencyEntity) {
            currencyDAO.insertCurrencyRates(currencyEntity)

    }

    /**
     * Created deleteAll to the delete the data on the ROOM DB
     * */
    suspend fun deleteAll() {
         currencyDAO.deleteAll()
    }

   /**
    * Created to handle the fetch the data on the ROOM DB
    * */
     fun getAll(): Flow<List<CurrencyEntity>> {
         return currencyDAO.getAll()
    }

}

