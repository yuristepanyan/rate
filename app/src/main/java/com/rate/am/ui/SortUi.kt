package com.rate.am.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.rate.am.R
import kotlinx.android.synthetic.main.ui_sort.view.*

@SuppressLint("CustomViewStyleable")
class SortUi @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var view: View
    private var textColorLight: Int = 0
    private var textColor: Int = 0

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.sort_ui_attribute, 0, 0
            )
            val title = resources.getText(
                typedArray.getResourceId(R.styleable.sort_ui_attribute_title, R.string.app_name)
            )
            typedArray.recycle()

            view = LayoutInflater.from(context).inflate(R.layout.ui_sort, this, true)
            view.title.text = title
        }

        textColor = ContextCompat.getColor(context, R.color.textColor)
        textColorLight = ContextCompat.getColor(context, R.color.textColorLight)
    }

    fun setSortSelected(isDesc: Boolean) {
        title.setTextColor(textColor)
        view.image.setImageResource(if (isDesc) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down)
    }

    fun setSortUnselected() {
        title.setTextColor(textColorLight)
        view.image.setImageResource(R.drawable.ic_arrow_drop_down_light)
    }
}