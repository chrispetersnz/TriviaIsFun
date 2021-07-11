package com.chrispetersnz.triviaisfun.network

import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mockito.*

class CategoryRepositoryTest {

    @Test
    fun `categories are loaded from service on first call`() = runBlockingTest {
        //given
        val mockService = mock(TriviaDBService::class.java)
        val categoryRepository = CategoryRepository(mockService)
        val response = TriviaDBService.TriviaCategories(listOf())
        `when`(mockService.getCategories()).thenReturn(response)

        //when
        categoryRepository.getCategories()

        //then
        verify(mockService).getCategories()
    }

    @Test
    fun `categories are returned from cache if already loaded`() = runBlockingTest {
        //given
        val mockService = mock(TriviaDBService::class.java)
        val categoryRepository = CategoryRepository(mockService)
        val categoryList = listOf(TriviaDBService.TriviaCategory(id = 123, name = "Sports"))
        val response = TriviaDBService.TriviaCategories(categoryList)
        `when`(mockService.getCategories()).thenReturn(response)

        categoryRepository.getCategories()
        reset(mockService)

        //when
        val categories = categoryRepository.getCategories()

        //then
        assertThat(categories, `is`(categoryList))
        verify(mockService, never()).getCategories()
    }
}