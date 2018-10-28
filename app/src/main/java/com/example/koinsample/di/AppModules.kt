package com.example.koinsample.di

import com.example.koinsample.main.MainViewModel
import com.example.koinsample.repository.*
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val myModule = module {
    single { Controller(get()) }
    single { BusinessService() }

    factory<HelloRepository> { HelloRepositoryImpl() }
    viewModel { MainViewModel(get(), get()) }
}

val apiModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Webservice::class.java)
    }
}

val appModules = listOf(myModule, apiModule)