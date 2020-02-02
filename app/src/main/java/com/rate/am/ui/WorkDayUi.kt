package com.rate.am.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.rate.am.R
import kotlinx.android.synthetic.main.ui_work_day.view.*

@SuppressLint("CustomViewStyleable")
class WorkDayUi  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var view: View

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.work_ui_attribute, 0, 0
            )
            val title = resources.getText(
                typedArray.getResourceId(R.styleable.work_ui_attribute_work_title, R.string.app_name)
            )
            typedArray.recycle()

            view = LayoutInflater.from(context).inflate(R.layout.ui_work_day, this, true)
            view.title.text = title
        }
    }

    fun setValue(text: String) {
        view.value.text = text
    }
}
