package com.example.smw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.example.smw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // a new way to interface with the view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root) // the root is the constraint layout

        binding?.flStart?.setOnClickListener {// binding now replaces findByID code
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }
    }

    // this is necessary to ensure absence of data leakage
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}