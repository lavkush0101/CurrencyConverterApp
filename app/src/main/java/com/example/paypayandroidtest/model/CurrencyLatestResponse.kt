package com.example.paypayandroidtest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class CurrencyLatestResponse(
    @SerializedName("disclaimer") var disclaimer: String? = null,
    @SerializedName("license") var license: String? = null,
    @SerializedName("timestamp") var timestamp: Double? = null,
    @SerializedName("base") var base: String? = null,
    @SerializedName("rates") var rates: Rates? = Rates()
)

