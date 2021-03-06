package com.example.revolut

import com.example.revolut.data.CurrencyApi
import com.example.revolut.domain.CurrencyRepository
import com.example.revolut.presentation.CurrenciesViewModel
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

    factory { CurrencyRepository(currencyApi = get()) }

    viewModel { CurrenciesViewModel(currencyRepository = get()) }
}