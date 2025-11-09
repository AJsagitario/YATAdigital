package com.example.billetera_digital

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiUtils {
    private const val BASE_URL = "https://api.clinicagovision.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}