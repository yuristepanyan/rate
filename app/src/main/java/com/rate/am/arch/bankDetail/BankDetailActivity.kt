package com.rate.am.arch.bankDetail

import android.graphics.Paint
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.RequestManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.rate.am.R
import com.rate.am.arch.bankDetail.adapter.DetailRatesAdapter
import com.rate.am.extensions.callIntent
import com.rate.am.extensions.getFullUrl
import com.rate.am.extensions.loadImageCenterInside
import com.rate.am.extensions.mapIntent
import com.rate.am.model.BankDetail
import com.rate.am.model.Constants
import com.rate.am.model.Rate
import com.rate.am.model.Workhours
import com.rate.am.ui.WorkDayUi
import kotlinx.android.synthetic.main.activity_bank_detail.*
import kotlinx.android.synthetic.main.content_parallax_app_bar.*
import kotlinx.android.synthetic.main.fragment_rates.*
import kotlinx.android.synthetic.main.loading_layout.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BankDetailActivity : AppCompatActivity() {

    private val viewModel by viewModel<BankDetailViewModel>()
    private val glide: RequestManager by inject()
    private val adapter: DetailRatesAdapter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_detail)
        setSupportActionBar(toolbar)

        viewModel.rate = intent.getSerializableExtra(Constants.RATE_DATA_KEY) as Rate

        bind()
        initRecycler()
        setInitialValues()
        titleLogic()
        clicks()

        viewModel.isCash = true
    }

    private fun initRecycler() {
        recycler.adapter = adapter
    }

    private fun bind() {
        viewModel.loading.observe(this, Observer {
            loading.visibility = if (it == true) VISIBLE else GONE
        })

        viewModel.error.observe(this, Observer {
            Snackbar.make(recycler, it ?: "", Snackbar.LENGTH_LONG).show()
        })

        viewModel.bankDetailData.observe(this, Observer { setValues(it) })

        viewModel.cureencies.observe(this, Observer { adapter.currencies = it })
    }

    private fun clicks() {
        cash.setOnClickListener {
            if (!viewModel.isCash) {
                viewModel.isCash = true
            }
        }

        noCash.setOnClickListener {
            if (viewModel.isCash) {
                viewModel.isCash = false
            }
        }

        showInTheMap.setOnClickListener {
            viewModel.latLng?.let {
                mapIntent(it, bankTitle.text.toString())
            }
        }

        phone.setOnClickListener { callIntent(phone.text.toString()) }
    }

    private fun setInitialValues() {
        viewModel.rate?.let {
            bankTitle.text = it.title
            image.loadImageCenterInside(glide, it.logo.getFullUrl())
        }
    }

    private fun setValues(data: BankDetail) {
        street.text = data.title
        address.text = data.address
        phone.text = data.contact
        phone.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        if (data.workHours.size < 2) {
            val workHour = data.workHours[0]
            setWorkHour(workHour)
        } else {
            data.workHours.forEach(this::setWorkHour)
        }
    }

    private fun setWorkHour(workHour: Workhours) {
        when (workHour.days) {
            Constants.MON_FRI -> workDays.setValue(workHour.hours)
            Constants.MON_SAT -> {
                workDays.setValue(workHour.hours)
                setWorkDayHours(saturday, workHour.hours)
            }
            Constants.MON_SUN -> {
                workDays.setValue(workHour.hours)
                setWorkDayHours(saturday, workHour.hours)
                setWorkDayHours(sunday, workHour.hours)
            }
            Constants.SAT -> setWorkDayHours(saturday, workHour.hours)
            Constants.SUN -> setWorkDayHours(sunday, workHour.hours)
            Constants.SAT_SUN -> {
                setWorkDayHours(saturday, workHour.hours)
                setWorkDayHours(sunday, workHour.hours)
            }
        }
    }

    private fun setWorkDayHours(view: WorkDayUi, value: String) {
        view.visibility = VISIBLE
        view.setValue(value)
    }

    private fun titleLogic() {
        var isShow = true
        var scrollRange = -1
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange ?: -1
            }
            if (scrollRange + verticalOffset < 10) {
                toolbarLayout.title = viewModel.rate?.title
                isShow = true
            } else if (isShow) {
                toolbarLayout.title = " "
                isShow = false
            }
        })
    }
}
