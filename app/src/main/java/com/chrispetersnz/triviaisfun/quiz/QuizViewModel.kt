package com.chrispetersnz.triviaisfun.quiz

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrispetersnz.triviaisfun.R
import com.chrispetersnz.triviaisfun.network.ITriviaDBProvider
import com.chrispetersnz.triviaisfun.network.TriviaDBService
import com.chrispetersnz.triviaisfun.util.IHtmlProvider
import com.chrispetersnz.triviaisfun.util.SingleLiveEvent
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizViewModel(
    private val provider: ITriviaDBProvider,
    private val htmlProvider: IHtmlProvider
) : ViewModel() {

    companion object {
        private val QUESTION_PAUSE = 3000L
    }

    val showLoadingSpinner = ObservableBoolean(true)
    val questionText = ObservableField<String>()
    val scoreText = ObservableField<String>(" ")
    val firstAnswer = ObservableField<String>()
    val secondAnswer = ObservableField<String>()
    val thirdAnswer = ObservableField<String>()
    val fourthAnswer = ObservableField<String>()

    val firstVisible = ObservableInt(View.GONE)
    val secondVisible = ObservableInt(View.GONE)
    val thirdVisible = ObservableInt(View.GONE)
    val fourthVisible = ObservableInt(View.GONE)

    val backgroundColor = ObservableInt(R.color.white)

    private val questions = mutableListOf<TriviaDBService.TriviaQuestion>()

    private var score = 0
    private var position = -1;

    private var answered = false

    private val _title = MutableLiveData("0/10")
    val title: LiveData<String> = _title

    private val _result = SingleLiveEvent<Int>()
    val result: LiveData<Int> = _result

    fun screenCreated(intent: Intent) {
        val category =
            intent.getSerializableExtra(QuizActivity.CATEGORY_ID) as TriviaDBService.TriviaCategory
        viewModelScope.launch {
            val response = provider.loadQuestionsForCategory(category = category)
            questions.addAll(response)
            showNextQuestion()
            showLoadingSpinner.set(false)
        }
    }

    fun answerSelected(view: View) {
        if (answered) {
            return
        }

        answered = true
        if (view !is MaterialButton) {
            return
        }
        val selectedAnswer = view.text.toString()
        val correctAnswer = htmlProvider.decodeHtml(questions[position].correctAnswer)
        if (correctAnswer == selectedAnswer) {
            score++
            backgroundColor.set(android.R.color.holo_green_dark)
        } else {
            backgroundColor.set(android.R.color.holo_red_dark)
        }
        scoreText.set("Score: $score")
        animateButtonVisibilty(correctAnswer)

        viewModelScope.launch {
            delay(QUESTION_PAUSE)
            if (position < 9) {
                showNextQuestion()
            } else {
                _result.value = score
            }

        }
    }

    fun backPressed() {
        //show a dialog confirming whether or not they want to leave
    }

    private fun animateButtonVisibilty(correctAnswer: String) {
        firstVisible.set(if (firstAnswer.get() == correctAnswer) View.VISIBLE else View.INVISIBLE)
        secondVisible.set(if (secondAnswer.get() == correctAnswer) View.VISIBLE else View.INVISIBLE)
        thirdVisible.set(if (thirdAnswer.get() == correctAnswer) View.VISIBLE else View.INVISIBLE)
        fourthVisible.set(if (fourthAnswer.get() == correctAnswer) View.VISIBLE else View.INVISIBLE)
    }

    private fun showAllButtons() {
        firstVisible.set(View.VISIBLE)
        secondVisible.set(View.VISIBLE)
        thirdVisible.set(View.VISIBLE)
        fourthVisible.set(View.VISIBLE)
    }

    private fun showNextQuestion() {
        answered = false
        backgroundColor.set(R.color.white)
        showAllButtons();
        val question = questions[++position]
        _title.value = "Question ${position + 1}"
        questionText.set(htmlProvider.decodeHtml(question.question))

        if (question.type == "boolean") {
            firstAnswer.set("True")
            secondAnswer.set("False")
            thirdVisible.set(View.INVISIBLE)
            fourthVisible.set(View.INVISIBLE)
            return
        }
        val allAnswers = mutableListOf<String>()
        allAnswers.add(question.correctAnswer)
        allAnswers.addAll(question.incorrectAnswers)

        allAnswers.shuffle()
        for (i in 0..3) {
            allAnswers[i] = htmlProvider.decodeHtml(allAnswers[i])
        }
        firstAnswer.set(allAnswers[0])
        secondAnswer.set(allAnswers[1])
        thirdAnswer.set(allAnswers[2])
        fourthAnswer.set(allAnswers[3])
    }
}