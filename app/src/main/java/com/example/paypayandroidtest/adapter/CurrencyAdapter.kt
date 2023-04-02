package com.example.paypayandroidtest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paypayandroidtest.databinding.ItemGridLayoutBinding

/**
 * created CurrencyAdapter to provide a binding from an app-specific data set to views that are displayed within a RecyclerView.
 * */
class CurrencyAdapter(
    private val currencyList: MutableList<String>,
    private val currencyValue: MutableList<Double>,
    private val currencyMap: HashMap<String, Double>,
    private val inputAmount: String,
    private val selectedItem: String
) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyAdapter.ViewHolder {
        val binding =
            ItemGridLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.bind(
                currencyList[position],
                currencyValue[position],
                inputAmount.toDoubleOrNull()!!,
                currencyMap[selectedItem] ?: 1.0
            )
            Log.e(
                "TAG",
                "onBindViewHolder: ${currencyList[position]} +${currencyValue[position]} ..${inputAmount.toDoubleOrNull()!!} --${currencyMap[selectedItem]}"
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }


    class ViewHolder(private val itemGridLayoutBinding: ItemGridLayoutBinding) :
        RecyclerView.ViewHolder(itemGridLayoutBinding.root) {
        fun bind(
            currencyName: String,
            currencyValue: Double,
            inputAmount: Double,
            inputCurrencyAmountValue: Double
        ) {
            itemGridLayoutBinding.nameOfCurrency.text = currencyName
            if (inputAmount > 0) {
                val baseUSD = 1.0
                val newConvertCurrency = (currencyValue / inputCurrencyAmountValue) * inputAmount
//                val displayAmount: Double = (Math.round(newConvertCurrency * 1000.0) / 1000.0)
                itemGridLayoutBinding.currnecyValue.text = newConvertCurrency.toString()

            } else {
                itemGridLayoutBinding.currnecyValue.text = currencyValue.toString()
            }
        }
    }
}