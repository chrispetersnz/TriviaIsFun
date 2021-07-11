package com.chrispetersnz.triviaisfun.network

class CategoryRepository(private val triviaDBService: TriviaDBService) {

    private var categories: List<TriviaDBService.TriviaCategory> = listOf()

    suspend fun getCategories(): List<TriviaDBService.TriviaCategory> {

        if (categories.isEmpty()) {
            categories = triviaDBService.getCategories().categories
        }

        return categories
    }
}