package com.example.revolut.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.revolut.R
import com.example.revolut.presentation.adapter.RatesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val ratesViewModel: RatesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ratesAdapter = RatesAdapter(ratesViewModel.events) {
            ratesViewModel.onItemClicked(it)
        }
        val ratesList: RecyclerView = findViewById(R.id.rates_list)
        ratesList.adapter = ratesAdapter

        ratesViewModel.rates.observe(this, Observer {
            ratesAdapter.submitList(it.rates) {
                if (it.scrollToTop) ratesList.scrollToPosition(0)
            }
        })
    }
}
