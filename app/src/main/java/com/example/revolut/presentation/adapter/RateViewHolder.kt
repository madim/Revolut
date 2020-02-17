package com.example.revolut.presentation.adapter

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revolut.R
import com.example.revolut.domain.Rate
import com.example.revolut.presentation.util.afterTextChanged

internal class RateViewHolder(
    private val root: View,
    private val callback: RatesAdapter.Callback
) : RecyclerView.ViewHolder(root), View.OnClickListener {

    private val image: ImageView = root.findViewById(R.id.image)
    private val title: TextView = root.findViewById(R.id.title)
    private val subtitle: TextView = root.findViewById(R.id.subtitle)
    private val rate: EditText = root.findViewById(R.id.rate)

    init {
        root.setOnClickListener(this)
        rate.afterTextChanged { text ->
            callback.onRateChanged(item!!, text)
        }
    }

    private var item: Rate? = null

    override fun onClick(view: View) {
        if (view == root) callback.onItemClicked(item!!)
    }

    fun bind(item: Rate) {
        this.item = item

        subtitle.text = item.currency
        rate.setText("${item.rate}")
    }
}
