package com.example.revolut.presentation

import com.example.revolut.domain.Rate

sealed class Event {
    data class QueryChanged(val rate: Rate, val query: String) : Event()
    data class ItemClicked(val rate: Rate) : Event()
}