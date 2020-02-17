package com.example.revolut.domain

import com.example.revolut.data.CurrencyApi

class RateRepository(
    private val currencyApi: CurrencyApi
) {

    suspend fun rates(base: Rate): List<Rate> {
        val response = currencyApi.currencyList(base.currency)
        val rates = response.rates.map { Rate(it.key, it.value) }

        return listOf(base) + rates
    }
}