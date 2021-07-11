package com.chrispetersnz.triviaisfun.network

import timber.log.Timber

class TriviaDBProvider(private val triviaDBService: TriviaDBService) {

    private var categories: List<TriviaDBService.TriviaCategory> = listOf()

    suspend fun getCategories(): List<TriviaDBService.TriviaCategory> {

        if (categories.isEmpty()) {
            categories = triviaDBService.getCategories().categories
        }

        for (category in categories) {
            Timber.d("Found category  ${category.name} with id ${category.id}")
        }

        return categories
    }

    suspend fun loadQuestionsForCategory(category: TriviaDBService.TriviaCategory): List<TriviaDBService.TriviaQuestion> {
        val response = triviaDBService.getQuestions(
            amount = 10,
            category = category.id
        )
        if (response.response != 0) {
            //TODO: Implement error handling
        }

        return response.questions
    }
}