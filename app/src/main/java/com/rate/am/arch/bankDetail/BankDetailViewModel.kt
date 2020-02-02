package com.rate.am.arch.bankDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rate.am.arch.base.BaseViewModel
import com.rate.am.model.*
import com.rate.am.net.RatesApi
import kotlinx.coroutines.launch

class BankDetailViewModel(private val ratesApi: RatesApi) : BaseViewModel() {
    private var bankDetail: BankDetailResponse? = null

    val bankDetailData = MutableLiveData<BankDetail>()
    val cureencies = MutableLiveData<List<PresentableCurrency>>()

    var latLng: LatLng? = null

    var isCash = true
        set(value) {
            field = value
            getPresentableCurrencies()
        }

    var rate: Rate? = null
        set(value) {
            field = value
            field?.let {
                getBankInfo(it.id)
            }
        }

    private fun getBankInfo(id: String) {
        viewModelScope.launch {
            runCatching {
                loading.postValue(true)
                ratesApi.getBankDetailAsync(id)
            }.onSuccess {
                try {
                    bankDetail = it.await()
                    successHandling()
                } catch (e: Throwable) {
                    handleError(e)
                }
            }.onFailure {
                handleError(it)
            }
        }
    }

    private fun successHandling() {
        var head = bankDetail?.list?.firstOrNull { detail ->
            detail.head.compareTo(1) == 0
        }

        if(head == null) {
            head = bankDetail?.list?.first()
        }

        latLng = head?.location
        head?.let { bankDetail ->
            bankDetailData.postValue(bankDetail)
        }
        loading.postValue(false)
    }

    private fun getPresentableCurrencies() {
        rate?.let {
            cureencies.value = it.getPresentableCurrencies(isCash)
        }
    }
}