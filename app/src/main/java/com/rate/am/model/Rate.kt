package com.rate.am.model

import java.io.Serializable

data class Rate(
    val id: String,
    val title: String,
    val logo: String,
    val date: Long,
    val currencies: List<CurrencyContainer>
) : Serializable {

    fun getPresentableCurrencies(isCash: Boolean = true): List<PresentableCurrency> {
        val filteredCurrencies = if (isCash) {
            currencies.filter { it.cash != null }
        } else {
            currencies.filter { it.nonCash != null }
        }
        return List(filteredCurrencies.size) { position ->
            val currency = filteredCurrencies[position]
            PresentableCurrency(currency.title, if (isCash) currency.cash!! else currency.nonCash!!)
        }
    }
}