package com.duroc.artelabspa.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("[https://jsonplaceholder.typicode.com](https://jsonplaceholder.typicode.com)")
            .addConverterFactory(GsonConverterFactory.create()) // Conversor JSON
            .build()
            .create(ApiService::class.java)
    }
}
