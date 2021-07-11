package com.chrispetersnz.triviaisfun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrispetersnz.triviaisfun.network.CategoryRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(val categoryRepository: CategoryRepository) : ViewModel() {

    fun screenCreated() {

        viewModelScope.launch {
            for (category in categoryRepository.getCategories()) {
                Timber.d("Found category  ${category.name} with id ${category.id}")
            }
        }
    }
}