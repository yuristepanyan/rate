package com.rate.am.di

import com.rate.am.R
import com.rate.am.arch.bankDetail.BankDetailViewModel
import com.rate.am.arch.rates.RatesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        RatesViewModel(
            get(),
            androidContext().resources.getStringArray(R.array.currancies)[0]
        )
    }

    viewModel { BankDetailViewModel(get()) }
}