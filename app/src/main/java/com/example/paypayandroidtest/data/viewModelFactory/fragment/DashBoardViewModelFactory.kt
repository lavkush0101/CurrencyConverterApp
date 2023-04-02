package com.example.paypayandroidtest.data.viewModelFactory.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.paypayandroidtest.data.repository.fragments.DashboardViewModelRepository
import com.example.paypayandroidtest.data.db.CurrencyDAO
import com.example.paypayandroidtest.viewmodel.fragment.DashBoardViewModel

/**
 * created DashBoardViewModelFactory to handle the view model and customize the object
 * */
class DashBoardViewModelFactory(private val currencyDAO: CurrencyDAO) : ViewModelProvider.Factory {

    private var dashboardViewModelRepository = DashboardViewModelRepository(currencyDAO)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashBoardViewModel::class.java)){
            return DashBoardViewModel(dashboardViewModelRepository) as T
        }
        throw IllegalArgumentException("Unknown view model found")
    }
}