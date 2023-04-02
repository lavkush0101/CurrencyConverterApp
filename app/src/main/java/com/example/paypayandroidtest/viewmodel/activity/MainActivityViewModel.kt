package com.example.paypayandroidtest.viewmodel.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paypayandroidtest.data.repository.activity.MainActivityRepository
/**
 * created MainActivityViewModel to the handle the Main activity view model and logic
 * */
class MainActivityViewModel(mainActivityRepository: MainActivityRepository) :ViewModel() {

    val status=MutableLiveData<String>()
}