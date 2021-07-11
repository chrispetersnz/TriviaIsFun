package com.chrispetersnz.triviaisfun.quiz.result

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chrispetersnz.triviaisfun.MainActivity
import com.chrispetersnz.triviaisfun.R
import com.google.android.material.button.MaterialButton

class ResultActivity : AppCompatActivity() {

    companion object {
        const val RESULT_ID = "RARID"
    }

    val poorResult = arrayOf("TERRIBLE", "AWFUL", "WOEFUL", "APPALLING", "DREADFUL")
    val mediocreResult = arrayOf("OK", "NOT BAD", "ACCEPTABLE", "ALRIGHT")
    val goodResult = arrayOf("GREAT", "AWESOME", "BRILLIANT", "IMPRESSIVE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val result = intent.getIntExtra(RESULT_ID, -1)
        if (result == -1) {
            finish()
        }

        findViewById<TextView>(R.id.messageText).text =
            when {
                result < 5 -> {
                    "That was ${poorResult[((Math.random() * 5).toInt())]}"
                }
                result < 7 -> {
                    "That was ${mediocreResult[((Math.random() * 5).toInt())]}"
                }
                result < 10 -> {
                    "That was ${goodResult[((Math.random() * 5).toInt())]}"
                }
                else -> {
                    "That was perfect!"
                }
            }

        findViewById<TextView>(R.id.resultText).text = "You scored $result out of 10"

        findViewById<MaterialButton>(R.id.playAgainButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.quitButton).setOnClickListener {
            finish()
        }
    }
}