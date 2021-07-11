package com.chrispetersnz.triviaisfun.network

interface ITriviaDBProvider {
    suspend fun getCategories(): List<TriviaDBService.TriviaCategory>
    suspend fun loadQuestionsForCategory(category: TriviaDBService.TriviaCategory): List<TriviaDBService.TriviaQuestion>
}