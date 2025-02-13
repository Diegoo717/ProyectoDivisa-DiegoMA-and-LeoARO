package com.example.proyectomovillldiegomaandleoaro.currencyList

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate ejecutado")
        setContent {
            CurrencyApp()
        }
    }
}

@Composable
fun CurrencyApp(viewModel: CurrencyViewModel = viewModel()) {
    Log.d("CurrencyApp", "Iniciando Composable CurrencyApp")

    val currencyData by viewModel.currencyData
    val isLoading by viewModel.isLoading

    LaunchedEffect(Unit) {
        Log.d("CurrencyApp", "Ejecutando fetchCurrency")
        viewModel.fetchCurrency("USD")
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("💰 Tipo de Cambio", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        if (isLoading) {
            Log.d("CurrencyApp", "Cargando datos...")
            CircularProgressIndicator()
        } else {
            if (currencyData != null) {
                Log.d("CurrencyApp", "Datos obtenidos correctamente")
                LazyColumn {
                    items(currencyData!!.conversion_rates.toList()) { (currency, rate) ->
                        Log.d("CurrencyApp", "Mostrando moneda: $currency - $rate")
                        Text("🔹 $currency: $rate", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                }
            } else {
                Log.e("CurrencyApp", "No se pudieron obtener los datos")
                Text("⚠️ No se pudieron obtener los datos", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}