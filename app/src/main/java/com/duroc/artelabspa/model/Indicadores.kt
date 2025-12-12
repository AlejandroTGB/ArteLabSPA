package com.duroc.artelabspa.model

data class IndicadoresResponse(
    val dolar: DolarData
)
data class DolarData(
    val valor: Double
)