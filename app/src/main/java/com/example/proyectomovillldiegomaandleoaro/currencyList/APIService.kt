package com.example.proyectomovillldiegomaandleoaro.currencyList

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface APIService {
    @GET("latest/{base}")
    suspend fun getCurrency(@Path("base") base: String): Response<CurrencyResponse>
}
