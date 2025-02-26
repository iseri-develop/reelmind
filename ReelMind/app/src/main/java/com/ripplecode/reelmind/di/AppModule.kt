package com.ripplecode.reelmind.di

import com.ripplecode.reelmind.data.remote.ApiService
import com.ripplecode.reelmind.data.remote.AuthInterceptor
import com.ripplecode.reelmind.data.repository.MovieRepository
import com.ripplecode.reelmind.presentation.viewmodel.DetailViewModel
import com.ripplecode.reelmind.presentation.viewmodel.HomeViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    // Criando uma única instância do OkHttpClient
    single {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    // Criando uma única instância do Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/") // Ajuste para a URL correta da API
            .client(get()) // Usa o OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // Converte JSON para objetos Kotlin
            .build()
            .create(ApiService::class.java) // Cria a instância do ApiService
    }

    single { MovieRepository(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }

}