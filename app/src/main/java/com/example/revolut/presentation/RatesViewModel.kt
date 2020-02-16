package com.example.revolut.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revolut.domain.RateRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class RatesViewModel(
    private val rateRepository: RateRepository
) : ViewModel() {

    private val _rates = MutableLiveData<List<RateItem>>()
    val rates: LiveData<List<RateItem>> = _rates

    private var baseCurrency: String = "EUR"

    init {
        viewModelScope.launch {
            while (true) {
                updateRates(baseCurrency)
                delay(1000)
            }
        }
    }

    fun onCurrencyClicked(currency: String) {
        baseCurrency = currency
    }

    private suspend fun updateRates(base: String) {
        val rates = rateRepository.rates(base)
        _rates.value = rates.map {
            RateItem(
                iconUrl = "",
                title = "",
                subtitle = it.currency,
                rate = it.rate
            )
        }
    }
}