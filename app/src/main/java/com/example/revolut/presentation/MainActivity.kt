package com.example.revolut.presentation

import android.os.Bundle
import android.view.ViewConfiguration
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.revolut.R
import com.example.revolut.presentation.adapter.RatesAdapter
import com.example.revolut.presentation.util.onScroll
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val ratesViewModel: RatesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ratesAdapter = RatesAdapter(ratesViewModel.events)
        val rateList: RecyclerView = findViewById(R.id.rate_list)
        rateList.adapter = ratesAdapter

        ratesViewModel.currencies.observe(this, Observer {
            ratesAdapter.submitList(it.currencies) {
                if (it.scrollToTop) rateList.scrollToPosition(0)
            }
        })

        ratesViewModel.message.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        val touchSlop = ViewConfiguration.get(this).scaledTouchSlop
        var totalDy = 0
        rateList.onScroll { _, dy ->
            if (dy > 0) {
                totalDy += dy
                if (totalDy >= touchSlop) {
                    totalDy = 0

                    val inputMethodManager: InputMethodManager? = getSystemService()
                    inputMethodManager?.hideSoftInputFromWindow(
                        rateList.windowToken,
                        HIDE_NOT_ALWAYS
                    )
                }
            }
        }
    }
}
