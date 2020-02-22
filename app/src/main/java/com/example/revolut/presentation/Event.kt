package com.example.revolut.presentation

import com.example.revolut.domain.Currency

sealed class Event {
    data class QueryChanged(val currency: Currency, val query: String) : Event()
    data class ItemClicked(val currency: Currency) : Event()
    data class BaseRecycled(val query: String) : Event()
}