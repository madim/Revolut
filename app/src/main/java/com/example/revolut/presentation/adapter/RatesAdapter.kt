package com.example.revolut.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.revolut.R
import com.example.revolut.domain.Currency
import com.example.revolut.presentation.Event
import kotlinx.coroutines.channels.SendChannel

internal class RatesAdapter(
    private val events: SendChannel<Event>
) : ListAdapter<Currency, RateViewHolder>(
    RateItemCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_rate, parent, false)
        return RateViewHolder(view, events)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: RateViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

    private object RateItemCallback : DiffUtil.ItemCallback<Currency>() {
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