package com.example.paypayandroidtest.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.paypayandroidtest.base.BaseViewModel
import com.example.paypayandroidtest.interfaces.DashboardInterface
import com.example.paypayandroidtest.data.repository.fragments.DashboardViewModelRepository
import com.example.paypayandroidtest.data.db.CurrencyEntity
import com.example.paypayandroidtest.model.CurrencyLatestResponse
import com.example.paypayandroidtest.utils.APIConstant
import com.example.paypayandroidtest.utils.MyApplication
import com.example.paypayandroidtest.utils.NetworkHelper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/***
 * Created DashBoardViewModel to handle the local Room DB data and also fetch API Data
 *
 */

class DashBoardViewModel(
    private val dashboardViewModelRepository: DashboardViewModelRepository,
) :
    BaseViewModel<DashboardInterface>() {
    //    val currencyRates = MutableLiveData<CurrencyLatestResponse>()
    val selectedItem = MutableLiveData<String>()
    val inputCurrency = MutableStateFlow<String>("0.0")

    // Created to handle the input changes
    fun onTextChanged(input: CharSequence, start: Int, before: Int, count: Int) {
        inputCurrency.value = input.toString()
    }

    /**
     * Created getCurrencyListRatesApi function to fetch Currency Latest Data on the ROOM DB
     * */
    fun getCurrencyListRatesApi() = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkHelper.isNetworkConnected(MyApplication.appContext)) {
            val res = dashboardViewModelRepository.getCurrencyRates(APIConstant.APP_ID)
            if (res is CurrencyLatestResponse) {
                deleteOfflineData()
                successGetCurrencyListRatesResponse(res)
            } else {
                errorGetCurrencyListRatesResponse(res.toString())
            }
        } else {
            navigator?.checkInternet(false)
        }
    }


    private fun errorGetCurrencyListRatesResponse(res: String) {

    }

   /**
    * Created successGetCurrencyListRatesResponse to the save the data on the ROOM DB
    * */
    private fun successGetCurrencyListRatesResponse(currencyLatestResponse: CurrencyLatestResponse) {
        try {
            val obj = Gson().toJsonTree(currencyLatestResponse.rates).asJsonObject
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
                currencyLatestResponse.disclaimer,
                currencyLatestResponse.license,
                currencyMap,
                currentDateTime,
                currencyLatestResponse.base,
            )
            saveCurrencyRatesLocal(currencyEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Created saveCurrencyRatesLocal function to handle the save data in the ROOM DB
     * */
    private fun saveCurrencyRatesLocal(currencyEntity: CurrencyEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            dashboardViewModelRepository.saveCurrencyRates(currencyEntity)
        }

    /**
     * Created fetchCurrencyList to fetch the response data on the local data
     * */
    val fetchCurrencyList : Flow<List<CurrencyEntity>> = dashboardViewModelRepository.getAll()
        .flowOn(Dispatchers.IO)


    /**
     * Created deleteOfflineData to handle the delete the database
     * */
    private fun deleteOfflineData() =
        viewModelScope.launch(Dispatchers.IO) {
            dashboardViewModelRepository.deleteAll()
        }


}



