package com.chrispetersnz.triviaisfun.koin

import com.chrispetersnz.triviaisfun.network.CategoryRepository
import com.chrispetersnz.triviaisfun.MainViewModel
import com.chrispetersnz.triviaisfun.network.TriviaDBService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { CategoryRepository(get()) }

    single { provideTriviaService(get()) }

    factory { provideOkHttpClient() }

    viewModel { MainViewModel(get()) }


}

fun provideOkHttpClient(): OkHttpClient {
    val okHttpClientBuilder = OkHttpClient.Builder()
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    okHttpClientBuilder.addInterceptor(interceptor)
    return okHttpClientBuilder.build()
}

fun provideTriviaService(okHttpClient: OkHttpClient): TriviaDBService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    return retrofit.create(TriviaDBService::class.java)
}