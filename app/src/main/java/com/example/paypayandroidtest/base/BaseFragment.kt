package com.example.paypayandroidtest.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<T : ViewDataBinding, V : ViewModel> : Fragment() {

    private var mainFragmentBinding: T? = null
    private var mViewModel: V? = null

    abstract fun getLayout(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V

    fun getViewDataBinding():T{
        return mainFragmentBinding!!

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainFragmentBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        return mainFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        perFormDataBinding()

    }


    private fun perFormDataBinding() {
        mainFragmentBinding?.lifecycleOwner = viewLifecycleOwner
        mainFragmentBinding?.setVariable(getBindingVariable(), mViewModel)
        mainFragmentBinding?.executePendingBindings()
    }
}