package com.example.revolut.presentation

import com.example.revolut.domain.Currency

sealed class ListEvent {
    data class QueryChanged(val currency: Currency, val query: String) : ListEvent()
    data class ItemClicked(val currency: Currency) : ListEvent()
    data class BaseRecycled(val query: String) : ListEvent()
}