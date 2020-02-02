package com.rate.am.model

import java.io.Serializable

data class CurrencyContainer(
    val title: String,
    val cash: Currency? = null,
    val nonCash: Currency? = null
) : Serializable