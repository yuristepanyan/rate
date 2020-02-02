package com.rate.am.arch.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rate.am.arch.rates.RatesFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RatesFragment.newInstance()
            else -> Fragment()
        }
    }

    override fun getItemCount() = 4
}