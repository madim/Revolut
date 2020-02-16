package com.example.revolut

import com.example.revolut.data.CurrencyApi
import com.example.revolut.domain.RateRepository
import com.example.revolut.presentation.RatesViewModel
import com.squareup.moshi.Moshi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    single {
        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://hiring.revolut.codes/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit.create(CurrencyApi::class.java) as CurrencyApi
    }

    factory { RateRepository(currencyApi = get()) }

    viewModel { RatesViewModel(rateRepository = get()) }
}