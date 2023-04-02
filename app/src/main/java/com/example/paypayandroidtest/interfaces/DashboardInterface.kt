package com.example.paypayandroidtest.interfaces

import com.example.paypayandroidtest.data.db.CurrencyEntity

interface DashboardInterface {
    fun apiSuccess(currencyEntity: CurrencyEntity)
    fun apiError(code: Int, message: String)
    fun checkInternet(networkStatus: Boolean)

}