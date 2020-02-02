package com.rate.am.model

data class PresentableRate(
    val id: String,
    val title: String,
    val logo: String,
    val date: Long,
    val currency: Currency
)