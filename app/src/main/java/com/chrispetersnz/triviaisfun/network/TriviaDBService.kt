package com.chrispetersnz.triviaisfun.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface TriviaDBService {

    data class TriviaCategory(val id: Int, val name: String)

    data class TriviaCategories(@SerializedName("trivia_categories") val categories: List<TriviaCategory>)

    @GET("api_category.php")
    suspend fun getCategories(): TriviaCategories
}