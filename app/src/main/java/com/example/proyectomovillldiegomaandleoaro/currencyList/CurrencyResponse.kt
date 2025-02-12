package com.example.proyectomovillldiegomaandleoaro.currencyList

data class CurrencyResponse(
    var time_last_update_unix: Int,
    var time_last_update_utc: String,
    var time_next_update_unix: Int,
    var time_next_update_utc: String,
    var base_code: String) {



}