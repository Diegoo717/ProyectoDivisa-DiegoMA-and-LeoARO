package com.example.proyectomovillldiegomaandleoaro.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.proyectomovillldiegomaandleoaro.data.Converters

@Entity(tableName = "currency_table")
@TypeConverters(Converters::class)
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val baseCurrency: String,
    val conversionRates: String,
    val timestamp: Long = System.currentTimeMillis()
)
