package com.jmdev.myutc.di

import com.jmdev.myutc.data.api.CharacterApi
import com.jmdev.myutc.data.repository.CharacterRepositoryImpl
import com.jmdev.myutc.domain.repository.CharacterRepository
import com.jmdev.myutc.presentation.character_list.CharacterListViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        OkHttpClient.Builder().build()
    }

    single {
        Retrofit.Builder().baseUrl("https://rickandmortyapi.com/api/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    single {
        get<Retrofit>().create(CharacterApi::class.java)
    }

    single<CharacterRepository> { CharacterRepositoryImpl(get()) }

    viewModel { CharacterListViewModel(get()) }
}