package com.example.paypayandroidtest.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertCurrencyRates(currencyEntity: CurrencyEntity)


    @Query("DELETE FROM Currency_data_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM Currency_data_table")
    fun getAll(): Flow<List<CurrencyEntity>>


}