package com.example.paypayandroidtest.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.paypayandroidtest.model.CurrencyLatestResponse
import com.example.paypayandroidtest.utils.RatesConverter

/**
 * Created CurrencyDatabase to handle the data base class instance and define the database name and version ..
 * */
@Database(entities = [CurrencyEntity::class], version = 2, exportSchema = false)
@TypeConverters(RatesConverter::class)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract val currencyDAO: CurrencyDAO

    companion object {
        @Volatile
        private var INSTANCE: CurrencyDatabase? = null
        fun getInstance(context: Context): CurrencyDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CurrencyDatabase::class.java,
                        "currency_data_database"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}
