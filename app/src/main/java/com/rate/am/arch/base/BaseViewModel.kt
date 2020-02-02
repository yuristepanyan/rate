package com.rate.am.arch.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    protected fun handleError(exception: Throwable) {
        error.postValue(exception.message)
        loading.postValue(false)
    }
}