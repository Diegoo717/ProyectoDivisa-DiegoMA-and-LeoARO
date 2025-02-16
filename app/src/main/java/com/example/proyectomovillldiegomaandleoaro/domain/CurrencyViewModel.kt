package com.example.proyectomovillldiegomaandleoaro.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.proyectomovillldiegomaandleoaro.data.AppDatabase
import com.example.proyectomovillldiegomaandleoaro.data.Converters
import kotlinx.coroutines.flow.map

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {
    private val currencyDao = AppDatabase.getDatabase(application).currencyDao()

    val latestCurrency = currencyDao.getLatestCurrency().map { currencyEntity ->
        Converters().toMap(currencyEntity?.conversionRates)
    }.asLiveData()
}
