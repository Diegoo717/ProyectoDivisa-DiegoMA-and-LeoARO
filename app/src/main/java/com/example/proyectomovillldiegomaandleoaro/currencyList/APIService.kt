package com.example.proyectomovillldiegomaandleoaro.currencyList

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {

    @GET
    fun getCurrency(@Url url:String):Response<CurrencyResponse>
}