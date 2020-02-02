package com.rate.am.ui.listeners

import android.os.SystemClock
import android.view.View
import java.util.*
import kotlin.math.abs

abstract class DebounceOnClickListener : View.OnClickListener {
    private val lastClickMap: MutableMap<View, Long>
    private val minIntervalMillis: Long = 1000L

    init {
        this.lastClickMap = WeakHashMap()
    }

    abstract fun onDebounceClick(view: View)

    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap[clickedView]
        val currentTimestamp = SystemClock.uptimeMillis()

        lastClickMap[clickedView] = currentTimestamp
        if (previousClickTimestamp == null || abs(currentTimestamp - previousClickTimestamp) > minIntervalMillis) {
            onDebounceClick(clickedView)
        }
    }
}