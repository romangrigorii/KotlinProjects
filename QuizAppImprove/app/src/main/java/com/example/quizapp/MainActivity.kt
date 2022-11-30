package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()

        val button : Button = findViewById(R.id.submitButton)
        val textarea : EditText = findViewById(R.id.nameField)

        textarea.setText(intent.getStringExtra(Constants.USER_NAME))

        button.setOnClickListener{
            if (textarea.text.isEmpty()){
                Toast.makeText(this, "Please enter your name! ", Toast.LENGTH_LONG)
            } else {
                val intent = Intent(this, QuizQuestions::class.java) // moves from one activity to another
                intent.putExtra(Constants.USER_NAME, textarea.text.toString())
                startActivity(intent) // starts up the activity of the intent
                finish() // will close the main activity so you cannot got back to it
            }
        }
    }
}