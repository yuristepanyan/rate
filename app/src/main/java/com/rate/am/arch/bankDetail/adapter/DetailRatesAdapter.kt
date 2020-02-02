package com.rate.am.arch.bankDetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rate.am.R
import com.rate.am.model.PresentableCurrency
import kotlinx.android.synthetic.main.recycler_detail_currency_item.view.*

class DetailRatesAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var currencies: List<PresentableCurrency>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_detail_currency_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(currencies?.get(position))
    }

    override fun getItemCount() = currencies?.size ?: 0

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(currency: PresentableCurrency?) {
            currency?.let {
                view.name.text = it.title
                view.buy.text = context.getString(R.string.price_converter, it.currency.buy)
                view.sell.text = context.getString(R.string.price_converter, it.currency.sell)
            }
        }
    }
}