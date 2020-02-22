package com.example.revolut.domain

import com.example.revolut.data.CurrencyApi
import com.example.revolut.data.CurrencyResponse

class CurrencyRepository(
    private val currencyApi: CurrencyApi
) {

    private val emptyResponse = CurrencyResponse(baseCurrency = "", rates = emptyMap())

    suspend fun currencies(base: Currency): List<Currency> {
        val response = currencyList(base.currency)
        val rates = response.rates.mapNotNull {
            if (it.key == base.currency) null
            else Currency(it.key, it.value)
        }

        return listOf(base) + rates
    }

    suspend fun currencies(base: Currency, rate: Float): List<Currency> {
        val response = currencyList(base.currency)
        val rates = response.rates.mapNotNull {
            if (it.key == base.currency) null
            else Currency(it.key, rate * it.value)
        }

        return listOf(base) + rates
    }

    private suspend fun currencyList(base: String): CurrencyResponse {
        return try {
            currencyApi.currencyList(base)
        } catch (e: Exception) {
            emptyResponse
        }
    }
}