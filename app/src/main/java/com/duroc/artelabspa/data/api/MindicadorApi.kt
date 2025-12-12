package com.duroc.artelabspa.data.api

import com.duroc.artelabspa.model.IndicadoresResponse
import retrofit2.http.GET

interface MindicadorApi {
    @GET("api")
    suspend fun obtenerIndicadores(): IndicadoresResponse
}