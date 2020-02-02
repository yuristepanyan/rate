package com.rate.am.arch.rates

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.rate.am.R
import com.rate.am.arch.bankDetail.BankDetailActivity
import com.rate.am.arch.rates.adaper.RatesAdapter
import com.rate.am.extensions.intentTo
import com.rate.am.model.Constants
import com.rate.am.ui.SortUi
import kotlinx.android.synthetic.main.fragment_rates.*
import kotlinx.android.synthetic.main.loading_layout.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RatesFragment : Fragment(), RatesAdapter.RatesAdapterListener {
    private val viewModel by viewModel<RatesViewModel>()
    private val adapter: RatesAdapter by inject { parametersOf(context, this) }

    private lateinit var currenciesArray: Array<String>
    private lateinit var typesArray: Array<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currenciesArray = resources.getStringArray(R.array.currancies)
        typesArray = resources.getStringArray(R.array.types)
        initView()
        bind()
        clicks()
    }

    private fun initView() {
        recycler.adapter = adapter

        currency.text = currenciesArray[0]
        type.text = typesArray[0]

        swipeToRefresh.setOnRefreshListener {
            viewModel.getRates()
        }
    }

    private fun clicks() {
        buy.setOnClickListener {
            changeStates(buy, sell, viewModel.sortBuyDesc)
            adapter.sortWithBuy(viewModel.sortBuyDesc)
            viewModel.sortByBuy()
        }

        sell.setOnClickListener {
            changeStates(sell, buy, viewModel.sortSellDesc)
            adapter.sortWithSell(viewModel.sortSellDesc)
            viewModel.sortBySell()
        }

        type.setOnClickListener { selectType() }
        currency.setOnClickListener { selectCurrency() }
    }

    private fun bind() {
        viewModel.loading.observe(this, Observer {
            if (!viewModel.loadingIsShown) {
                if (it == true) {
                    loading.visibility = VISIBLE
                } else {
                    loading.visibility = GONE
                    viewModel.loadingIsShown = true
                }
            } else {
                swipeToRefresh.isRefreshing = it == true
            }
        })

        viewModel.rates.observe(this, Observer {
            adapter.let { checkedAdapter ->
                checkedAdapter.rates = it
                when {
                    viewModel.sortedByBuy -> checkedAdapter.sortWithBuy(!viewModel.sortBuyDesc)
                    viewModel.sortedBySell -> checkedAdapter.sortWithSell(!viewModel.sortSellDesc)
                    else -> checkedAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.error.observe(this, Observer {
            Snackbar.make(recycler, it ?: "", Snackbar.LENGTH_LONG).show()
        })
    }

    private fun changeStates(active: SortUi, inactive: SortUi, isDesc: Boolean) {
        active.setSortSelected(isDesc)
        inactive.setSortUnselected()
    }

    private fun selectType() {
        AlertDialog.Builder(context)
            .setTitle(R.string.type)
            .setSingleChoiceItems(typesArray, if (viewModel.isCash) 0 else 1) { dialog, which ->
                viewModel.isCash = which == 0
                viewModel.updateData()
                type.text = typesArray[which]
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }

    private fun selectCurrency() {
        AlertDialog.Builder(context)
            .setTitle(R.string.currency)
            .setSingleChoiceItems(
                currenciesArray,
                currenciesArray.indexOf(viewModel.currency)
            ) { dialog, which ->
                viewModel.currency = currenciesArray[which]
                viewModel.updateData()
                currency.text = viewModel.currency
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }

    override fun rateClicked(id: String) {
        viewModel.getRateObject(id)?.let {
            startActivity(
                intentTo(BankDetailActivity::class.java)
                    .putExtra(Constants.RATE_DATA_KEY, it)
            )
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = RatesFragment()
    }
}
