package com.example.revolut.presentation.adapter

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revolut.R
import com.example.revolut.presentation.RateItem

internal class RateViewHolder(
    root: View,
    private val callback: RatesAdapter.Callback
) : RecyclerView.ViewHolder(root), View.OnClickListener {

    private val image: ImageView = root.findViewById(R.id.image)
    private val title: TextView = root.findViewById(R.id.title)
    private val subtitle: TextView = root.findViewById(R.id.subtitle)
    private val rate: EditText = root.findViewById(R.id.rate)

    init {
        root.setOnClickListener(this)
        rate.setOnClickListener(this)
    }

    private var item: RateItem? = null

    override fun onClick(view: View) {
        if (view == rate) {
            callback.onRateClicked(item!!)
        } else {
            callback.onItemClicked(item!!)
        }
    }

    fun bind(item: RateItem) {
        this.item = item

        title.text = item.title
        subtitle.text = item.subtitle
        rate.setText("${item.rate}")
    }
}
