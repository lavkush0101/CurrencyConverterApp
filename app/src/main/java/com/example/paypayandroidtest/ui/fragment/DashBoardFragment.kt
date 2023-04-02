package com.example.paypayandroidtest.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.paypayandroidtest.BR
import com.example.paypayandroidtest.R
import com.example.paypayandroidtest.adapter.CurrencyAdapter
import com.example.paypayandroidtest.base.BaseFragment
import com.example.paypayandroidtest.data.db.CurrencyDatabase
import com.example.paypayandroidtest.data.db.CurrencyEntity
import com.example.paypayandroidtest.data.viewModelFactory.fragment.DashBoardViewModelFactory
import com.example.paypayandroidtest.databinding.FragmentDashBoardBinding
import com.example.paypayandroidtest.interfaces.DashboardInterface
import com.example.paypayandroidtest.utils.CurrencyRatesWorker
import com.example.paypayandroidtest.viewmodel.fragment.DashBoardViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


/**
 * Created DashBoardFragment to handle the input amount, select the currency and render list of the list
 * */
class DashBoardFragment : BaseFragment<FragmentDashBoardBinding, DashBoardViewModel>(),
    DashboardInterface {

    private var fragmentDashBoardBinding: FragmentDashBoardBinding? = null
    private var dashBoardFragmentViewModel: DashBoardViewModel? = null

    override fun getLayout(): Int {
        return R.layout.fragment_dash_board
    }

    override fun getBindingVariable(): Int {
        return BR.dashBoardViewModel
    }

    override fun getViewModel(): DashBoardViewModel {
        val dao = CurrencyDatabase.getInstance(requireContext()).currencyDAO
        dashBoardFragmentViewModel = ViewModelProvider(
            this, DashBoardViewModelFactory(dao)
        )[DashBoardViewModel::class.java]
        return dashBoardFragmentViewModel!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDashBoardBinding = getViewDataBinding()
        dashBoardFragmentViewModel?.navigator = this

        currencyAPICallWithScheduleTime()
        fetchLocalDB()

    }


    /**
     * Created to handle the work manager to fetch API and save the updated data on local db and call API every 30 min
     * */
    private fun currencyAPICallWithScheduleTime() {
        dashBoardFragmentViewModel?.getCurrencyListRatesApi()
        val workManager = WorkManager.getInstance(requireContext())
        val constraints: Constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val currencyRatesWorkerPeriodic =
            PeriodicWorkRequestBuilder<CurrencyRatesWorker>(30, TimeUnit.MINUTES).setConstraints(
                    constraints
                ).build()

        workManager.enqueue(currencyRatesWorkerPeriodic)
        workManager.getWorkInfoByIdLiveData(currencyRatesWorkerPeriodic.id)
            .observe(viewLifecycleOwner) { workerInfo ->
                if (workerInfo.state.isFinished) {
                    dashBoardFragmentViewModel?.getCurrencyListRatesApi()
                    val data = workerInfo.outputData
                    val message = data.getString(CurrencyRatesWorker.KEY_WORKER)
                    Log.e("TAG", "currencyAPICallWithScheule_30min:  -> $message ")
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
    }

    /**
     * created initInputAmount to the input the ammount
     * */
    private fun initInputAmount(
        currencyList: MutableList<String>,
        currencyValue: MutableList<Double>,
        currencyMap: HashMap<String, Double>
    ) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    dashBoardFragmentViewModel?.inputCurrency?.collect() { inputAmount ->
                        if (inputAmount.isNotEmpty() && inputAmount.toDoubleOrNull()!! >= 0) {
                            fragmentDashBoardBinding?.currencyListRecyclerview?.adapter =
                                CurrencyAdapter(
                                    currencyList,
                                    currencyValue,
                                    currencyMap,
                                    inputAmount,
                                    getSelectedCurrency()
                                ).apply {
                                    notifyDataSetChanged()
                                }
                        } else {
                            Toast.makeText(context, "Please enter the amount", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *Created to handle the render the setup the recyclerview and render the list
     **/
    private fun initRecyclerview(
        currencyList: MutableList<String>,
        currencyValue: MutableList<Double>,
        currencyMap: HashMap<String, Double>
    ) {
        initSpinner(currencyList, currencyValue, currencyMap)
        fragmentDashBoardBinding?.currencyListRecyclerview?.layoutManager =
            GridLayoutManager(requireContext(), 3)
        fragmentDashBoardBinding?.currencyListRecyclerview?.adapter = CurrencyAdapter(
            currencyList,
            currencyValue,
            currencyMap,
            dashBoardFragmentViewModel!!.inputCurrency.value.toString(),
            getSelectedCurrency()
        ).apply { notifyDataSetChanged() }
    }

    /**
     * created to handle the fetch the offline data and render the list , spinner, apply the currency converter as input balance
     * */
    private fun fetchLocalDB() {
        val currencyList: MutableList<String> = ArrayList()
        val currencyValue: MutableList<Double> = ArrayList()
        val currencyMap: HashMap<String, Double> = HashMap<String, Double>()

        lifecycleScope.launch {
            dashBoardFragmentViewModel?.fetchCurrencyList?.collect {
                try {
                    Log.e("TAG", "fetchLocalDB: $it")
                    if (it.isNotEmpty()) {
                        val currencyEntity = it[0] as CurrencyEntity
                        for ((key, value) in currencyEntity.ratesCurrency) {
                            currencyList.add(key)
                            currencyValue.add(value)
                            currencyMap.put(key, value)
                        }
                        initRecyclerview(currencyList, currencyValue, currencyMap)
                        initInputAmount(currencyList, currencyValue, currencyMap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Created initSpinner to handle the currency selection
     * */

    private fun initSpinner(
        currencyList: MutableList<String>,
        currencyValue: MutableList<Double>,
        currencyMap: HashMap<String, Double>
    ) {
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, currencyList
        )
        fragmentDashBoardBinding?.autoCompleteTextView?.apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = currencyList[position]
                Log.d("TAG", "initSpinner: $selectedItem/...../${getSelectedCurrency()}")
                initRecyclerview(currencyList, currencyValue, currencyMap)
            }
        }
    }

    override fun apiSuccess(currencyEntity: CurrencyEntity) {

    }

    override fun apiError(code: Int, message: String) {
        val snackBar = Snackbar.make(
            fragmentDashBoardBinding!!.root.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
        snackBar.show()
    }

    override fun checkInternet(networkStatus: Boolean) {
        val snackBar = Snackbar.make(
            fragmentDashBoardBinding!!.root.findViewById(android.R.id.content),
            getString(R.string.network_not_connected),
            Snackbar.LENGTH_LONG
        )
        snackBar.show()

    }

    /**
     * Created getSelectedCurrency to the return  selected currency
     * */
    private fun getSelectedCurrency(): String {
        var selectedItem = ""
        dashBoardFragmentViewModel?.selectedItem?.observe(viewLifecycleOwner) {
            selectedItem = it
            Log.d("TAG", "getSelectedCurrency: $selectedItem")
        }
        return selectedItem
    }

}