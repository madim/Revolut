package com.example.revolut.presentation.adapter

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revolut.R
import com.example.revolut.domain.Currency
import com.example.revolut.presentation.Event
import com.example.revolut.presentation.util.afterTextChanged
import kotlinx.coroutines.channels.SendChannel

private const val RATE_FORMAT = "%.2f"

internal class RateViewHolder(
    private val root: View,
    private val events: SendChannel<Event>
) : RecyclerView.ViewHolder(root), View.OnClickListener {

    // private val image: ImageView = root.findViewById(R.id.image)
    // private val title: TextView = root.findViewById(R.id.title)
    private val subtitle: TextView = root.findViewById(R.id.subtitle)
    private val rate: EditText = root.findViewById(R.id.rate)

    private val isBaseCurrency: Boolean get() = adapterPosition == 0

    init {
        root.setOnClickListener(this)
        rate.afterTextChanged { text ->
            if (isBaseCurrency) events.offer(Event.QueryChanged(item!!, text.toString()))
        }
    }

    private var item: Currency? = null

    override fun onClick(view: View) {
        if (view == root) events.offer(Event.ItemClicked(item!!))
    }

    fun bind(item: Currency) {
        this.item = item

        subtitle.text = item.currency
        rate.setText(RATE_FORMAT.format(item.rate))
    }
}
