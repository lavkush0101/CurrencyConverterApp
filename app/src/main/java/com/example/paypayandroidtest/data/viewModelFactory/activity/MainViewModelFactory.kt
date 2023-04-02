package com.example.paypayandroidtest.data.viewModelFactory.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.paypayandroidtest.data.repository.activity.MainActivityRepository
import com.example.paypayandroidtest.viewmodel.activity.MainActivityViewModel

class MainViewModelFactory(val message: String) :ViewModelProvider.Factory {

    private var mainActivityRepository= MainActivityRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel(mainActivityRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}