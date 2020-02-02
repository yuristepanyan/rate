package com.rate.am.arch.rates.adaper

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.rate.am.R
import com.rate.am.extensions.getFullUrl
import com.rate.am.extensions.loadImageCenterInside
import com.rate.am.model.PresentableRate
import com.rate.am.ui.listeners.DebounceOnClickListener
import kotlinx.android.synthetic.main.recycler_currency_item.view.*

class RatesAdapter(val context: Context, val glide: RequestManager) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var bestSell = 0f
    private var bestBuy = 0f

    var listener: RatesAdapterListener? = null
    var rates: List<PresentableRate>? = null
        set(value) {
            field = value
            calculateBestPrices()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_currency_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(rates?.get(position))
    }

    override fun getItemCount() = rates?.size ?: 0

    fun sortWithBuy(isDesc: Boolean) {
        rates = if (isDesc) {
            rates?.sortedByDescending { it.currency.buy }
        } else {
            rates?.sortedBy { it.currency.buy }
        }
        notifyDataSetChanged()
    }

    fun sortWithSell(isDesc: Boolean) {
        rates = if (isDesc) {
            rates?.sortedByDescending { it.currency.sell }
        } else {
            rates?.sortedBy { it.currency.sell }
        }
        notifyDataSetChanged()
    }

    private fun calculateBestPrices() {
        bestBuy = rates?.maxBy { it.currency.buy }?.currency?.buy ?: 0f
        bestSell = rates?.minBy { it.currency.sell }?.currency?.sell ?: 0f
    }

    interface RatesAdapterListener {
        fun rateClicked(id: String)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener(object : DebounceOnClickListener() {
                override fun onDebounceClick(view: View) {
                    rates?.let { rate ->
                        listener?.rateClicked(rate[adapterPosition].id)
                    }
                }
            })
        }

        fun bind(rate: PresentableRate?) {
            rate?.let {
                view.image.loadImageCenterInside(glide, it.logo.getFullUrl())
                view.name.text = it.title
                view.buy.text = context.getString(R.string.price_converter, it.currency.buy)
                view.sell.text = context.getString(R.string.price_converter, it.currency.sell)
                setStyle(it.currency.sell.compareTo(bestSell) == 0, view.sell)
                setStyle(it.currency.buy.compareTo(bestBuy) == 0, view.buy)
            }
        }

        private fun setStyle(isBest: Boolean, view: TextView) {
            if (isBest) {
                view.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                view.setTypeface(null, Typeface.BOLD)
            } else {
                view.setTextColor(ContextCompat.getColor(context, R.color.textColor))
                view.setTypeface(null, Typeface.NORMAL)
            }
        }
    }
}