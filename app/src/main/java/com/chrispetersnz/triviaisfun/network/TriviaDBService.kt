package com.chrispetersnz.triviaisfun.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.Serializable

interface TriviaDBService {

    data class TriviaCategory(val id: Int, val name: String) : Serializable

    data class TriviaCategories(@SerializedName("trivia_categories") val categories: List<TriviaCategory>)

    data class TriviaQuestion(
        val category: String,
        val type: String,
        val difficulty: String,
        val question: String,
        @SerializedName("correct_answer") val correctAnswer: String,
        @SerializedName("incorrect_answers") val incorrectAnswers: List<String>
    )

    data class QuestionResponse(
        val response: Int,
        @SerializedName("results") val questions: List<TriviaQuestion>
    )


    @GET("api_category.php")
    suspend fun getCategories(): TriviaCategories

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int
    ): QuestionResponse
}