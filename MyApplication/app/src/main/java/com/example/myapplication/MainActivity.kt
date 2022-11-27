package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnCLickMe = findViewById<Button>(R.id.mybutton)
        val tvMyTextView = findViewById<TextView>(R.id.textView)
        var count = 0
        tvMyTextView.text = count.toString()
        btnCLickMe.text = "Count a person"
        btnCLickMe.setOnClickListener {
            btnCLickMe.text = "Add more"
            count += 1
            tvMyTextView.text = count.toString()
            Toast.makeText(this, "+1", Toast.LENGTH_SHORT).show()
        }

    }
}