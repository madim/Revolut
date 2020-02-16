package com.example.revolut.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.revolut.R
import com.example.revolut.presentation.RateItem

internal class RatesAdapter(
    private val callback: Callback
) : ListAdapter<RateItem, RateViewHolder>(
    RateItemCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_rate, parent, false)
        return RateViewHolder(view, callback)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface Callback {
        fun onItemClicked(item: RateItem)
        fun onRateClicked(item: RateItem)
    }

    private object RateItemCallback : DiffUtil.ItemCallback<RateItem>() {
        override fun areItemsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
            return oldItem.subtitle == newItem.subtitle
        }

        override fun areContentsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
            return oldItem == newItem
        }
    }
}