package com.example.revolut.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.revolut.R
import com.example.revolut.domain.Currency
import com.example.revolut.presentation.ListEvent
import kotlinx.coroutines.channels.SendChannel

internal class CurrencyAdapter(
    private val events: SendChannel<ListEvent>
) : ListAdapter<Currency, CurrencyViewHolder>(
    CurrencyItemCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view, events)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: CurrencyViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

    private object CurrencyItemCallback : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.currency == newItem.currency
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Currency, newItem: Currency): Any? {
            if (oldItem == newItem) {
                return null
            }
            return Unit // Dummy value to prevent item change animation.
        }
    }
}