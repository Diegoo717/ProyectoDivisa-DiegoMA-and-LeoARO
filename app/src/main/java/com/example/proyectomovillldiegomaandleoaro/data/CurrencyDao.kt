package com.example.proyectomovillldiegomaandleoaro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyData(currencyEntity: CurrencyEntity)

    @Query("SELECT * FROM currency_table ORDER BY timestamp DESC LIMIT 1")
    fun getLatestCurrency(): Flow<CurrencyEntity?>
}
