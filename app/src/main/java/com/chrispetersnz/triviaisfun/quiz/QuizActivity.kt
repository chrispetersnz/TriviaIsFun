package com.chrispetersnz.triviaisfun.quiz

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.chrispetersnz.triviaisfun.R
import com.chrispetersnz.triviaisfun.databinding.ActivityQuizBinding
import org.koin.android.ext.android.inject

class QuizActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY_ID = "CID"
    }

    private val viewModel: QuizViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityQuizBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_quiz)
        binding.viewModel = viewModel
        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.screenCreated(intent)
    }

    override fun onBackPressed() {
        viewModel.backPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            viewModel.backPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}