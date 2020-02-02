package com.rate.am.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.rate.am.arch.bankDetail.adapter.DetailRatesAdapter
import com.rate.am.arch.main.adapter.ViewPagerAdapter
import com.rate.am.arch.rates.adaper.RatesAdapter
import org.koin.dsl.module

val adapterModule = module {
    factory { (context: Context, listener: RatesAdapter.RatesAdapterListener) ->
        RatesAdapter(
            context,
            get()
        ).apply { this.listener = listener }
    }

    factory { (context: Context) -> DetailRatesAdapter(context) }

    factory { (activity: FragmentActivity) -> ViewPagerAdapter(activity) }
}