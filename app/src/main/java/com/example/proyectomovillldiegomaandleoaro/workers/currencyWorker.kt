package com.example.proyectomovillldiegomaandleoaro.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.proyectomovillldiegomaandleoaro.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.proyectomovillldiegomaandleoaro.data.Converters

class CurrencyWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://v6.exchangerate-api.com/v6/52011cf4751e20e69c0e22db/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(APIService::class.java)

            val baseCurrency = inputData.getString("BASE_CURRENCY") ?: "USD"

            val response = apiService.getCurrency(baseCurrency)

            if (response.isSuccessful) {
                val data: CurrencyResponse? = response.body()
                val conversionRates = data?.conversion_rates ?: emptyMap()

                val database = AppDatabase.getDatabase(applicationContext)
                val currencyDao = database.currencyDao()

                withContext(Dispatchers.IO) {
                    val conversionRatesString = Converters().fromMap(conversionRates)

                    currencyDao.insertCurrencyData(
                        CurrencyEntity(
                            baseCurrency = baseCurrency,
                            conversionRates = conversionRatesString ?: ""
                        )
                    )
                }

                Log.d("CurrencyWorker", "Datos guardados en Room: $conversionRates")
                Result.success()
            } else {
                Log.e("CurrencyWorker", "Error en la respuesta: ${response.code()}")
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("CurrencyWorker", "Error: ${e.message}")
            Result.failure()
        }
    }
}


