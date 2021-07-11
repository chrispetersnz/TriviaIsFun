package com.chrispetersnz.triviaisfun.quiz

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrispetersnz.triviaisfun.R
import com.chrispetersnz.triviaisfun.network.TriviaDBProvider
import com.chrispetersnz.triviaisfun.network.TriviaDBService
import com.chrispetersnz.triviaisfun.util.IHtmlProvider
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class QuizViewModel(
    private val provider: TriviaDBProvider,
    private val htmlProvider: IHtmlProvider
) : ViewModel() {

    val showLoadingSpinner = ObservableBoolean(true)
    val isTrueFalse = ObservableBoolean(false)
    val questionText = ObservableField<String>()
    val scoreText = ObservableField<String>()
    val firstAnswer = ObservableField<String>()
    val secondAnswer = ObservableField<String>()
    val thirdAnswer = ObservableField<String>()
    val fourthAnswer = ObservableField<String>()

    val backgroundColor = ObservableInt(R.color.design_default_color_background)

    private val questions = mutableListOf<TriviaDBService.TriviaQuestion>()

    private var score = 0
    private var position = -1;

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
        // animate the wrong answers away and change the
        // background color depending on success or failure

        if (view !is MaterialButton) {
            return
        }
        val selectedAnswer = view.text
        if (htmlProvider.decodeHtml(questions[position].correctAnswer) == selectedAnswer) {
            score++
            backgroundColor.set(android.R.color.holo_green_dark)
        } else {
            backgroundColor.set(android.R.color.holo_red_dark)
        }
        scoreText.set("Score: $score")

        showNextQuestion()
    }

    fun backPressed() {
        //show a dialog confirming whether or not they want to leave
    }

    private fun showNextQuestion() {
        val question = questions[++position]
        questionText.set(htmlProvider.decodeHtml(question.question))

        val allAnswers = mutableListOf<String>()
        allAnswers.add(question.correctAnswer)
        allAnswers.addAll(question.incorrectAnswers)

        if (question.type == "boolean") {
            isTrueFalse.set(true)
            firstAnswer.set("True")
            secondAnswer.set("False")
            return
        }
        isTrueFalse.set(false)
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