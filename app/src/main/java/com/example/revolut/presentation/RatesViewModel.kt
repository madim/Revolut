package com.example.revolut.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revolut.domain.Rate
import com.example.revolut.domain.RateRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class RatesViewModel(
    private val rateRepository: RateRepository
) : ViewModel() {

    private val _rates = MutableLiveData<Rates>()
    val rates: LiveData<Rates> = _rates

    private var base = Rate(currency = "EUR", rate = 100.0)

    init {
        viewModelScope.launch {
            while (true) {
                updateRates(scrollToTop = false)
                delay(1000)
            }
        }
    }

    fun onItemClicked(item: Rate) {
        if (base == item) return

        base = item
        viewModelScope.launch {
            updateRates(scrollToTop = true)
        }
    }

    fun onRateChanged(item: Rate, newRate: CharSequence) {
        item
        newRate
    }

    private suspend fun updateRates(scrollToTop: Boolean) {
        val rates = rateRepository.rates(base)
        _rates.value = Rates(rates, scrollToTop)
    }

    data class Rates(
        val rates: List<Rate>,
        val scrollToTop: Boolean
    )
}