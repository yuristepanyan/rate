package com.rate.am.model

data class RatesResponse(
    val rates: List<Rate>
) {
    fun getPresentableRate(currency: String, isCash: Boolean): List<PresentableRate> {
        val list = mutableListOf<PresentableRate>()

        rates.forEach { rate ->
            val foundedCurrency = rate.currencies.find { it.title == currency }
            if (foundedCurrency != null) {
                if (isCash && foundedCurrency.cash != null) {
                    list.add(createPresentableRate(rate, foundedCurrency.cash))
                } else if (foundedCurrency.nonCash != null) {
                    list.add(createPresentableRate(rate, foundedCurrency.nonCash))
                }
            }
        }
        return list
    }

    private fun createPresentableRate(rate: Rate, currency: Currency) = PresentableRate(
        rate.id,
        rate.title,
        rate.logo,
        rate.date,
        currency
    )
}