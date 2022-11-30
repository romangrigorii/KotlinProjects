package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class QuizEnded : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_ended)

        var tvUserName : TextView = findViewById(R.id.nameText)
        val tvScore : TextView = findViewById(R.id.scoreText)
        val btnFinish : Button = findViewById(R.id.btnFinish)

        tvUserName.text = intent.getStringExtra(Constants.USER_NAME)
        tvScore.text = "Your score was ${intent.getIntExtra(Constants.CORRECT_ANSWERS,0)}/${intent.getIntExtra(Constants.TOTAL_QUESTIONS,1)}"

        btnFinish.setOnClickListener{
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constants.USER_NAME, tvUserName.text)
            startActivity(intent)
            finish() // will close the main activity so you cannot got back to it
        }
    }
}