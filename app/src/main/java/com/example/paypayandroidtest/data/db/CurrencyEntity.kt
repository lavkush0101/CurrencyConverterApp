package com.example.paypayandroidtest.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * Created CurrencyEntity to create the table on the ROOM DB
 * */
@Entity(tableName = "Currency_data_table")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "disclaimer") var disclaimer: String? = null,
    @ColumnInfo(name = "license") var license: String?=null,
    @ColumnInfo(name = "rates") var ratesCurrency: Map<String, Double>,
    @ColumnInfo(name = "date") var stimestampource: String? = null,
    @ColumnInfo(name = "base") var base: String? = null
)
