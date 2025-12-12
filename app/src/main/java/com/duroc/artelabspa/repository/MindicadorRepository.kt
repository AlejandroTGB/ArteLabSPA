package com.duroc.artelabspa.repository

import com.duroc.artelabspa.data.api.RetrofitInstance

class MindicadorRepository {
    suspend fun obtenerValorDolar(): Double {
        val respuesta = RetrofitInstance.api.obtenerIndicadores()
        return respuesta.dolar.valor
    }
}