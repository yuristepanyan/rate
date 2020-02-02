package com.rate.am.arch.rates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rate.am.arch.base.BaseViewModel
import com.rate.am.model.PresentableRate
import com.rate.am.model.RatesResponse
import com.rate.am.net.RatesApi
import kotlinx.coroutines.launch

class RatesViewModel(private val ratesApi: RatesApi, defaultCurrency: String) : BaseViewModel() {

    private var data: RatesResponse? = null

    val rates = MutableLiveData<List<PresentableRate>>()

    var loadingIsShown = false
    var currency = defaultCurrency
    var isCash = true
    var sortedByBuy = false
    var sortedBySell = false
    var sortBuyDesc = false
    var sortSellDesc = false

    init {
        getRates()
    }

    fun getRates() {
        viewModelScope.launch {
            runCatching {
                loading.postValue(true)
                ratesApi.getRatesAsync()
            }.onSuccess {
                try {
                    data = it.await()
                    rates.postValue(getPresentableRates())
                    loading.postValue(false)
                } catch (e: Throwable) {
                    handleError(e)
                }
            }.onFailure {
                handleError(it)
            }
        }
    }

    private fun getPresentableRates(currency: String = this.currency, isCash: Boolean = this.isCash) =
        data?.getPresentableRate(currency, isCash)

    fun updateData() {
        rates.postValue(getPresentableRates())
    }

    fun getRateObject(id: String) = data?.rates?.find { it.id == id }

    fun sortByBuy() {
        sortSellDesc = false
        sortedBySell = false
        sortBuyDesc = !sortBuyDesc
        sortedByBuy = true
    }

    fun sortBySell() {
        sortBuyDesc = false
        sortedByBuy = false
        sortSellDesc = !sortSellDesc
        sortedBySell = true
    }
}