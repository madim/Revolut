package com.example.revolut.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revolut.domain.Currency
import com.example.revolut.domain.CurrencyRepository
import com.example.revolut.presentation.util.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val UPDATE_DELAY = 1000L

internal class CurrenciesViewModel(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _listEvents = Channel<ListEvent>(Channel.RENDEZVOUS)
    val listEvents: SendChannel<ListEvent> get() = _listEvents

    private val _networkEvents = Channel<NetworkEvent>(Channel.RENDEZVOUS)
    val networkEvents: SendChannel<NetworkEvent> get() = _networkEvents

    private val _currencies = MutableLiveData<Currencies>()
    val currencies: LiveData<Currencies> get() = _currencies

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> get() = _message

    private var base = Currency(currency = "EUR", rate = 1f)

    init {
        viewModelScope.launch {
            var updateListJob: Job? = null

            launch {
                _networkEvents.consumeEach { event ->
                    when (event) {
                        is NetworkEvent.OnAvailable -> {
                            updateListJob?.cancel()
                            updateListJob = launch {
                                while (true) {
                                    updateList()
                                    delay(UPDATE_DELAY)
                                }
                            }
                        }
                        is NetworkEvent.OnLost -> {
                            _message.value = Event("Network is unavailable")
                            updateListJob?.cancel()
                        }
                    }
                }
            }

            launch {
                var activeQuery = ""

                _listEvents.consumeEach { event ->
                    when (event) {
                        is ListEvent.QueryChanged -> {
                            val query = event.query
                            if (query != activeQuery) {
                                activeQuery = query
                                updateListJob?.cancel()

                                if (query.isNotEmpty()) {
                                    updateListJob = launch {
                                        while (true) {
                                            onQueryChanged(query)
                                            delay(UPDATE_DELAY)
                                        }
                                    }
                                }
                            }
                        }
                        is ListEvent.ItemClicked -> {
                            if (base == event.currency) return@consumeEach
                            onItemClicked(event.currency)
                        }
                        is ListEvent.BaseRecycled -> {
                            val query = event.query
                            val rate = query.toFloatOrNull() ?: return@consumeEach
                            base = base.copy(rate = rate)
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateList() {
        val currencies = currencyRepository.currencies(base)
        if (currencies.isEmpty()) {
            _message.value = Event("Failed to fetch rates")
            return
        }
        _currencies.value = Currencies(currencies, false)
    }

    private suspend fun onItemClicked(currency: Currency) {
        base = currency.copy(rate = 1f)
        val currencies = currencyRepository.currencies(base)
        if (currencies.isEmpty()) {
            _message.value = Event("Failed to fetch rates")
            return
        }
        _currencies.value = Currencies(currencies, true)
    }

    private suspend fun onQueryChanged(query: String) {
        val rate = query.toFloatOrNull() ?: return

        val currencies = currencyRepository.currencies(base, rate)
        if (currencies.isEmpty()) {
            _message.value = Event("Failed to fetch rates")
            return
        }
        _currencies.value = Currencies(currencies, false)
    }

    data class Currencies(
        val currencies: List<Currency>,
        val scrollToTop: Boolean
    )
}