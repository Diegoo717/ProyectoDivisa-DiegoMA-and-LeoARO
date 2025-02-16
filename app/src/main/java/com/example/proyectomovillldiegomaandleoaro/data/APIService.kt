package com.example.proyectomovillldiegomaandleoaro.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET("latest/{base}")
    suspend fun getCurrency(@Path("base") base: String): Response<CurrencyResponse>
}
