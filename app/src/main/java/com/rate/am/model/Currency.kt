package com.rate.am.model

import java.io.Serializable

data class Currency(
    val buy: Float,
    val sell: Float
) : Serializable