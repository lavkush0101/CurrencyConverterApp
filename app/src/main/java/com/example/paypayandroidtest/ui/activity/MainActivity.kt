package com.example.paypayandroidtest.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.paypayandroidtest.BR
import com.example.paypayandroidtest.R
import com.example.paypayandroidtest.base.BaseActivity
import com.example.paypayandroidtest.data.viewModelFactory.activity.MainViewModelFactory
import com.example.paypayandroidtest.databinding.ActivityMainBinding
import com.example.paypayandroidtest.viewmodel.activity.MainActivityViewModel

/**
 * created MainActivity to handle the navController for the fragment
 * */
class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    private lateinit var activityMainViewModel: MainActivityViewModel
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = getViewDataBinding()
        activityMainViewModel = getViewModel()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

    }

    override fun getBindingVariable(): Int {
        return BR.mainViewModel
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainActivityViewModel {
        activityMainViewModel =
            ViewModelProvider(this, MainViewModelFactory("name"))[MainActivityViewModel::class.java]
        return activityMainViewModel
    }

}