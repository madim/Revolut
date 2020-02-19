package com.example.revolut.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revolut.domain.Rate
import com.example.revolut.domain.RateRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val UPDATE_DELAY = 1000L
private const val QUERY_DELAY = 200L

internal class RatesViewModel(
    private val rateRepository: RateRepository
) : ViewModel() {

    private val _events = Channel<Event>(Channel.RENDEZVOUS)
    val events: SendChannel<Event> get() = _events

    private val _rates = MutableLiveData<Rates>()
    val rates: LiveData<Rates> get() = _rates

    private var base = Rate(currency = "EUR", rate = 100.0)

    init {
        viewModelScope.launch {
            fun updateRatesJob() = launch {
                while (true) {
                    updateRates(scrollToTop = false)
                    delay(UPDATE_DELAY)
                }
            }
            var updateRatesJob: Job = updateRatesJob()
            var activeQuery = ""
            var activeQueryJob: Job? = null

            _events.consumeEach { event ->
                when (event) {
                    is Event.ItemClicked -> {
                        if (base != event.rate) {
                            base = event.rate
                            updateRates(scrollToTop = true)
                        }
                    }
                    is Event.QueryChanged -> {
                        val query = event.query
                        if (base == event.rate && query != activeQuery) {
                            activeQuery = query
                            activeQueryJob?.cancel()
                            updateRatesJob.cancel()

                            if (query == "") {
                                // base = base.copy(rate = 0.0)
                            } else {
                                activeQueryJob = launch {
                                    delay(QUERY_DELAY)
                                    // base = base.copy(rate = query.toDouble())
                                    updateRatesJob = updateRatesJob()
                                }
                            }
                        }
                    }
                }
            }
        }
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