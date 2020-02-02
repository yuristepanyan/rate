package com.rate.am.arch.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rate.am.R
import com.rate.am.arch.main.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_tab.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val adapter: ViewPagerAdapter by inject { parametersOf(this) }

    private lateinit var tabTitles: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabTitles = resources.getStringArray(R.array.tabs)
        initView()
        setTabChangeListener()
    }

    private fun initView() {
        viewPager.adapter = adapter
        TabLayoutMediator(
            tabs,
            viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = tabTitles[position]
            }).attach()

        initCustomTabViews()
    }

    private fun setTabChangeListener() {
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                initCustomTabViews()
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun initCustomTabViews() {
        for (position in 0 until tabs.tabCount) {
            val tab = tabs.getTabAt(position)

            if (tab?.customView != null) {
                setTabItemState(tab.isSelected, tab.customView!!.tabImage, tab.customView!!.tabTitle)
            } else {
                val view = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
                view.tabTitle.text = tabTitles[position]
                view.tabImage.setImageResource(R.drawable.ic_tab_item)

                setTabItemState(tab?.isSelected == true, view.tabImage, view.tabTitle)

                tab?.customView = view
            }
        }
    }

    private fun setTabItemState(isSelected: Boolean, image: ImageView, text: TextView) {
        if (isSelected) {
            setAlpha(1f, image, text)
        } else {
            setAlpha(0.6f, image, text)
        }
    }

    private fun setAlpha(alpha: Float, image: ImageView, text: TextView) {
        image.alpha = alpha
        text.alpha = alpha
    }
}
