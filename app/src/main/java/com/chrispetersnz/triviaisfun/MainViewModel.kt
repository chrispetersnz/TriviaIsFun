package com.chrispetersnz.triviaisfun

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrispetersnz.triviaisfun.network.CategoryRepository
import com.chrispetersnz.triviaisfun.network.TriviaDBService
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(val categoryRepository: CategoryRepository) : ViewModel() {

    val showLoadingSpinner = ObservableBoolean(true)

    private val _categories = MutableLiveData<List<TriviaDBService.TriviaCategory>>()

    val categories: LiveData<List<TriviaDBService.TriviaCategory>> = _categories

    var selectedPosition = -1

    fun screenCreated() {
        viewModelScope.launch {
            val loadedCategories = categoryRepository.getCategories()
            _categories.value = loadedCategories
        }
        showLoadingSpinner.set(false)
    }

    fun itemSelected(position: Int, isChecked: Boolean ) {
        Timber.d("Selected position: $position is $isChecked")
    }

    fun startButtonPressed() {

    }
}