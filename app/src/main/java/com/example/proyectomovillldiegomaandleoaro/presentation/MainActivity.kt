package com.example.proyectomovillldiegomaandleoaro.presentation

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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.proyectomovillldiegomaandleoaro.workers.CurrencyWorker
import java.util.concurrent.TimeUnit
import androidx.work.Constraints
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectomovillldiegomaandleoaro.domain.CurrencyViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inputData = workDataOf("BASE_CURRENCY" to "USD")

        val workRequest = PeriodicWorkRequestBuilder<CurrencyWorker>(1, TimeUnit.HOURS)
            .setInputData(inputData)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "currency_update",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    val exchangeRate = workInfo.outputData.getDouble("EXCHANGE_RATE", 0.0)
                    println("Tasa de cambio recibida: $exchangeRate")
                }
            }

        Log.d("MainActivity", "onCreate ejecutado")
        setContent {
            CurrencyScreen()
        }
    }
}

@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel = viewModel()) {
    val conversionRates by viewModel.latestCurrency.observeAsState(initial = emptyMap())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("\uD83D\uDCB8 Cambio de divisas \uD83D\uDCB8", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        if (conversionRates?.isEmpty() == true) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(conversionRates?.entries?.toList() ?: emptyList()) { (currency, rate) ->
                    Text("\uD83E\uDD90 $currency: $rate", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}


