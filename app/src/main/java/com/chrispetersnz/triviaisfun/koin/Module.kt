package com.chrispetersnz.triviaisfun.koin

import com.chrispetersnz.triviaisfun.MainViewModel
import com.chrispetersnz.triviaisfun.network.ITriviaDBProvider
import com.chrispetersnz.triviaisfun.network.TriviaDBProvider
import com.chrispetersnz.triviaisfun.network.TriviaDBService
import com.chrispetersnz.triviaisfun.quiz.QuizViewModel
import com.chrispetersnz.triviaisfun.util.HtmlProvider
import com.chrispetersnz.triviaisfun.util.IHtmlProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideTriviaDBProvider(get()) }

    single { provideTriviaService(get()) }

    factory { provideHtmlProvider() }

    factory { provideOkHttpClient() }

    viewModel { MainViewModel(get()) }

    viewModel { QuizViewModel(get(), get()) }
}

fun provideTriviaDBProvider(triviaDBService: TriviaDBService): ITriviaDBProvider =
    TriviaDBProvider(triviaDBService)

fun provideHtmlProvider(): IHtmlProvider = HtmlProvider()

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