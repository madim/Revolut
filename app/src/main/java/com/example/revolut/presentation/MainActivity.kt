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
import com.example.revolut.presentation.adapter.RatesAdapter
import com.example.revolut.presentation.util.EventObserver
import com.example.revolut.presentation.util.onScroll
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            ratesViewModel.networkEvents.offer(NetworkEvent.OnAvailable)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            ratesViewModel.networkEvents.offer(NetworkEvent.OnLost)
        }
    }

    private val ratesViewModel: RatesViewModel by viewModel()

    private var connectivityManager: ConnectivityManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ratesAdapter = RatesAdapter(ratesViewModel.listEvents)
        val rateList: RecyclerView = findViewById(R.id.rate_list)
        rateList.adapter = ratesAdapter

        ratesViewModel.currencies.observe(this, Observer {
            ratesAdapter.submitList(it.currencies) {
                if (it.scrollToTop) rateList.scrollToPosition(0)
            }
        })

        ratesViewModel.message.observe(this, EventObserver {
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

        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager = getSystemService()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }
}
