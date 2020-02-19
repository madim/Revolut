package com.example.revolut.presentation

import com.example.revolut.domain.Rate

data class QueryChangedEvent(
    val rate: Rate,
    val query: String
)