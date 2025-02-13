package com.example.proyectomovillldiegomaandleoaro.currencyList

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class CurrencyViewModel : ViewModel() {
    private val _currencyData = mutableStateOf<CurrencyResponse?>(null)
    val currencyData: State<CurrencyResponse?> = _currencyData

    private val _isLoading = mutableStateOf(true) // Estado de carga
    val isLoading: State<Boolean> = _isLoading

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://v6.exchangerate-api.com/v6/52011cf4751e20e69c0e22db/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(APIService::class.java)

    fun fetchCurrency(base: String) {
        viewModelScope.launch {
            _isLoading.value = true // Inicia la carga
            try {
                val response = apiService.getCurrency(base)
                if (response.isSuccessful) {
                    _currencyData.value = response.body()
                } else {
                    Log.e("CurrencyViewModel", "Error en la respuesta: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CurrencyViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false // Finaliza la carga
            }
            Log.d("CurrencyViewModel", "Datos recibidos: ${_currencyData.value}")
        }
    }
}
