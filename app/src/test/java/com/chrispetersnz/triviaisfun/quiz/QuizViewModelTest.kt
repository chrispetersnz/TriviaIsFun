package com.chrispetersnz.triviaisfun.quiz

import android.content.Intent
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chrispetersnz.triviaisfun.R
import com.chrispetersnz.triviaisfun.network.ITriviaDBProvider
import com.chrispetersnz.triviaisfun.network.TriviaDBService
import com.chrispetersnz.triviaisfun.util.IHtmlProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class QuizViewModelTest {

    @Mock
    private lateinit var htmlProvider: IHtmlProvider

    @Mock
    private lateinit var triviaDbProvider: ITriviaDBProvider

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(htmlProvider.decodeHtml(any())).thenAnswer {
            it.arguments[0]
        }
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial databinding values are set correctly`() {
        //given

        //when
        val viewModel = QuizViewModel(htmlProvider = htmlProvider, provider = triviaDbProvider)

        //then
        assertThat(viewModel.showLoadingSpinner.get(), `is`(true))
        assertThat(viewModel.backgroundColor.get(), `is`(R.color.white))
        assertThat(viewModel.firstAnswer.get(), nullValue())
        assertThat(viewModel.secondAnswer.get(), nullValue())
        assertThat(viewModel.thirdAnswer.get(), nullValue())
        assertThat(viewModel.fourthAnswer.get(), nullValue())
        assertThat(viewModel.firstVisible.get(), `is`(View.GONE))
        assertThat(viewModel.secondVisible.get(), `is`(View.GONE))
        assertThat(viewModel.thirdVisible.get(), `is`(View.GONE))
        assertThat(viewModel.fourthVisible.get(), `is`(View.GONE))
        assertThat(viewModel.questionText.get(), nullValue())
        assertThat(viewModel.scoreText.get(), `is`(" "))
    }

    @Test
    fun `screen resumed requests questions from provider with correct category id`() =
        runBlockingTest {
            //given
            prepareMockTrueFalseResponse()
            val mockIntent = mock(Intent::class.java)
            val category = TriviaDBService.TriviaCategory(1234, "A name!")
            `when`(mockIntent.getSerializableExtra(QuizActivity.CATEGORY_ID)).thenReturn(category)

            val viewModel = QuizViewModel(htmlProvider = htmlProvider, provider = triviaDbProvider)

            //when
            viewModel.screenCreated(mockIntent)

            //then
            verify(triviaDbProvider).loadQuestionsForCategory(category)
        }


    @Test
    fun `boolean question sets answer buttons correctly`() =
        runBlockingTest {
            //given
            prepareMockTrueFalseResponse()
            val mockIntent = mock(Intent::class.java)
            val category = TriviaDBService.TriviaCategory(1234, "A name!")
            `when`(mockIntent.getSerializableExtra(QuizActivity.CATEGORY_ID)).thenReturn(category)

            val viewModel = QuizViewModel(htmlProvider = htmlProvider, provider = triviaDbProvider)

            //when
            viewModel.screenCreated(mockIntent)

            //then
            assertThat(viewModel.firstAnswer.get(), `is`("True"))
            assertThat(viewModel.secondAnswer.get(), `is`("False"))
            assertThat(viewModel.firstVisible.get(), `is`(View.VISIBLE))
            assertThat(viewModel.secondVisible.get(), `is`(View.VISIBLE))
            assertThat(viewModel.thirdVisible.get(), `is`(View.INVISIBLE))
            assertThat(viewModel.fourthVisible.get(), `is`(View.INVISIBLE))
        }

    @Test
    fun `multi choice question sets answer buttons correctly`() =
        runBlockingTest {
            //given
            prepareMockMultiChoiceResponse()
            val mockIntent = mock(Intent::class.java)
            val category = TriviaDBService.TriviaCategory(1234, "A name!")
            `when`(mockIntent.getSerializableExtra(QuizActivity.CATEGORY_ID)).thenReturn(category)

            val viewModel = QuizViewModel(htmlProvider = htmlProvider, provider = triviaDbProvider)

            //when
            viewModel.screenCreated(mockIntent)

            //then
            val allAnswers = listOf("Dane Coles", "Ardie Savea", "George Bridge", "Aaron Smith")
            val answerSet = mutableSetOf<String>()
            answerSet.add(viewModel.firstAnswer.get().orEmpty())
            answerSet.add(viewModel.secondAnswer.get().orEmpty())
            answerSet.add(viewModel.thirdAnswer.get().orEmpty())
            answerSet.add(viewModel.fourthAnswer.get().orEmpty())
            assertThat(answerSet.size, `is`(4))
            allAnswers.forEach {
                assertTrue(answerSet.contains(it))
            }
            assertThat(viewModel.firstVisible.get(), `is`(View.VISIBLE))
            assertThat(viewModel.secondVisible.get(), `is`(View.VISIBLE))
            assertThat(viewModel.thirdVisible.get(), `is`(View.VISIBLE))
            assertThat(viewModel.fourthVisible.get(), `is`(View.VISIBLE))
        }

    private suspend fun prepareMockTrueFalseResponse(): List<TriviaDBService.TriviaQuestion> {
        val questions = mutableListOf<TriviaDBService.TriviaQuestion>()
        questions.add(
            TriviaDBService.TriviaQuestion(
                "Sport", "boolean", "Easy",
                "The All Blacks play rugby?", "True", listOf("False")
            )
        )

        `when`(triviaDbProvider.loadQuestionsForCategory(any())).thenReturn(questions)

        return questions
    }

    private suspend fun prepareMockMultiChoiceResponse(): List<TriviaDBService.TriviaQuestion> {
        val questions = mutableListOf<TriviaDBService.TriviaQuestion>()
        questions.add(
            TriviaDBService.TriviaQuestion(
                "Sport", "Multiple", "Difficult",
                "Which All Black replacement player scored 4 tries against Fiji after coming off the bench?",
                "Dane Coles", listOf("Ardie Savea", "George Bridge", "Aaron Smith")
            )
        )
        `when`(triviaDbProvider.loadQuestionsForCategory(any())).thenReturn(questions)

        return questions
    }
}

private fun <T> any(): T {
    return Mockito.any()
}