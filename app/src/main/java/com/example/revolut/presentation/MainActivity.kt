package com.example.revolut.presentation

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
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
import com.example.revolut.presentation.adapter.CurrencyAdapter
import com.example.revolut.presentation.util.EventObserver
import com.example.revolut.presentation.util.onScroll
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            currenciesViewModel.networkEvents.offer(NetworkEvent.OnAvailable)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            currenciesViewModel.networkEvents.offer(NetworkEvent.OnLost)
        }
    }

    private val currenciesViewModel: CurrenciesViewModel by viewModel()

    private var connectivityManager: ConnectivityManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currencyAdapter = CurrencyAdapter(currenciesViewModel.listEvents)
        val currencyList: RecyclerView = findViewById(R.id.currency_list)
        currencyList.adapter = currencyAdapter

        currenciesViewModel.currencies.observe(this, Observer {
            currencyAdapter.submitList(it.currencies) {
                if (it.scrollToTop) currencyList.scrollToPosition(0)
            }
        })

        currenciesViewModel.message.observe(this, EventObserver {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        hideKeyboardOnScroll(currencyList)

        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager = getSystemService()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    private fun hideKeyboardOnScroll(list: RecyclerView) {
        val touchSlop = ViewConfiguration.get(this).scaledTouchSlop
        var totalDy = 0
        list.onScroll { _, dy ->
            if (dy > 0) {
                totalDy += dy
                if (totalDy >= touchSlop) {
                    totalDy = 0

                    val inputMethodManager: InputMethodManager? = getSystemService()
                    inputMethodManager?.hideSoftInputFromWindow(list.windowToken, HIDE_NOT_ALWAYS)
                }
            }
        }
    }
}
