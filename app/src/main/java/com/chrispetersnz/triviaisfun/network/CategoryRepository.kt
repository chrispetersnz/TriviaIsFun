package com.chrispetersnz.triviaisfun.network

import timber.log.Timber

class CategoryRepository(private val triviaDBService: TriviaDBService) {

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
}